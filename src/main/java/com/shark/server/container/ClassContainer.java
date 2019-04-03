package com.shark.server.container;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.shark.container.Container;
import com.shark.server.annotation.BehaviorMapAnnotation;
import com.shark.server.annotation.behavior.Behavior;
import com.shark.server.rpc.RemoteService;
import com.shark.server.xml.XmlParser;
import com.shark.util.classes.ClassScannerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;

/**
 * @Author: SuLiang
 * @Date: 2018/8/29 0029
 * @Description:
 */
public class ClassContainer implements Container {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassContainer.class);
	/**所有的注解对象*/
	private Map<Class<?>, Behavior> annotationBehaviorMap;
	/**应用程序所有类*/
	private Set<Class<?>> applicationClass;
	/**远程调用方法*/
	private Table<Class<?>, String,Map<Class[],Method>> remoteMethod;

	ClassContainer() {
		annotationBehaviorMap = new HashMap<>();
		applicationClass = new LinkedHashSet<>();
		remoteMethod= HashBasedTable.create();
	}

	@Override
	public Container init() {
		LOGGER.info(ClassContainer.class.getName()+" init");
		scanXmlConfig();
		scanClassWithSystemAnnotation();
		scanRemote();
		scanApplicationClass();
		//程序结束时调用钩子线程
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
		return this;
	}

	/**
	 * scan remote class
	 */
	private void scanRemote(){
		Predicate<Class<?>> isRemoteClass= c->RemoteService.class.isAssignableFrom(c)&&c!=RemoteService.class&&!Modifier.isAbstract(c.getModifiers());
		Set<Class<?>> remoteClass= ClassScannerUtil.scanClassWithPredicate("com.shark",isRemoteClass);
		for (Class<?> aClass : remoteClass) {
			// 注册代理类
			MainContainer.get().getBeanContainer().registerBean(aClass);
			for (Method method : aClass.getMethods()) {
				Map<Class[],Method> methods=remoteMethod.get(aClass,method.getName());
				if (methods==null){
					methods= Maps.newHashMap();
					remoteMethod.put(aClass,method.getName(),methods);
				}
				methods.put(method.getParameterTypes(),method);
			}
			LOGGER.info("Register remote class method: {}",aClass.getSimpleName());
		}
	}

	/**
	 * scan system class
	 */
	private void scanSystemClass() {
		//scan package: util
		//scanPackageClassAndDirectCreateBean("util");
	}

	/**
	 * scan SharkConfig.xml
	 */
	private void scanXmlConfig(){
		scanXmlBeans();
	}

	/**
	 * Scan beans from SharkConfig.xml
	 */
	private void scanXmlBeans() {
		XmlParser xmlParser= MainContainer.get().getOrCreateBean(XmlParser.class);
		xmlParser.readDoc().parseAndCreateBean();
	}

	/**
	 * scan application class and create message
	 */
	private void scanApplicationClass() {
		scanPackageClassWithBehavior("com.shark");
	}

	/**
	 * scan package class that marked @Pit and create message
	 *
	 * @param packageName
	 */
	private void scanPackageClassWithBehavior(String packageName) {
		applicationClass = ClassScannerUtil.scanClass(packageName);
		//执行注解行为类初始动作
		for (Class<?> aClass : applicationClass) {
			//内部类不创建bean
			if (filterClass(aClass)) {
				for (Behavior annotationBehavior : annotationBehaviorMap.values()) {
					annotationBehavior.whenClassLoad(aClass);
				}
			}
		}
	}

	/**
	 * scan package class and create message no matter what @Pit marked.
	 *
	 * @param packageName
	 */
	private void scanPackageClassAndDirectCreateBean(String packageName) {
		applicationClass = ClassScannerUtil.scanClass(packageName);
		//执行注解行为类初始动作
		for (Class<?> aClass : applicationClass) {
			if (filterClass(aClass)) {
				MainContainer.get().getBeanContainer().registerBean(aClass);
			}
		}
	}

	/**
	 * Scan system annotation
	 * Must to be executed
	 */
	private void scanClassWithSystemAnnotation() {
		//扫描注解类
		Set<Class<?>> allClasses = ClassScannerUtil.scanClass("com.shark.server.annotation.behavior");
		//创建注解类bean
		for (Class<?> aClass : allClasses) {
			if (!aClass.isInterface()) {
				BehaviorMapAnnotation behavior = aClass.getAnnotation(BehaviorMapAnnotation.class);
				if (behavior != null) {
					Behavior annotationBehavior = (Behavior) MainContainer.get().getBeanContainer().registerBean(aClass).getProxy();
					if (behavior.annotation() == Annotation.class) {
						annotationBehaviorMap.put(behavior.value(), annotationBehavior);
					} else {
						annotationBehaviorMap.put(behavior.annotation(), annotationBehavior);
					}
				}
			}
		}
	}

	/**
	 * 内部类不创建bean,接口,抽象类也不创建bean
	 * @param aClass
	 * @return
	 */
	private boolean filterClass(Class<?> aClass) {
		return !aClass.getSimpleName().equals("") && !aClass.isInterface() && !Modifier.isAbstract(aClass.getModifiers());
	}

	/**
	 * Get remote class method
	 * @param C
	 * @param method
	 * @return
	 */
	Method getRemoteMethod(Class<?> C, String method, Class<?>... parameters){
		Map<Class[],Method> map=remoteMethod.get(C,method);
		for (Class[] key : map.keySet()) {
			if (Arrays.equals(key,parameters))
				return map.get(key);
		}
		return null;
	}

	public Map<Class<?>, Behavior> getAnnotationBehaviorMap() {
		return annotationBehaviorMap;
	}

	@Override
	public Container start() {
		LOGGER.info(ClassContainer.class.getName()+" start");
		return this;
	}

	@Override
	public Container stop() {
		LOGGER.info(ClassContainer.class.getName()+" stop");
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
}
