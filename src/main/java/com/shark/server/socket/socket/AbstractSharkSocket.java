package com.shark.server.socket.socket;

import com.shark.server.socket.message.Message;
import com.shark.server.socket.message.Request;
import com.shark.server.socket.message.Response;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
public abstract class AbstractSharkSocket implements SharkSocket {

	private int port;
	private String remoteAddress;
	private AbstractBootstrap bootstrap;
	private SocketIdentity socketIdentity;
	private Channel channel;
	private Status status;

	{
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
		remoteAddress="localhost";
	}

	@Override
	public SharkSocket port(int port) {
		this.port=port;
		return this;
	}

	@Override
	public SharkSocket remoteAddress(String remoteAddress) {
		this.remoteAddress=remoteAddress;
		return this;
	}

	@Override
	public SharkSocket bootstrap(AbstractBootstrap bootstrap) {
		this.bootstrap=bootstrap;
		return this;
	}

	AbstractBootstrap getBootstrap() {
		return bootstrap;
	}

	int getPort() {
		return port;
	}

	String getRemoteAddress() {
		return remoteAddress;
	}

	public void setSocketIdentity(SocketIdentity socketIdentity) {
		this.socketIdentity = socketIdentity;
	}

	@Override
	public SocketIdentity identity() {
		return socketIdentity;
	}

	public Channel getChannel() {
		return channel;
	}

	void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Status getStatus() {
		return status;
	}

	void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public SharkSocket request(Object object) {
		Message request= Request.create(object);
		getChannel().writeAndFlush(request);
		return this;
	}

	@Override
	public SharkSocket request(Message request) {
		getChannel().writeAndFlush(request);
		return this;
	}

	@Override
	public SharkSocket response(Object object) {
		Message response= Response.create(object);
		getChannel().writeAndFlush(response);
		return this;
	}

	@Override
	public SharkSocket response(Message response) {
		getChannel().writeAndFlush(response);
		return this;
	}

	@Override
	public SharkSocket identity(SocketIdentity socketIdentity) {
		setSocketIdentity(socketIdentity);
		return this;
	}

	@Override
	public String toString() {
		return "AbstractSharkSocket{" +
				"port=" + port +
				", remoteAddress='" + remoteAddress + '\'' +
				", status=" + status +
				'}';
	}
}
