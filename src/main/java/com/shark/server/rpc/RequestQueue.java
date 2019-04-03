package com.shark.server.rpc;

import com.google.common.collect.Maps;
import com.shark.server.exception.RemoteServiceException;
import com.shark.server.socket.message.Request;
import com.shark.server.socket.message.Response;
import com.shark.server.socket.message.Type;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: SuLiang
 * @Date: 2018/9/22 0022
 * @Description: Block to request to wait for a response
 */
public class RequestQueue {

	private Map<Request, Response> requestResponseMap= Maps.newConcurrentMap();
	private Map<Request,Thread> requestThreadMap=Maps.newConcurrentMap();
	private static final long WAIT_TIME_MAX= TimeUnit.SECONDS.toMillis(3);
	private static final Response EMPTY_RESPONSE= Response.empty();

	/**
	 * Request and wait fo response.
	 * @param request
	 * @return
	 */
	public Response blockRequest(Request request){
		requestResponseMap.put(request,EMPTY_RESPONSE);
		// 如果是阻塞请求则注册一个监听器
		if (request.getType()!= Type.BLOCK){
			throw  new RemoteServiceException("The <request> is not a block request. {}",request);
		}
		addListener(request);
		return getResponse(request);
	}

	/**
	 * Request and return immediately
	 * @param request null
	 */
	public Object noBlockRequest(Request request){
		requestResponseMap.put(request,null);
		if (request.getType()== Type.BLOCK){
			throw  new RemoteServiceException("The <request> is a block request. {}",request);
		}
		return null;
	}

	/**
	 * Block request and make current thread wait
	 * @param request
	 */
	private void addListener(Request request) {
		requestThreadMap.put(request,Thread.currentThread());
		try {
			synchronized (Thread.currentThread()){
				Thread.currentThread().wait(WAIT_TIME_MAX);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Notify thread for corresponding request
	 * @param request
	 */
	private void notify(Request request){
		synchronized (requestThreadMap.get(request)){
			requestThreadMap.get(request).notify();
		}
	}

	/**
	 * Response to request and if necessary to notify block request thread.
	 * @param response
	 */
	public void response(Response response){
		for (Request request : requestResponseMap.keySet()) {
			if (request.getId()==response.getId()){
				if (request.getType()== Type.BLOCK){
					requestResponseMap.put(request,response);
					notify(request);
				}else {
					// 非阻塞请求不保留响应
					requestResponseMap.remove(request);
				}
				return;
			}
		}
		throw new RemoteServiceException("No request corresponding to the <response> {}",response);
	}

	/**
	 * Invoke this method only when had accepted response corresponding to request.
	 * @param request
	 * @return
	 */
	public Response getResponse(Request request){
		Response response=requestResponseMap.get(request);
		if (response==EMPTY_RESPONSE){
			throw new RemoteServiceException("No response corresponding to the remote <request>",request);
		}
		// Clear request.
		requestResponseMap.remove(request);
		return response;
	}
}
