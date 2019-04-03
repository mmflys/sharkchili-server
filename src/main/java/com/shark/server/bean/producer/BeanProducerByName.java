package com.shark.server.bean.producer;

import com.shark.server.bean.Bean;
import com.shark.util.util.StringUtil;


/**
 * Produce message that the first letter of the name is firstLowercase
 */
public class BeanProducerByName extends AbstractBeanProducer {

	@Override
	public Bean produce(Class<?> C, boolean proxy) {
		Bean bean = super.produce(C, proxy);
		String className = C.getSimpleName();
		bean.setName(StringUtil.firstLowercase(className));
		return bean;
	}
}
