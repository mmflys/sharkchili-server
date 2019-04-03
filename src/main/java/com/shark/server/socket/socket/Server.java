package com.shark.server.socket.socket;

import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: SuLiang
 * @Date: 2018/9/12 0012
 * @Description:
 */
public class Server extends AbstractSharkServer {
	private static final Logger LOGGER= LoggerFactory.getLogger(Server.class);

	@Override
	public SharkSocket init() {
		LOGGER.info("Server init");
		return this;
	}

	@Override
	public SharkSocket start() throws InterruptedException {
		LOGGER.info("Server bind port {}",getPort());
		ChannelFuture future=getBootstrap().bind().syncUninterruptibly();
		setChannel(future.channel());
		setStatus(Status.RUNNING);
		// 关闭时抛出异常
		future.channel().closeFuture().syncUninterruptibly();
		return this;
	}

	@Override
	public SharkSocket stop() {
		LOGGER.info("Server stop");
		setStatus(Status.STOP);
		return this;
	}
}
