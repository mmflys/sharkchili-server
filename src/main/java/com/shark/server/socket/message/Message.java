package com.shark.server.socket.message;

import com.shark.server.socket.socket.SocketIdentity;

import java.io.Serializable;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description: A bean that is transport to remote peer.
 */
public interface Message extends Serializable {
	Object getBody();
	void setBody(Object body);
	Object getValue(String key);
	void putValue(String key, Object value);
	SocketIdentity getAccepterId();
	SocketIdentity getSenderId();
	int getId();
	Message type(Type type);
}
