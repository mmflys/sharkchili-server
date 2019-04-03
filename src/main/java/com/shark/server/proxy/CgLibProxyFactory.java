package com.shark.server.proxy;

import com.shark.server.annotation.behavior.Behavior;
import com.shark.server.exception.SystemException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

public class CgLibProxyFactory extends AbstractProxyFactory implements MethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CgLibProxyFactory.class);

	@Override
	public Object getProxy(Object origin) {
		super.setOrigin(origin);
		Enhancer enhancer=new Enhancer();
		enhancer.setSuperclass(getOrigin().getClass());
		//设置回调：对于代理类上所有方法的调用，都会调用CallBack，而Callback则需要实现intercept()方法进行拦
		enhancer.setCallback(this);
		return enhancer.create();
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (isRemote(getOrigin())){
			return remoteInvoke(getOrigin(),method,args);
		}else {
			try {
				Set<Behavior> reinforceSet = new LinkedHashSet<>();
				//前置增强
				preReinforce(getOrigin(),method,reinforceSet);
				Object result =method.invoke(getOrigin(),args);
				//后置增强
				rearReinforce(getOrigin(),method,reinforceSet);
				return result;
			}catch (Exception e){
				e.printStackTrace();
				throw new SystemException("Invoke method com.shark.job.exception method {},object {}",method.getName(),obj);
			}
		}
	}
}
