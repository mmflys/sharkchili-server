package com.shark.server.frame.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: SuLiang
 * @Date: 2018/9/4 0004
 * @Description:
 */
public class ScheduledExecutorTest {

	public static void main(String[] args) {
		ScheduledExecutorService service= Executors.newScheduledThreadPool(10);
		service.scheduleAtFixedRate(new MyScheduledExecutor("job1"),1,1, TimeUnit.SECONDS);
		service.scheduleAtFixedRate(new MyScheduledExecutor("job2"),2,2, TimeUnit.SECONDS);
		service.scheduleWithFixedDelay(new MyScheduledExecutor("job3"),2,2, TimeUnit.SECONDS);
	}

	static class MyScheduledExecutor implements Runnable{

		private String jobName;

		public MyScheduledExecutor(String jobName) {
			this.jobName = jobName;
		}

		@Override
		public void run() {
			System.out.println("execute "+jobName);
		}
	}
}
