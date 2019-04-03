package com.shark.server.proxy;

import com.shark.server.annotation.behavior.Behavior;
import com.shark.util.classes.classloder.ClassLoaderFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Set;

public class JDKProxyFactory extends AbstractProxyFactory implements InvocationHandler {

	@Override
	public Object getProxy(Object origin) {
		super.setOrigin(origin);
		return Proxy.newProxyInstance(ClassLoaderFactory.getClassLoader(),origin.getClass().getInterfaces(),this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (isRemote(proxy)){
			return remoteInvoke(proxy,method,args);
		}else {
			//对该对象的字段注入bean
			Set<Behavior> reinforceSet= new LinkedHashSet<>();
			//前置增强
			preReinforce(proxy,method,reinforceSet);
			Object result=method.invoke(getOrigin(),args);
			//后置增强
			rearReinforce(proxy,method,reinforceSet);
			return result;
		}
	}
}
