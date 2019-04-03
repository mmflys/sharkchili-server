package com.shark.server.bean.producer;

import com.shark.server.bean.Bean;
import com.shark.server.bean.DefaultBean;
import com.shark.server.proxy.ProxcyFactoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBeanProducer implements BeanProducer {

	private static final Logger LOGGER= LoggerFactory.getLogger(AbstractBeanProducer.class);

	@Override
	public final Bean produce(Class<?> C) {
		return produce(C,true);
	}

	@Override
	public Bean produce(Class<?> C, boolean proxy) {
		//先默认bean
		Bean bean=new DefaultBean();
		try {
			//生成原对象
			Object object = C.newInstance();
			bean.setObject(object);
			//生成代理对象
			if (proxy){
				Object proxyObj= ProxcyFactoryFactory.getProxyFactory(C).getProxy(object);
				bean.setProxy(proxyObj);
			}else {
				bean.setProxy(object);
			}
			bean.setCreateTime(System.currentTimeMillis());
			bean.setUpdateTime(System.currentTimeMillis());
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("Reflect new instance failed {}",C);
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public final Bean produce(Class<?> C, String name) {
		Bean bean=produce(C,true);
		bean.setName(name);
		return bean;
	}

	@Override
	public final Bean produce(Class<?> C, String name, boolean proxy) {
		Bean bean=produce(C,proxy);
		bean.setName(name);
		return bean;
	}
}
