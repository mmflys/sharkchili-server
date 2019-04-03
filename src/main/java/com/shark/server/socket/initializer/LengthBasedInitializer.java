package com.shark.server.socket.initializer;

import com.shark.server.socket.consts.CodecConst;
import com.shark.server.socket.handler.RequestDispatcherHandler;
import com.shark.server.socket.handler.codec.serializer.Codec;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Author: SuLiang
 * @Date: 2018/9/10 0010
 * @Description: 基于长度的数据
 */
public class LengthBasedInitializer extends ChannelInitializer<Channel> {
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new RequestDispatcherHandler());
		pipeline.addLast(new LengthFieldBasedFrameDecoder(CodecConst.FIXED_LENGTH, 0, 8));
		pipeline.addLast(Codec.JSON.codec());
	}
}
