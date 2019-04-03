package com.shark.server.socket.initializer;

import com.shark.server.socket.consts.CodecConst;
import com.shark.server.socket.handler.ServerHandler;
import com.shark.server.socket.handler.codec.serializer.Codec;
import com.shark.server.socket.handler.codec.serializer.JsonHandlerCodec;
import com.shark.server.socket.handler.decoder.RemoteRequestHandler;
import com.shark.server.socket.handler.decoder.RemoteResponseHandler;
import com.shark.server.socket.handler.encoder.DelimiterBasedFrameEncode;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * @Author: SuLiang
 * @Date: 2018/9/13 0013
 * @Description: A handler initializer that use {@link DelimiterBasedFrameDecoder},{@link DelimiterBasedFrameEncode},{@link JsonHandlerCodec}
 */
public class DelimiterHandlerInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		addHandler(ch);
	}

	public static void addHandler(Channel channel){
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new DelimiterBasedFrameDecoder(CodecConst.FRAME_LENGTH_MAX, true, Unpooled.copiedBuffer(CodecConst.DELIMITER.getBytes())));
		pipeline.addLast(new DelimiterBasedFrameEncode(CodecConst.DELIMITER));
		pipeline.addLast(Codec.JSON.codec());
		pipeline.addLast(new ServerHandler());
		pipeline.addLast(new RemoteRequestHandler());
		pipeline.addLast(new RemoteResponseHandler());
	}

	public static void removeHandler(Channel channel){
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.remove(DelimiterBasedFrameDecoder.class);
		pipeline.remove(DelimiterBasedFrameEncode.class);
		pipeline.remove(JsonHandlerCodec.class);
		pipeline.remove(ServerHandler.class);
		pipeline.remove(new RemoteRequestHandler());
		pipeline.remove(new RemoteResponseHandler());
	}

}
