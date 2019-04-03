package com.shark.server.frame.netty.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> { //1

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace(); //2
		ctx.close();
	}

	@Override
	public void channelRead0(ChannelHandlerContext channelHandlerContext, LogEvent event) throws Exception {
		StringBuilder builder = new StringBuilder(); //3
		builder.append(event.getReceivedTimestamp());
		builder.append(" [");
		builder.append(event.getSource().toString());
		builder.append("] [");
		builder.append(event.getLogfile());
		builder.append("] : ");
		builder.append(event.getMsg());

		System.out.println(builder.toString()); //4
	}
}

