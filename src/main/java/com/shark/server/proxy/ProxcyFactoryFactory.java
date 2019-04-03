package com.shark.server.proxy;

public class ProxcyFactoryFactory {

	public static ProxyFactory getProxyFactory(Class<?> C){
		/*if (C.getInterfaces().length>0){
			return new JDKProxyFactory();
		}else {
			return new CgLibProxyFactory();
		}*/
		// 固定用cglib
		return new CgLibProxyFactory();
	}

}
