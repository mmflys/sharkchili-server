package com.shark.server.socket.handler;

import com.shark.server.socket.message.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: SuLiang
 * @Date: 2018/9/13 0013
 * @Description:
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
	private final static Logger LOGGER= LoggerFactory.getLogger(ServerHandler.class);

	private static ChannelGroup channelGroup=new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		LOGGER.info(ctx.channel().remoteAddress()+": "+msg);
		for (Channel c: channelGroup) {
			if (c != ctx.channel()) {
				c.writeAndFlush(Response.create("[" + ctx.channel().remoteAddress() + "] " + msg));
			} else {
				c.writeAndFlush(Response.create("[you] " + msg));
			}
		}
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String sendWord="Client " + ctx.channel() + " connected";
		LOGGER.info(sendWord);
		if (!channelGroup.contains(ctx.channel())){
			channelGroup.add(ctx.channel());
			//channelGroup.writeAndFlush(Response.create(sendWord));
		}
	}
}
