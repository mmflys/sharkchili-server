package com.shark.server.frame.netty.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @Author: SuLiang
 * @Date: 2018/9/10 0010
 * @Description: 使用https加解码,且压缩数据(Netty 支持“gzip”和“deflate”)
 */
public class HttpsAggregatorInitializer extends ChannelInitializer<Channel> {
	private final SslContext context;
	private final boolean isClient;

	public HttpsAggregatorInitializer(SslContext context, boolean isClient) {
		this.context = context;
		this.isClient = isClient;
	}
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		SSLEngine engine = context.newEngine(ch.alloc());
		pipeline.addFirst("ssl", new SslHandler(engine));  //1

		if (isClient) {
			pipeline.addLast("codec", new HttpClientCodec()); //2
			pipeline.addLast("decompressor",new HttpContentDecompressor()); //3
		} else {
			pipeline.addLast("codec", new HttpServerCodec()); //4
			pipeline.addLast("compressor",new HttpContentCompressor()); //5
		}
	}
}


