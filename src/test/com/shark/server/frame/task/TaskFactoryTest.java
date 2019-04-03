package com.shark.server.frame.task;

import com.shark.job.factory.JobFactory;
import com.shark.job.job.AbstractScheduleJob;
import com.shark.job.job.ScheduleJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.shark.util.util.LogUtil.LOGGER;


/**
 * @Author: SuLiang
 * @Date: 2018/9/27 0027
 * @Description:
 */
public class TaskFactoryTest {

	public static void main(String[] args) throws SchedulerException {
		timingTask();
	}

	public static void timingTask() throws SchedulerException {
		Job job=new AbstractScheduleJob() {
			@Override
			public void execute(JobExecutionContext context) throws JobExecutionException {
				System.out.println(context);
				LOGGER(TaskFactoryTest.class).info("Now: "+new Date());
			}
		};
		long period= TimeUnit.MINUTES.toMillis(1);
		Date start= DateBuilder.dateOf(17,28,0,28,9,2018);
		ScheduleJob scheduleJob= JobFactory.startAtDate(start,period,3,job,"timingTask");
		SchedulerFactory factory=new StdSchedulerFactory();
		Scheduler scheduler=factory.getScheduler();
		scheduler.start();
		scheduler.scheduleJob(scheduleJob.getJobDetail(),scheduleJob.getTrigger());
	}

}
