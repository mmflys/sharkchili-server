package com.shark.server.frame.task;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: SuLiang
 * @Date: 2018/9/4 0004
 * @Description:
 */
public class TimerTaskTest {

	public static void main(String[] args) {
		Timer timer=new Timer();
		MyTimerTask timerTask=new MyTimerTask();
		timer.schedule(timerTask,5000,1000);
	}

	static class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			System.out.println("I am a timer");
		}
	}
}
