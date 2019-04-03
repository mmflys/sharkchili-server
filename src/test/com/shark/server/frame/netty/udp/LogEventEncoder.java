package com.shark.server.frame.netty.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
	private final InetSocketAddress remoteAddress;

	public LogEventEncoder(InetSocketAddress remoteAddress) {  //1
		this.remoteAddress = remoteAddress;
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, LogEvent logEvent, List<Object> out) throws Exception {
		byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8); //2
		byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
		ByteBuf buf = channelHandlerContext.alloc().buffer(file.length + msg.length + 1);
		buf.writeBytes(file);
		buf.writeByte(LogEvent.SEPARATOR); //3
		buf.writeBytes(msg);  //4
		out.add(new DatagramPacket(buf, remoteAddress));  //5
	}
}

