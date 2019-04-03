package com.shark.server.container;

import com.shark.container.Container;
import com.shark.feifei.db.FeifeiPoolDatasource;
import com.shark.server.annotation.Pit;
import com.shark.server.annotation.behavior.PitBehavior;
import com.shark.server.bean.Bean;
import com.shark.server.bean.producer.BeanProducer;
import com.shark.server.bean.producer.BeanProducerFactory;
import com.shark.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.LinkedHashSet;
import java.util.Set;

public class BeanContainer implements Container {

	private static final Logger LOGGER= LoggerFactory.getLogger(BeanContainer.class);

	/**所有对象*/
	private Set<Bean> beans;
	/**对象生产者*/
	private BeanProducer beanProducer;

	BeanContainer() {
		beans=new LinkedHashSet<>();
		beanProducer= BeanProducerFactory.getBeanProducer();
	}

	@Override
	public Container init() {
		LOGGER.info(BeanContainer.class.getName()+" init");
		//程序结束时调用钩子线程
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
		return this;

	}

	@Override
	public Container start() {
		LOGGER.info(BeanContainer.class.getName()+" start");
		//一些bean的初始化
		//连接池初始化
		FeifeiPoolDatasource poolDatasource = (FeifeiPoolDatasource) MainContainer.get().getBean(DataSource.class);
		poolDatasource.init();

		return this;
	}

	@Override
	public Container stop() {
		LOGGER.info(BeanContainer.class.getName()+" stop");
		return this;
	}

	@Override
	public void containerInit() {

	}

	@Override
	public void containerStart() {

	}

	@Override
	public void containerStop() {

	}

	/**
	 * Produce a object for the class that message name is class firstLowercase and is proxied.
	 * @param C the class of message
	 */
	Bean registerBean(Class<?> C) {
		return registerBean(C, com.shark.util.util.StringUtil.firstLowercase(C.getSimpleName()));
	}

	/**
	 * Produce a message for the class
	 * @param C the class of message
	 */
	public Bean registerBean(Class<?> C, String beanName) {
		Bean bean=beanProducer.produce(C,beanName);
		beans.add(bean);
		//默认设置路径
		bean.setPath(C.getName());
		LOGGER.debug("Register bean {}",bean.getName());
		return bean;
	}

	/**
	 * Get message by name,if it`s existed and to create it,and it`s name is class name that is firstLowercase.
	 * Fill the pit from message after message is created.
	 * @param C the class of message
	 * @param name Bean name
	 * @return
	 */
	Bean getOrCreateBean(Class<?> C, String name){
		Bean bean=existBean(C,name);
		bean=bean==null?registerBean(C,name):bean;
		//注入依赖
		PitBehavior pitBehavior= (PitBehavior) MainContainer.get().getClassContainer().getAnnotationBehaviorMap().get(Pit.class);
		if (pitBehavior!=null){
			pitBehavior.fillPit(bean.getObject());
		}
		return bean;
	}

	/**
	 * Get message the name is firstLowercase str of class name or create it.
	 * @param C the class of message
	 * @return
	 */
	Bean getOrCreateBean(Class<?> C){
		return getOrCreateBean(C, StringUtil.firstLowercase(C.getSimpleName()));
	}

	/**
	 * Test whether or not message is existed for specific name.
	 * Apply to message that type is equal to CreateType.SINGLETON
	 * @param name the class of message
	 * @return
	 */
	Bean existBean(String name){
		for (Bean bean : beans) {
			if (bean.getName().equals(name)){
				return bean;
			}
		}
		return null;
	}

	/**
	 * Test whether or not message is existed that it`s name is class name firstLowercase
	 * @param C
	 * @return
	 */
	Bean existBean(Class<?> C){
		for (Bean bean : beans) {
			if (bean.getName().equals(StringUtil.firstLowercase(C.getSimpleName()))){
				return bean;
			}
		}
		return null;
	}

	/**
	 * Test whether or not message is existed for specific name.
	 * Through package name of the class that hold the message for name
	 * Apply to message that type is equal to CreateType.MANY_CASE
	 * @param name message name
	 * @param C the class include the message
	 * @return
	 */
	public Bean existBean(Class<?> C, String name){
		for (Bean bean : beans) {
			if (bean.getName().equals(name)&&C.getName().equals(bean.getPath())){
					return bean;
			}
		}
		return null;
	}

	public Set<Bean> getBeans(){
		return beans;
	}
}
