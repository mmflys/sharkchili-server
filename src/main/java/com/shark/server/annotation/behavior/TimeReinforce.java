package com.shark.server.annotation.behavior;

import com.shark.server.annotation.BehaviorMapAnnotation;
import com.shark.server.annotation.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author: SuLiang
 * @Date: 2018/8/29 0029
 * @Description:
 */
@BehaviorMapAnnotation(Timer.class)
public class TimeReinforce extends AbstractBehavior {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeReinforce.class);

	/**开始时间*/
	private long startTime;
	/**结束时间*/
	private long endTime;

	@Override
	public void preReinforce(Object object,Method method) {
		this.startTime=System.currentTimeMillis();
	}

	@Override
	public void rearReinforce(Object object,Method method) {
		this.endTime=System.currentTimeMillis();
		LOGGER.info("Execute method {},time {}ms",method.getName(),(endTime-startTime));
	}

	@Override
	public boolean isOpened(Object object,Method method) {
		return method.getAnnotation(Timer.class)!=null;
	}
}
