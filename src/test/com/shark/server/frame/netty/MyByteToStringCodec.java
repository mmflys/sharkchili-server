package com.shark.server.frame.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/9/10 0010
 * @Description:
 */
public class MyByteToStringCodec extends ByteToMessageCodec<String> {

	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
		out.writeCharSequence(msg, CharsetUtil.UTF_8);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes()>=4){
			out.add(in.readCharSequence(4,CharsetUtil.UTF_8));
		}
	}
}
