package com.shark.server.socket.socket;

import com.shark.server.socket.message.Message;
import io.netty.bootstrap.AbstractBootstrap;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description: Receive socket message.
 */
public interface SharkSocket {

	SharkSocket port(int port);

	SharkSocket remoteAddress(String remoteAddress);

	SharkSocket bootstrap(AbstractBootstrap bootstrap);

	SharkSocket init();

	SharkSocket start() throws InterruptedException;

	SharkSocket stop();

	SocketIdentity identity();

	SharkSocket identity(SocketIdentity socketIdentity);

	SharkSocket request(Object object);

	SharkSocket request(Message request);

	SharkSocket response(Object object);

	SharkSocket response(Message response);

	Status getStatus();
}
