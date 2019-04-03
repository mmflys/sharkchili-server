package com.shark.server.frame.task.stop;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @Author: SuLiang
 * @Date: 2018/9/6 0006
 * @Description:
 */
public class Schedulers {

	private volatile Scheduler scheduler;

	private Schedulers() {
		init();
	}

	private void init(){
		SchedulerFactory schedulerFactory=new StdSchedulerFactory();
		try {
			scheduler=schedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public static Schedulers get(){
		return InstanceHolder.schedulers;
	}

	private static class InstanceHolder{
		private static Schedulers schedulers=new Schedulers();
	}
}
