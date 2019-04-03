package com.shark.server.annotation.behavior;

import com.shark.server.annotation.BehaviorMapAnnotation;
import com.shark.server.annotation.Pit;
import com.shark.server.annotation.consts.CreateType;
import com.shark.server.bean.Bean;
import com.shark.server.container.MainContainer;
import com.shark.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Author: SuLiang
 * @Date: 2018/8/29 0029
 * @Description:
 */
@BehaviorMapAnnotation(Pit.class)
public class PitBehavior extends AbstractBehavior {

	private static final Logger LOGGER = LoggerFactory.getLogger(PitBehavior.class);

	@Override
	public void whenClassLoad(Class<?> C) {
		//@Pit 根据字段注解创建bean对象
		for (Field field : C.getDeclaredFields()) {
			Pit pit = field.getAnnotation(Pit.class);
			if (pit != null) {
				String beanName;
				//为字段创建bean对象,在所在容器对象创建时为该变量注入bean
				String initName = pit.name().equals("") ? (pit.value().equals("") ? null : pit.value()) : pit.name();
				if (initName == null || initName.equals("")) {
					beanName = field.getName();
				} else {
					beanName = initName;
				}
				//单例模式下只创建一个
				boolean createBean=false;
				if (pit.type()== CreateType.SINGLETON){
					Bean bean= MainContainer.get().getBean(beanName);
					if (bean==null){
						createBean=true;
					}
				}else if (pit.type()== CreateType.MANY_CASE){
					createBean=true;
				}else if (pit.type()== CreateType.THREAD_SAFETY){

				}
				if (createBean){
					Bean bean = MainContainer.get().getBeanContainer().registerBean(field.getType(), beanName);
					//创建bean后设置bean路径
					String path=C.getName();
					bean.setPath(path);
				}
			}
		}
	}

	/**
	 * 当对象创建时增强->注入依赖
	 *
	 * @param object
	 * @param method
	 */
	@Override
	public void preReinforce(Object object, Method method) {
		//创建对象时,注入依赖
		if (method.getName().equals(object.getClass().getSimpleName())) {
			fillPit(object);
		}
	}

	/**
	 * 递归注入依赖
	 *
	 * @param object
	 */
	public void fillPit(Object object) {
		//检查有无需要注入的字段
		Set<Object> filledFiled = new LinkedHashSet<>();
		for (Field field : object.getClass().getDeclaredFields()) {
			try {
				//解除封装
				field.setAccessible(true);
				// 字段有@Pit注解,且还未初始化
				if (field.getAnnotation(Pit.class) != null) {
					Object bean;
					Pit pit = field.getAnnotation(Pit.class);
					//依次检查 注解bean name --> 字段名 --> 字段所属类首字母小写name,若都不存在则创建这个bean
					//注解中定义的bean name
					String initName = pit.name().equals("") ? (pit.value().equals("") ? null : pit.value()) : pit.name();
					bean = MainContainer.get().getBeanContainer().existBean(object.getClass(),initName);
					if (bean == null) {
						//字段名
						String filedName = field.getName();
						bean = MainContainer.get().getBeanContainer().existBean(object.getClass(),filedName);
						if (bean == null) {
							//字段类的首字母小写名
							String filedClassLowercaseName = StringUtil.firstLowercase(field.getType().getSimpleName());
							bean = MainContainer.get().getBeanContainer().existBean(object.getClass(),filedClassLowercaseName);
							if (bean == null) {
								bean = MainContainer.get().getOrCreateBean(field.getType(), filedClassLowercaseName);
							}
						}
					}
					//设置字段值
					field.set(object, ((Bean) bean).getProxy());
					filledFiled.add(((Bean) bean).getObject());
					LOGGER.debug("Object {} fill the pit message {}", object, ((Bean) bean).getProxy());
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		//递归注入字段的字段
		for (Object fieldObj : filledFiled) {
			fillPit(fieldObj);
		}
	}

	@Override
	public boolean isOpened(Object object, Method method) {
		//只增强构造方法
		return method.getName().equals(object.getClass().getSimpleName());
	}
}
