package com.shark.server.socket.handler.codec;

import io.netty.handler.codec.MessageToMessageCodec;

/**
 * @Author: SuLiang
 * @Date: 2018/9/14 0014
 * @Description:
 */
public abstract class AbstractMessageToMessageCodec<INBOUND_IN, OUTBOUND_IN> extends MessageToMessageCodec<INBOUND_IN, OUTBOUND_IN> {

}
