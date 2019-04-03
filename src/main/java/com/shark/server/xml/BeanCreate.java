package com.shark.server.xml;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Author: SuLiang
 * @Date: 2018/8/31 0031
 * @Description:
 */
public class BeanCreate {
	private String beanName;
	/**type of bean*/
	private String classPath;
	/**element type of collection,only when the bean is a collection*/
	private String elementClassPath;
	/**bean type*/
	private BeanType beanType;
	/**key->fileName,Object->fieldValue*/
	private Map<String, String> fileds;
	/**element*/
	private List<String> element;
	/**Object created*/
	private Object object;

	public BeanCreate() {
		fileds = Maps.newHashMap();
		element= Lists.newArrayList();
		beanType=BeanType.NORMAL;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public Map<String, String> getFileds() {
		return fileds;
	}

	public void setFileds(Map<String, String> fileds) {
		this.fileds = fileds;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public List<String> getElement() {
		return element;
	}

	public void setElement(List<String> element) {
		this.element = element;
	}

	public BeanType getBeanType() {
		return beanType;
	}

	public void setBeanType(BeanType beanType) {
		this.beanType = beanType;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getElementClassPath() {
		return elementClassPath;
	}

	public void setElementClassPath(String elementClassPath) {
		this.elementClassPath = elementClassPath;
	}

	@Override
	public String toString() {
		return "BeanCreate{" +
				"beanName='" + beanName + '\'' +
				", classPath='" + classPath + '\'' +
				", elementClassPath='" + elementClassPath + '\'' +
				", beanType=" + beanType +
				", fileds=" + fileds +
				", element=" + element +
				", object=" + object +
				'}';
	}
}
