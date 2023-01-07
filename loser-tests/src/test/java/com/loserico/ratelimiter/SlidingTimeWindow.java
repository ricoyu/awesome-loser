package com.loserico.ratelimiter;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Random;

import static java.util.concurrent.TimeUnit.*;

/**
 * 滑动时间窗口限流实现
 * 假设某个服务最多只能每秒钟处理100个请求, 我们可以设置一个一秒钟的滑动时间窗口, 
 * 窗口中有10个格子, 每个格子100毫秒, 每100毫秒移动一次, 每次移动都要记录当前服务请求的次数
 * <p>
 * Copyright: (C), 2022-08-23 17:00
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SlidingTimeWindow {
	
	//服务访问次数，可以放在Redis中，实现分布式系统的访问计数
	private long counter = 0L;
	
	private LinkedList<Long> slots = new LinkedList<>();
	
	public static void main(String[] args) throws InterruptedException {
		SlidingTimeWindow timeWindow = new SlidingTimeWindow();
		new Thread(() -> {
			try {
				timeWindow.doCheck();
			} catch (InterruptedException e) {
				log.error("", e);
			}
		}).start();
		
		//模拟外部请求不断进来
		while (true) {
			//TODO 判断限流标记
			timeWindow.counter++;
			MILLISECONDS.sleep(new Random().nextInt(15));
		}
	}
	
	private void doCheck() throws InterruptedException {
		while (true) {
			MILLISECONDS.sleep(100);
			slots.addLast(counter);
			if (slots.size() > 10) {
				slots.removeFirst();
			}
			//比较最后一个和第一个，两者相差100以上就限流
			if (slots.peekLast()- slots.peekFirst() >100) {
				System.out.println("限流了 ....");
				//TODO 修改限流标记为true
			} else {
				//TODO 修改限流标记为false
			}
		}
	}
}
