package com.shark.server.socket.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: SuLiang
 * @Date: 2018/9/12 0012
 * @Description:
 */
public class Client extends AbstractSharkClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

	@Override
	public SharkSocket init() {
		LOGGER.info("Client init");
		return this;
	}

	@Override
	public SharkSocket start() throws InterruptedException {
		LOGGER.info("Client start");
		ChannelFuture future = ((Bootstrap)getBootstrap()).connect().syncUninterruptibly();
		Channel channel = future.channel();

		setChannel(channel);
		setStatus(Status.RUNNING);

		request("Hello remote server.");
		// 关闭时抛出异常
		future.channel().closeFuture().syncUninterruptibly();
		return this;
	}

	@Override
	public SharkSocket stop() {
		LOGGER.info("Client stop");
		setStatus(Status.STOP);
		return this;
	}
}
