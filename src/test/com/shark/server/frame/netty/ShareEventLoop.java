package com.shark.server.frame.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
public class ShareEventLoop {
	public static void main(String[] args) {
		ServerBootstrap bootstrap = new ServerBootstrap(); //1
		bootstrap.group(new NioEventLoopGroup(), //2
				new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class) //3
				.childHandler(        //4
						new SimpleChannelInboundHandler<ByteBuf>() {
							ChannelFuture connectFuture;

							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								Bootstrap bootstrap = new Bootstrap();//5
								bootstrap.channel(NioSocketChannel.class) //6
										.handler(new SimpleChannelInboundHandler<ByteBuf>() {  //7
											@Override
											protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
												System.out.println("Reveived data");
											}
										});
								bootstrap.group(ctx.channel().eventLoop()); //8
								connectFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));  //9
							}

							@Override
							protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
								if (connectFuture.isDone()) {
									// do something with the data  //10
								}
							}
						});
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));  //11
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				if (channelFuture.isSuccess()) {
					System.out.println("AbstractSharkServer bound");
				} else {
					System.err.println("Bound attempt failed");
					channelFuture.cause().printStackTrace();
				}
			}
		});

	}
}
