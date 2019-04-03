package com.shark.server.frame.netty.initializer;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description: 支持异步写大数据流不引起高内存消耗
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {
	private final File file;
	private final SslContext sslCtx;

	public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
		this.file = file;
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new SslHandler(sslCtx.newEngine(ch.alloc()))); //1
		pipeline.addLast(new ChunkedWriteHandler());//2
		pipeline.addLast(new WriteStreamHandler());//3
	}

	public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {  //4

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
			ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
		}
	}
}

