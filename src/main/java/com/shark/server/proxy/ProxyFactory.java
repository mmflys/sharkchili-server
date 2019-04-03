package com.shark.server.proxy;

public interface ProxyFactory {

	void setOrigin(Object object);

	Object getOrigin();

	Object getProxy(Object origin);

}
