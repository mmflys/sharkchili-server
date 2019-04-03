package com.shark.server.socket.message;

import com.shark.server.socket.socket.SocketIdentity;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description: Response message to request of remote peer.
 */
public class Response extends AbstractMessage{

	public Response() {
	}

	private Response(Object body, SocketIdentity senderId, SocketIdentity accepterId) {
		super(body,senderId, accepterId);
	}

	private Response(SocketIdentity senderId, SocketIdentity accepterId) {
		super(senderId, accepterId);
	}

	public static Response create(Object body){
		Response response=new Response();
		response.setBody(body);
		return response;
	}

	public static Response create(Object body, SocketIdentity senderId, SocketIdentity accepterId){
		return new Response(body, senderId, accepterId);
	}

	public static Response create(Request request){
		Response response=new Response();
		response.setSenderId(request.getAccepterId());
		response.setAccepterId(request.getSenderId());
		response.setId(request.getId());
		return response;
	}

	public static Response create(SocketIdentity senderId, SocketIdentity accepterId){
		return new Response(senderId, accepterId);
	}

	public static Response empty(){
		return new Response();
	}
}
