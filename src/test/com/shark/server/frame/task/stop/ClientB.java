package com.shark.server.frame.task.stop;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * @Author: SuLiang
 * @Date: 2018/9/6 0006
 * @Description:
 */
public class ClientB {
	public static void main(String[] args) throws SchedulerException {
		Scheduler scheduler=Schedulers.get().getScheduler();
		scheduler.start();
		System.out.println(scheduler.getTriggersOfJob(ClientA.JOB_KEY));
		//scheduler.pauseTrigger(ClientA.TRIGGER_KEY);
	}
}
