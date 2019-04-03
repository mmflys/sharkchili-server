package com.shark.server.xml;

import com.shark.server.bean.Bean;
import com.shark.server.container.MainContainer;
import com.shark.util.classes.ClassUtil;
import com.shark.util.classes.classloder.ClassLoaderFactory;
import com.shark.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Bean type
 */
enum BeanType {

	NORMAL {
		@Override
		public void createBean(List<BeanCreate> beanCreates, BeanCreate beanCreate) throws ClassNotFoundException {
			Object proxy = newInstance(beanCreate.getClassPath(), beanCreate.getBeanName());
			beanCreate.setObject(proxy);
			// 设置属性
			if (!beanCreate.getFileds().isEmpty()) {
				beanCreate.getFileds().entrySet().stream().filter(entry->!StringUtil.isEmpty(entry.getValue())).forEach(stringStringEntry ->{
					// 判断是否是ref值
					if (stringStringEntry.getValue().charAt(0)== SharkXmlNode.SYMBOL_REF.getName().charAt(0)){
						setRefField(beanCreates, proxy,stringStringEntry);
					}else {
						setFields(proxy, stringStringEntry);
					}
				});
			}
		}
	},
	COLLECTION {
		@Override
		public void createBean(List<BeanCreate> beanCreates, BeanCreate beanCreate) throws ClassNotFoundException {
			Collection proxy = (Collection) newInstance(beanCreate.getClassPath(), beanCreate.getBeanName());
			beanCreate.setObject(proxy);
			if (!beanCreate.getElement().isEmpty()){
				beanCreate.getElement().stream().filter(s->!StringUtil.isEmpty(s)).forEach(s -> {
					if (s.charAt(0)== SharkXmlNode.SYMBOL_REF.getName().charAt(0)){
						addRefElement(beanCreates,proxy,s,beanCreate.getElementClassPath());
					}else {
						addElement(proxy,s,beanCreate.getElementClassPath());
					}
				});
			}
		}
	},
	MAP {};

	public static Object stringToBaseType(String fieldValue, Class fieldClass) {
		Object objValue = null;
		if (fieldClass == String.class) {
			objValue = fieldValue;
		} else {
			try {
				// 用包装类的带参数构造函数创建一个包装类对象
				Constructor constructor = null;
				try {
					constructor = fieldClass.getConstructor(String.class);
				} catch (NoSuchMethodException e) {
					constructor = ClassUtil.getWrapperType(fieldClass).getConstructor(String.class);
				}
				objValue = constructor.newInstance("1");
				// 基本数据类型的包装类都含有valueOf方法,把字符串转成自己类型的对象
				objValue = fieldClass.getMethod("valueOf", String.class).invoke(objValue, fieldValue);
				return objValue;
			} catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return objValue;
	}

	/**
	 * Get field recursively
	 *
	 * @param c
	 * @param fieldName
	 * @return
	 */
	public static Field recursiveGetField(Class c, String fieldName) {
		Field field;
		try {
			field = c.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class superClass = c.getSuperclass();
			if (superClass == Object.class) {
				return null;
			} else {
				return recursiveGetField(superClass, fieldName);
			}
		}
		return field;
	}

	/**
	 * Get method recursively
	 *
	 * @param setMethodName
	 * @param type
	 * @return
	 */
	public static Method recursiveGetMethod(String setMethodName, Class<?> type, Class<?>... parameterTypes) {
		Method method = null;
		try {
			method = type.getMethod(setMethodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			Class superClass = type.getSuperclass();
			return recursiveGetMethod(setMethodName, superClass, parameterTypes);
		} catch (NullPointerException e) {
			System.out.println(setMethodName);
			System.out.println(type);
		}
		return method;
	}

	public void createBean(List<BeanCreate> beanCreates, BeanCreate beanCreate) throws ClassNotFoundException{

	}

	/**
	 * new instance of the SqlType
	 * @param classPath
	 * @param beanName
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static Object newInstance(String classPath, String beanName) throws ClassNotFoundException {
		Class c = ClassLoaderFactory.getClassLoader().loadClass(classPath);
		Bean bean = MainContainer.get().getBeanContainer().registerBean(c, beanName);
		return bean.getProxy();
	}

	/**
	 * set field object to object
	 * @param object
	 * @param field
	 */
	private static void setFields(Object object, Map.Entry<String, String> field) {
		String fieldName = field.getKey();
		String fieldValue = field.getValue();

		Field reflectField = recursiveGetField(object.getClass(), fieldName);
		if (reflectField == null) {
			LOGGER().error("Field {} not exist in class {}", fieldName, object.getClass());
		} else {
			//获取基本数据类型的包装类型,非基本数据类型返回原类型
			Class fieldType = ClassUtil.getWrapperType(reflectField.getType());
			boolean isBaseType = fieldType != reflectField.getType();
			Object fieldObjectValue = stringToBaseType(fieldValue, fieldType);
			// 反射调用set方法
			String setMethodName = "set" + StringUtil.firstUppercase(fieldName);
			// 还原字段类型
			fieldType = isBaseType ? reflectField.getType() : fieldType;
			//有可能是父类方法?
			Method method = recursiveGetMethod(setMethodName, object.getClass(), fieldType);
			invokeMethod(object, fieldObjectValue, setMethodName, method);
		}
	}

	/**
	 * set ref field object to object
	 * @param beanCreated
	 * @param object
	 * @param field
	 */
	public static void setRefField(List<BeanCreate> beanCreated, Object object, Map.Entry<String, String> field) {
		try {
			String fieldName = field.getKey();
			//去除第一个字符"@"
			String fieldValue = field.getValue().substring(1);
			Field reflectField = object.getClass().getDeclaredField(fieldName);
			if (reflectField == null) {
				LOGGER().error("Field {} not exist in class {}", fieldName, object.getClass());
			} else {
				BeanCreate refBean = getByBeanName(beanCreated, fieldValue);
				if (refBean == null) {
					LOGGER().error("No bean map to ref {}", fieldValue);
				} else {
					Object fieldObjectValue = refBean.getObject();
					String setMethodName="set"+ StringUtil.firstUppercase(fieldName);
					Method method = recursiveGetMethod(setMethodName, object.getClass(), fieldObjectValue.getClass());
					invokeMethod(object, fieldObjectValue, setMethodName, method);
				}
			}
		} catch (NoSuchFieldException e) {
			LOGGER().error("class {} has no such field {}",object.getClass(),field.getKey());
		}
	}

	/**
	 * Add element which is base type to collection
	 * @param collection
	 * @param value
	 */
	public static void addElement(Collection collection,String value,String elementClassPath){
		try {
			Class c = ClassLoaderFactory.getClassLoader().loadClass(elementClassPath);
			if (c==null){
				LOGGER().error("Element class path {} is error",elementClassPath);
			}else {
				Object objectValue= stringToBaseType(value,c);
				collection.add(objectValue);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add ref element which is base type to collection
	 * @param beanCreates
	 * @param collection
	 * @param value
	 * @param elementClassPath
	 */
	public static void addRefElement(List<BeanCreate> beanCreates, Collection collection, String value, String elementClassPath){
		try {
			Class c = ClassLoaderFactory.getClassLoader().loadClass(elementClassPath);
			if (c==null){
				LOGGER().error("Element class path {} is error",elementClassPath);
			}else {
				collection.add(getByBeanName(beanCreates,value).getObject());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void invokeMethod(Object object, Object fieldObjectValue, String setMethodName, Method method) {
		if (method == null) {
			LOGGER().error("Get method null, class {}, method {}, parameters {}", object.getClass().getSimpleName(), setMethodName, fieldObjectValue.getClass());
		} else {
			try {
				method.invoke(object, fieldObjectValue);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}


	private static BeanCreate getByBeanName(List<BeanCreate> beanCreate, String name) {
		for (BeanCreate create : beanCreate) {
			if (create.getObject() != null && create.getBeanName().equals(name.substring(1))) return create;
		}
		return null;
	}


	private static Logger LOGGER() {
		return LoggerFactory.getLogger(BeanType.class);
	}
}
