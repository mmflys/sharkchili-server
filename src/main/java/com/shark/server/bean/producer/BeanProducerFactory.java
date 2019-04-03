package com.shark.server.bean.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by SuLiang on 2018/8/29 0029
 */
public class BeanProducerFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanProducerFactory.class);

	/**
	 * Create a BeanProducer
	 * @return
	 */
	public static BeanProducer getBeanProducer() {
		//根据情况选择生成器
		BeanProducer beanProducer = new BeanProducerByName();
		return beanProducer;
	}
}
