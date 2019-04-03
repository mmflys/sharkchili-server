package com.shark.server.socket.handler.decoder;

import com.shark.server.rpc.data.RpcData;
import com.shark.server.socket.message.Message;
import com.shark.server.socket.message.Request;
import com.shark.server.socket.message.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/9/20 0020
 * @Description: A handler for remote request
 */
public class RemoteRequestHandler extends MessageToMessageDecoder<Request>{
	private static final Logger LOGGER= LoggerFactory.getLogger(RemoteRequestHandler.class);

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
	protected void decode(ChannelHandlerContext ctx, Request msg, List<Object> out) throws Exception {
		Message response= Response.create(msg);
		if (RpcData.class.isAssignableFrom(msg.getBody().getClass())) {
			LOGGER.info("Remote request: {}",msg);
			RpcData rpcData = (RpcData) msg.getBody();
			try {
				Object returnValue=rpcData.invoke();
				// response
				response.setBody(returnValue);
				ctx.channel().writeAndFlush(response);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}else {
			LOGGER.info("Receive remote message: {}",msg);
		}
	}
}
