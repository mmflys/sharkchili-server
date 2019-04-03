package com.shark.server.socket.handler.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/9/15 0015
 * @Description: A encoder that add a delimiter to fail of the message.
 */
@ChannelHandler.Sharable
public class DelimiterBasedFrameEncode extends MessageToMessageEncoder<ByteBuf> {

	private final String delimiter;

	public DelimiterBasedFrameEncode(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * Encode from one message to an other. This method will be called for each written message that can be handled
	 * by this encoder.
	 *
	 * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageEncoder} belongs to
	 * @param msg the message to encode to an other one
	 * @param out the {@link List} into which the encoded msg should be added
	 *            needs to do some kind of aggregation
	 * @throws Exception is thrown if an error occurs
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		out.add(msg.retain().writeBytes(delimiter.getBytes()));
	}
}