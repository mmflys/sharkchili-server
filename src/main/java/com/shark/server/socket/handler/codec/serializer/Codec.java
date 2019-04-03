package com.shark.server.socket.handler.codec.serializer;

import io.netty.handler.codec.MessageToMessageCodec;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description: Serialize instance enum.
 */
public enum Codec {

	/**
	 * {@link JsonHandlerCodec}
	 */
	JSON {
		@Override
		public MessageToMessageCodec codec() {
			return Codec.JSON_CODEC;
		}
	},

	/**
	 * {@link StringHandlerCodec}
	 */
	STRING {
		@Override
		public MessageToMessageCodec codec() {
			return Codec.STRING_CODEC;
		}
	};


	private static final MessageToMessageCodec JSON_CODEC = new JsonHandlerCodec();
	private static final MessageToMessageCodec STRING_CODEC = new StringHandlerCodec();

	public abstract MessageToMessageCodec codec();
}
