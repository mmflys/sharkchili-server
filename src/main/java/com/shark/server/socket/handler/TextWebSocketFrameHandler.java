package com.shark.server.socket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 完成握手
		if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
			ctx.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " connected"));
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		// 发消息给所有客户端
		//group.writeAndFlush(msg.retain());
	}
}

