package com.shark.server.socket.handler.codec.serializer;

import com.shark.server.socket.handler.codec.AbstractMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description: String serialize
 */
@ChannelHandler.Sharable
public class StringHandlerCodec extends AbstractMessageToMessageCodec<ByteBuf,String> {
	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		if (msg.length() == 0) {
			return;
		}
		out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), Charset.defaultCharset()));
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		out.add(msg.toString(Charset.defaultCharset()));
	}
}
