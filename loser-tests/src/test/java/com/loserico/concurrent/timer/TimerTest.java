package com.loserico.concurrent.timer;

import com.loserico.common.lang.utils.DateUtils;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020/3/28 16:22
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TimerTest {
	
	@SneakyThrows
	@After
	public void join() {
		Thread.currentThread().join();
	}
	
	/**
	 * 延迟两秒执行, 第一次执行后每隔3秒执行一次, 一直执行下去
	 */
	@Test
	public void testScheduleWithDelayAndFixedPeriod() {
		Timer timer = new Timer("定时任务");
		/**
		 * delay 是第一次任务延迟就多执行
		 * period 是接下来间隔多久运行一次
		 */
		timer.schedule(new MyTask(), 2000, 3000);
	}
	
	/**
	 * 启动后延迟一秒执行, 第一次执行后每隔一秒执行一次, 10秒后定时任务取消
	 */
	@SneakyThrows
	@Test
	public void testScheduleWithDelayAndFixedPeriodThenCancel() {
		Timer timer = new Timer("定时任务");
		/**
		 * delay 是第一次任务延迟就多执行
		 * period 是接下来间隔多久运行一次
		 */
		timer.schedule(new MyTask(), 1000, 1000);
		
		/**
		 * 一定时间候取消定时任务
		 */
		TimeUnit.SECONDS.sleep(10);
		timer.cancel();
		Thread.currentThread().interrupt();
		System.out.println("定时任务取消");
	}
	
	/**
	 * 启动后延迟一秒执行, 执行一次后退出
	 */
	@Test
	public void testDelayExecuteOnceThenExit() {
		Timer timer = new Timer("定时任务");
		/**
		 * delay是延迟多久执行, 只执行一次
		 */
		timer.schedule(new MyTask(), 1000);
	}
	
	@Test
	public void test() {
		Timer timer = new Timer("定时任务");
		timer.schedule(new MyTask(), DateUtils.secondPlus(new Date(), 3));
	}
	
	static class MyTask extends TimerTask {
		
		private int i =0;
		
		@Override
		public void run() {
			System.out.println("第" + (++i) +"次运行");
		}
	}
}
