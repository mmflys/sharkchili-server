package com.shark.server.annotation.behavior;

import java.lang.reflect.Method;

/**
 * @Author: SuLiang
 * @Date: 2018/8/29 0029
 * @Description: pre reinforce or rear reinforce
 */
public interface Reinforce {
	public void preReinforce(Object object, Method method);
	public void rearReinforce(Object object, Method method);
	public boolean isOpened(Object object, Method method);
}
