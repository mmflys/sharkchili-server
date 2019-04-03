package com.shark.server.socket.socket;

import io.netty.bootstrap.AbstractBootstrap;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
public abstract class AbstractSharkServer extends AbstractSharkSocket {
	@Override
	public SharkSocket bootstrap(AbstractBootstrap bootstrap) {
		super.bootstrap(bootstrap);
		getBootstrap().localAddress(getPort());
		return this;
	}
}
