package com.shark.server.frame.netty.sypdy;

import io.netty.channel.ChannelHandler;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
@ChannelHandler.Sharable
public class SpdyRequestHandler extends HttpRequestHandler {   //1
	@Override
	protected String getContent() {
		return "This content is transmitted via SPDY\r\n";  //2
	}
}

