package com.shark.server.frame.task;


import com.shark.util.util.FileUtil;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

/**
 * @Author: Su Liang
 * @Date: 2018/9/5 0005
 * @Description:
 */
public class QuartzTest {

	public static void main(String[] args) throws ParseException, SchedulerException {
		Properties properties= FileUtil.readProperties("sharkQuartz.properties");

		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(properties);
		Scheduler scheduler = factory.getScheduler();

		try {
			properties.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME,"myScheduler");
			factory.initialize(properties);
			Scheduler scheduler1 = factory.getScheduler();

			scheduler.start();
			//创建一个JobDetail,指明name,groupname,以及具体的job类名
			JobDetail jobDetail = new JobDetailImpl("myJob", "myJobGroup", MyJob.class);
			jobDetail.getJobDataMap().put("type", "FULL");
			//创建一个每周触发的Trigger,指明星期几几点执行
			Trigger trigger=new CronTriggerImpl("corn","cornTrigger","5/2 * * * * ?");
			//scheduler.scheduleJob(jobDetail,trigger);
			//TriggerBuilder
			Trigger trigger1=TriggerBuilder.newTrigger()
					.withIdentity(TriggerKey.triggerKey("myTrigger","myTriggerGroup"))
					.withSchedule(SimpleScheduleBuilder.simpleSchedule()
							.withIntervalInSeconds(1)
							.repeatForever())
					.startAt(new Date())
					.build();
			scheduler.scheduleJob(jobDetail,trigger1);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public static class MyJob implements Job {
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			System.out.println("Generating repot - " + context.getJobDetail().getDescription());
			System.out.println(new Date().toString());
		}
	}
}
