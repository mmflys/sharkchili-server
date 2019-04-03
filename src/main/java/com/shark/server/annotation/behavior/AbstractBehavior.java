package com.shark.server.annotation.behavior;

import java.lang.reflect.Method;

/**
 * @Author: SuLiang
 * @Date: 2018/8/30 0030
 * @Description:
 */
public abstract class AbstractBehavior implements Behavior {

	@Override
	public void whenClassLoad(Class<?> containerClass) {

	}

	@Override
	public void preReinforce(Object object, Method method) {

	}

	@Override
	public void rearReinforce(Object object, Method method) {

	}

	@Override
	public boolean isOpened(Object object,Method method) {
		return true;
	}
}
