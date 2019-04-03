package com.shark.server.frame.netty.sypdy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import org.eclipse.jetty.npn.NextProtoNego;

import javax.net.ssl.SSLEngine;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
public class SpdyChannelInitializer extends ChannelInitializer<SocketChannel> {  //1
	private final SslContext context;

	public SpdyChannelInitializer(SslContext context)  {//2
        this.context = context;
}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		SSLEngine engine = context.newEngine(ch.alloc());  //3
		engine.setUseClientMode(false);  //4

		NextProtoNego.put(engine, new DefaultServerProvider());  //5
		NextProtoNego.debug = true;

		pipeline.addLast("sslHandler", new SslHandler(engine));  //6
		//pipeline.addLast("chooser", new DefaultSpdyOrHttpChooser(1024 * 1024, 1024 * 1024));
	}
}

