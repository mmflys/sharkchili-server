package com.shark.server.frame.task.stop;

import org.quartz.*;

/**
 * @Author: SuLiang
 * @Date: 2018/9/6 0006
 * @Description:
 */
public class SpeakTask implements InterruptableJob {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("I want to speak...");
	}


	@Override
	public void interrupt() throws UnableToInterruptJobException {
		System.out.println("I am interrupted...");
	}
}
