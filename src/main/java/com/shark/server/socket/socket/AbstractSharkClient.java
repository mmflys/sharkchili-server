package com.shark.server.socket.socket;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
public abstract class AbstractSharkClient extends AbstractSharkSocket {

	@Override
	public SharkSocket bootstrap(AbstractBootstrap bootstrap) {
		super.bootstrap(bootstrap);
		Bootstrap clientBootstrap= (Bootstrap) getBootstrap();
		clientBootstrap.remoteAddress(getRemoteAddress(),getPort());
		setStatus(Status.INIT);
		return this;
	}
}