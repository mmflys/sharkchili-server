package com.shark.server.frame.netty.initializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * @Author: SuLiang
 * @Date: 2018/9/10 0010
 * @Description:
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline().addLast(
				new HttpServerCodec(),
				new HttpObjectAggregator(65536),  //1
				new WebSocketServerProtocolHandler("/websocket"),  //2
				new TextFrameHandler(),  //3
				new BinaryFrameHandler(),  //4
				new ContinuationFrameHandler());  //5
				new PingWebSocketFrameHandler();
				new PongWebSocketFrameHandler();
				new CloseWebSocketFrameHandler();
				//new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS);
				//new HeartbeatHandler();
	}

	public static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
		@Override
		public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
			// Handle text frame
		}
	}

	public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
		@Override
		public void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
			// Handle binary frame
		}
	}

	public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
		@Override
		public void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg) throws Exception {
			// Handle continuation frame
		}
	}

	public static final class PingWebSocketFrameHandler extends SimpleChannelInboundHandler<PingWebSocketFrame> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, PingWebSocketFrame msg) throws Exception {
			System.out.println("ping: "+msg);
		}
	}

	public static final class PongWebSocketFrameHandler extends SimpleChannelInboundHandler<PongWebSocketFrame> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, PongWebSocketFrame msg) throws Exception {
			System.out.println("pong");
		}
	}

	public static final class CloseWebSocketFrameHandler extends SimpleChannelInboundHandler<CloseWebSocketFrame> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, CloseWebSocketFrame msg) throws Exception {
			System.out.println("websocket closed");
		}
	}

	public static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {
		private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1));  //2

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
