package com.shark.server.socket.factory;

import com.shark.server.socket.initializer.DelimiterHandlerInitializer;
import com.shark.server.socket.initializer.DispatcherHandlerInitializer;
import com.shark.server.socket.initializer.LengthBasedInitializer;
import com.shark.server.socket.initializer.WebSocketChannelInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * @Author: SuLiang
 * @Date: 2018/9/12 0012
 * @Description:
 */
public class HandlerInitializerFactory {

	/**
	 * Get a web socket initializer {@link WebSocketChannelInitializer} and use EventExecutor {@link ImmediateEventExecutor}
	 * @return
	 */
	static ChannelInitializer webSocketInitializer(){
		return new WebSocketChannelInitializer();
	}

	static ChannelInitializer delimiterHandlerInitializer(){
		return new DelimiterHandlerInitializer();
	}

	static ChannelInitializer lengthBasedInitializer(){
		return new LengthBasedInitializer();
	}

	static ChannelInitializer dispatcherInitializer(){
		return new DispatcherHandlerInitializer();
	}
}
