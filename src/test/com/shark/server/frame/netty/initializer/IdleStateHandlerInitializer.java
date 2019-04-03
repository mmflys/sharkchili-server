package com.shark.server.frame.netty.initializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Author: SuLiang
 * @Date: 2018/9/10 0010
 * @Description:
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));  //1
		pipeline.addLast(new HeartbeatHandler());
	}

	public static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {
		private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
				Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1));  //2

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (evt instanceof IdleStateEvent) {
				ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);  //3
			} else {
				super.userEventTriggered(ctx, evt);  //4
			}
		}
	}
}

