package com.shark.server.socket.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @Author: SuLiang
 * @Date: 2018/9/12 0012
 * @Description: A handler for http request.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private final static Logger LOGGER= LoggerFactory.getLogger(HttpRequestHandler.class);

	private final String wsUri;

	public HttpRequestHandler(String wsUri) {
		this.wsUri = wsUri;
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		// Judge whether request is web socket request.
		if (wsUri.equalsIgnoreCase(request.uri())) {
			ctx.fireChannelRead(request.retain());
		} else {
			// 是否是100 continue请求(客户端Post发送数据,一般用于传输大数据)
			if (HttpUtil.is100ContinueExpected(request)) {
				send100Continue(ctx);
			}
			// 构造请求头
			HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
			//response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpRequest.CONTENT_TYPE_JSON+";"+StandardCharsets.UTF_8);
			// 是否保持连接
			boolean keepAlive = HttpUtil.isKeepAlive(request);
			if (keepAlive) {
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.KEEP_ALIVE);
			}
			byte[] sendWord="Connect to SharkServer...".getBytes(StandardCharsets.UTF_8);
			// !!!一定要写这个属性,否则,浏览器会一直阻塞
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH,sendWord.length);
			// 写入响应
			ctx.write(response);
			ctx.write(Unpooled.copiedBuffer(sendWord));
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			if (!keepAlive) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
	}

	private static void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
