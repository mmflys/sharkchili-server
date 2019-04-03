package com.shark.server.frame.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: SuLiang
 * @Date: 2018/9/7 0007
 * @Description:
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("AbstractSharkServer received: " + msg);
		ctx.writeAndFlush(msg);
	}
}
