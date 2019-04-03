package com.shark.server.frame.netty.initializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * @Author: SuLiang
 * @Date: 2018/9/10 0010
 * @Description: 基于分隔符
 */
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new LineBasedFrameDecoder(65 * 1024));   //1
		pipeline.addLast(new FrameHandler());  //2
	}

	public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {
		@Override
		public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {  //3
			// Do something with the frame
		}
	}
}

