package com.shark.server.frame.task.stop;

import org.quartz.*;

import java.util.Date;

/**
 * @Author: SuLiang
 * @Date: 2018/9/6 0006
 * @Description:
 */
public class ClientA {
	public static final TriggerKey TRIGGER_KEY =TriggerKey.triggerKey("myTrigger","myTriggerGroup");
	public static final JobKey JOB_KEY=JobKey.jobKey("a-speak","speak");

	public static void main(String[] args) throws InterruptedException {
		Thread produce=new Thread(() -> {
			try {
				producerDo();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		});
		Thread customer=new Thread(() -> {
			Scheduler scheduler=Schedulers.get().getScheduler();
			try {
				customerDo(scheduler);
			} catch (InterruptedException | SchedulerException e) {
				e.printStackTrace();
			}
		});
		Thread customer1=new Thread(() -> {
			Scheduler scheduler=Schedulers.get().getScheduler();
			try {
				customerDo1(scheduler);
			} catch (InterruptedException | SchedulerException e) {
				e.printStackTrace();
			}
		});
		produce.start();
		customer.start();
		customer1.start();
		produce.join();
		customer.join();
		customer1.join();
	}

	private static void customerDo(Scheduler scheduler) throws InterruptedException, SchedulerException {
		Thread.sleep(3000);
		System.out.println("customer do com.shark.job.task");
		scheduler.pauseJob(JOB_KEY);
	}

	private static void customerDo1(Scheduler scheduler) throws InterruptedException, SchedulerException {
		Thread.sleep(5000);
		System.out.println("another customer do com.shark.job.task");
		scheduler.resumeJob(JOB_KEY);
	}

	private static void producerDo() throws SchedulerException {
		JobDetail jobDetail= JobBuilder.newJob(SpeakTask.class).withIdentity(JOB_KEY).build();
		Trigger trigger= TriggerBuilder.newTrigger()
				.withIdentity(ClientA.TRIGGER_KEY)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(1)
						.repeatForever())
				.startAt(new Date())
				.build();

		Scheduler scheduler= Schedulers.get().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(jobDetail, trigger);
		System.out.println(scheduler.getTriggersOfJob(ClientA.JOB_KEY));
	}

}
