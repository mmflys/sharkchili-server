package com.shark.server.socket.handler.codec.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.shark.server.socket.handler.codec.AbstractMessageToMessageCodec;
import com.shark.server.socket.message.Request;
import com.shark.server.socket.message.Response;
import com.shark.util.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/9/13 0013
 * @Description: Json serialize,use FastJson
 */
@ChannelHandler.Sharable
public class JsonHandlerCodec extends AbstractMessageToMessageCodec<ByteBuf, Object> {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonHandlerCodec.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
		byte[] bytes = JSON.toJSONBytes(msg);
		out.add(ctx.alloc().buffer(bytes.length).writeBytes(JSON.toJSONBytes(msg, SerializerFeature.WriteClassName)));
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		String jsonStr = msg.toString(Charset.defaultCharset());
		if (jsonStr != null && !jsonStr.equals("") && !StringUtil.isNewLineChar(jsonStr)) {
			try {
				Object object = JSON.parseObject(jsonStr, Response.class);
				out.add(object);
				LOGGER.debug("json deserialize object: {}", object);
			}catch (JSONException e){
				try {
					Object object=JSON.parseObject(jsonStr, Request.class);
					out.add(object);
					LOGGER.debug("json deserialize object: {}", object);
				} catch (JSONException e1) {
					LOGGER.error("The ByteBuf did`t support deserialize,json string {}",jsonStr);
				}
			}
		}
	}
}
