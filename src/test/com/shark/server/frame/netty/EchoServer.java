package com.shark.server.frame.netty;

import com.shark.server.frame.netty.initializer.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: SuLiang
 * @Date: 2018/9/7 0007
 * @Description:
 */
public class EchoServer {

	private final int port;

	public EchoServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		/*if (args.length != 1) {
			System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
			return;
		}*/
		//int port = Integer.parseInt(args[0]);        //1
		new EchoServer(8080).start();                //2
	}

	public void start() throws Exception {
		NioEventLoopGroup group = new NioEventLoopGroup(); //3
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group)                                //4
					.channel(NioServerSocketChannel.class)        //5
					.localAddress(new InetSocketAddress(port))    //6
					.childHandler(new WebSocketServerInitializer());

			ChannelFuture f = b.bind().sync();            //8
			System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
			f.channel().closeFuture().sync();            //9
		} finally {
			group.shutdownGracefully().sync();            //10
		}
	}

}

