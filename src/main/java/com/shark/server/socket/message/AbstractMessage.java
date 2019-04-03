package com.shark.server.socket.message;

import com.google.common.collect.Maps;
import com.shark.server.socket.socket.SocketIdentity;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description:
 */
class AbstractMessage implements Message {
	private static AtomicInteger MESSAGE_ID=new AtomicInteger(0);

	/**transport body*/
	private Object body;
	/**other parameters*/
	private Map<String,Object> values;
	/**message id*/
	private int id;
	/**type: {@link Type}*/
	private Type type;
	/**The id of sender*/
	private SocketIdentity senderId;
	/**The id of accepter*/
	private SocketIdentity accepterId;

	AbstractMessage() {
	}

	AbstractMessage(Object body, SocketIdentity senderId, SocketIdentity accepterId) {
		this.body = body;
		this.senderId = senderId;
		this.accepterId = accepterId;
	}

	AbstractMessage(SocketIdentity senderId, SocketIdentity accepterId) {
		this.senderId = senderId;
		this.accepterId = accepterId;
	}

	public Object getBody() {
		return this.body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public Object getValue(String key) {
		return this.values.get(key);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SocketIdentity getSenderId() {
		return senderId;
	}

	public void setSenderId(SocketIdentity senderId) {
		this.senderId = senderId;
	}

	public SocketIdentity getAccepterId() {
		return accepterId;
	}

	public void setAccepterId(SocketIdentity accepterId) {
		this.accepterId = accepterId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void putValue(String key, Object value) {
		if (this.values==null){
			this.values= Maps.newHashMap();
		}
		this.values.put(key,value);
	}

	int messageId(){
		return MESSAGE_ID.incrementAndGet();
	}

	@Override
	public Message type(Type type) {
		setType(type);
		return this;
	}

	@Override
	public String toString() {
		return "AbstractMessage{" +
				"body=" + body +
				", id=" + id +
				'}';
	}
}
