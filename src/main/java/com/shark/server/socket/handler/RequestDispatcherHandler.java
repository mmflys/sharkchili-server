package com.shark.server.socket.handler;

import com.shark.server.socket.consts.CodecConst;
import com.shark.server.socket.initializer.DelimiterHandlerInitializer;
import com.shark.server.socket.initializer.WebSocketChannelInitializer;
import com.shark.util.http.HttpUtil;
import com.shark.util.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Author: SuLiang
 * @Date: 2018/9/16 0647
 * @Description: A dispatcher that distribute http request or other request to corresponding initializer handler
 */
public class RequestDispatcherHandler extends SimpleChannelInboundHandler<ByteBuf> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		String jsonStr = msg.retain().toString(Charset.defaultCharset());
		if (!StringUtil.isEmpty(jsonStr)) {
			Map<String, String> httpRequestHeader = HttpUtil.isHttpRequestHeader(jsonStr);
			Attribute<String> requestType = ctx.channel().attr(AttributeKey.valueOf(CodecConst.REQUEST_TYPE));
			if (httpRequestHeader.size() != 0) {
				// Set request type
				requestType.getAndSet(CodecConst.REQUEST_TYPE_HTTP);
				// Add http request handler
				WebSocketChannelInitializer.addHandler(ctx.channel());
			} else {
				// Set request type
				requestType.getAndSet(CodecConst.REQUEST_TYPE_NORMAL);
				// Remove normal request handler
				DelimiterHandlerInitializer.addHandler(ctx.channel());
			}
		}
		// Fire next handler read.
		ctx.fireChannelRead(msg);
	}

}
