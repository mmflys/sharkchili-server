package com.shark.server.frame.netty.initializer;

import com.google.protobuf.MessageLite;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description: 序列化框架,已经绑定各种编程语言,适合跨语言项目
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {

	private final MessageLite lite;

	public ProtoBufInitializer(MessageLite lite) {
		this.lite = lite;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new ProtobufVarint32FrameDecoder());
		pipeline.addLast(new ProtobufEncoder());
		pipeline.addLast(new ProtobufDecoder(lite));
		pipeline.addLast(new ObjectHandler());
	}

	public static final class ObjectHandler extends SimpleChannelInboundHandler<Object> {
		@Override
		public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
			// Do something with the object
		}
	}
}

