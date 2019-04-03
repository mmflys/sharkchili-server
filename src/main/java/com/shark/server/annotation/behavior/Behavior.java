package com.shark.server.annotation.behavior;

/**
 * @Author: SuLiang
 * @Date: 2018/8/29 0029
 * @Description: BehaviorMapAnnotation of annotation included reinforce.
 */
public interface Behavior extends Reinforce {

	/**
	 * 当类加载时
	 * @param containerClass
	 */
	public void whenClassLoad(Class<?> containerClass);

}
