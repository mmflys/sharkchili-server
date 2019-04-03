package com.shark.server.socket.initializer;

import com.shark.server.socket.consts.CodecConst;
import com.shark.server.socket.handler.HttpRequestHandler;
import com.shark.server.socket.handler.TextWebSocketFrameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @Author: SuLiang
 * @Date: 2018/9/12 0012
 * @Description:
 */
public class WebSocketChannelInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		addHandler(ch);
	}

	public static void addHandler(Channel channel){
		ChannelPipeline pipeline = channel.pipeline();
		// 编解码
		pipeline.addLast(new HttpServerCodec());
		// 聚合消息
		pipeline.addLast(new HttpObjectAggregator(CodecConst.FRAME_LENGTH_MAX));
		// 请求处理器
		pipeline.addLast(new HttpRequestHandler("/ws"));
		// webSocket处理器(已对CloseWebSocketFrame,PingWebSocketFrame,PongWebSocketFrame处理)
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		// 对TextWebSocketFrame处理
		pipeline.addLast(new TextWebSocketFrameHandler());
	}

	public static void removeHandler(Channel channel){
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.remove(HttpServerCodec.class);
		pipeline.remove(HttpObjectAggregator.class);
		pipeline.remove(HttpRequestHandler.class);
		pipeline.remove(WebSocketServerProtocolHandler.class);
		pipeline.remove(TextWebSocketFrameHandler.class);
	}
}
