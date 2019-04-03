package com.shark.server.socket.factory;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;

/**
 * @Author: SuLiang
 * @Date: 2018/9/12 0012
 * @Description:
 */
public class BootstrapFactory {

	/**
	 * Get a ServerBootstrap that use nio
	 *
	 * @param channelInitializer
	 * @return
	 */
	private static ServerBootstrap serverNioBootstrap(ChannelInitializer channelInitializer) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(channelInitializer);
		return serverBootstrap;
	}

	/**
	 * Get a Bootstrap that use nio
	 *
	 * @param channelInitializer
	 * @return
	 */
	private static Bootstrap nioBootstrap(ChannelInitializer channelInitializer) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup())
				.channel(NioSocketChannel.class)
				.handler(channelInitializer);
		return bootstrap;
	}

	/**
	 * Get a ServerBootstrap that use nio
	 *
	 * @param channelInitializer
	 * @return ServerBootstrap
	 */
	private static ServerBootstrap serverOioBootstrap(ChannelInitializer channelInitializer) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(new OioEventLoopGroup())
				.channel(OioServerSocketChannel.class)
				.childHandler(channelInitializer);
		return serverBootstrap;
	}

	/**
	 * Get a Bootstrap that use oio
	 *
	 * @param channelInitializer
	 * @return Bootstrap
	 */
	private static Bootstrap oioBootstrap(ChannelInitializer channelInitializer) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new OioEventLoopGroup())
				.channel(OioSocketChannel.class)
				.handler(channelInitializer);
		return bootstrap;
	}

	/**
	 * Get a ServerBootstrap that use nio,webSocket
	 *
	 * @return ServerBootstrap
	 */
	static ServerBootstrap serverWebSocketNioBootstrap() {
		return serverNioBootstrap(HandlerInitializerFactory.webSocketInitializer());
	}

	/**
	 * Get Bootstrap that use nio,webSocket
	 *
	 * @return Bootstrap
	 */
	static Bootstrap webSocketNioBootstrap() {
		return nioBootstrap(HandlerInitializerFactory.webSocketInitializer());
	}

	/**
	 * Get a Bootstrap that use nio,delimiter handler
	 *
	 * @return Bootstrap
	 */
	static Bootstrap delimiterNioBootstrap() {
		return nioBootstrap(HandlerInitializerFactory.delimiterHandlerInitializer());
	}

	/**
	 * Get a ServerBootstrap that use nio,delimiter handler
	 *
	 * @return ServerBootstrap
	 */
	static ServerBootstrap delimiterNioServerBootstrap() {
		return serverNioBootstrap(HandlerInitializerFactory.delimiterHandlerInitializer());
	}

	/**
	 * Get a Bootstrap that use nio,delimiter handler
	 *
	 * @return Bootstrap
	 */
	static Bootstrap lengthNioBootstrap() {
		return nioBootstrap(HandlerInitializerFactory.lengthBasedInitializer());
	}

	/**
	 * Get a ServerBootstrap that use nio,delimiter handler
	 *
	 * @return ServerBootstrap
	 */
	static ServerBootstrap lengthNioServerBootstrap() {
		return serverNioBootstrap(HandlerInitializerFactory.lengthBasedInitializer());
	}

	/**
	 * Get a Bootstrap that use nio and use length or delimiter or http dynamically.
	 *
	 * @return Bootstrap
	 */
	static Bootstrap dynamicNioBootstrap() {
		return nioBootstrap(HandlerInitializerFactory.dispatcherInitializer());
	}

	/**
	 * Get a ServerBootstrap that use nio and use length or delimiter or http dynamically.
	 *
	 * @return ServerBootstrap
	 */
	static ServerBootstrap dynamicNioServerBootstrap() {
		return serverNioBootstrap(HandlerInitializerFactory.dispatcherInitializer());
	}
}
