package com.shark.server.socket.handler.decoder;

import com.shark.server.container.MainContainer;
import com.shark.server.socket.message.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/9/22 0022
 * @Description:
 */
public class RemoteResponseHandler extends MessageToMessageDecoder<Response> {
	private static final Logger LOGGER= LoggerFactory.getLogger(RemoteResponseHandler.class);

	/**
	 * Decode from one message to an other. This method will be called for each written message that can be handled
	 * by this encoder.
	 *
	 * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageDecoder} belongs to
	 * @param msg the message to decode to an other one
	 * @param out the {@link List} to which decoded messages should be added
	 * @throws Exception is thrown if an error occurs
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, Response msg, List<Object> out) throws Exception {
		LOGGER.info("Remote response: {}",msg);
		MainContainer.get().getRequestQueue().response(msg);
	}
}
