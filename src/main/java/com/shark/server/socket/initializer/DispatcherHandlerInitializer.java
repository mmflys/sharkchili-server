package com.shark.server.socket.initializer;

import com.shark.server.socket.handler.RequestDispatcherHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @Author: SuLiang
 * @Date: 2018/9/17 0017
 * @Description:
 */
public class DispatcherHandlerInitializer extends ChannelInitializer<Channel> {
	/**
	 * This method will be called once the {@link Channel} was registered. After the method returns this instance
	 * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
	 *
	 * @param ch the {@link Channel} which was registered.
	 * @throws Exception is thrown if an error occurs. In that case it will be handled by
	 *                   {@link #exceptionCaught(ChannelHandlerContext, Throwable)} which will by default close
	 *                   the {@link Channel}.
	 */
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new RequestDispatcherHandler());
	}
}
