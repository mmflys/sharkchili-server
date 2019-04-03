package com.shark.server.frame.netty.sypdy;

import org.eclipse.jetty.npn.NextProtoNego;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: SuLiang
 * @Date: 2018/9/11 0011
 * @Description:
 */
public class DefaultServerProvider implements NextProtoNego.ServerProvider {
	private static final List<String> PROTOCOLS = Collections.unmodifiableList(Arrays.asList("spdy/2", "spdy/3", "http/1.1"));  //1

	private String protocol;

	@Override
	public void unsupported() {
		protocol = "http/1.1";   //2
	}

	@Override
	public List<String> protocols() {
		return PROTOCOLS;   //3
	}

	@Override
	public void protocolSelected(String protocol) {
		this.protocol = protocol;  //4
	}

	public String getSelectedProtocol() {
		return protocol;  //5
	}
}

