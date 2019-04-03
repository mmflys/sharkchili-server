package com.shark.server.socket.message;

import com.shark.server.socket.socket.SocketIdentity;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description: Remote peer request message.
 */
public class Request extends AbstractMessage{

	public Request() {
		setId(messageId());
	}

	private Request(Object body, SocketIdentity senderId, SocketIdentity accepterId) {
		super(body, senderId, accepterId);
		setId(messageId());
	}

	public static Request create(Object body){
		Request request=new Request();
		request.setBody(body);
		return request;
	}

	public static Request create(Object body, SocketIdentity senderId, SocketIdentity accepterId){
		return new Request(body, senderId, accepterId);
	}

}
