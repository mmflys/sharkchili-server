package com.shark.server.socket.factory;

import com.shark.server.socket.socket.Client;
import com.shark.server.socket.socket.Server;
import com.shark.server.socket.socket.SharkSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: SuLiang
 * @Date: 2018/9/17 0017
 * @Description:
 */
public class SharkSocketFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(SharkSocketFactory.class);

	public static SharkSocket server(int port) throws InterruptedException {
		return new Server().port(port).bootstrap(BootstrapFactory.dynamicNioServerBootstrap());
	}

	public static SharkSocket client(String remoteAddress, int port) throws InterruptedException {
		return new Client().port(port).remoteAddress(remoteAddress).bootstrap(BootstrapFactory.delimiterNioBootstrap());
	}
}
