package com.loserico.concurrent.thread;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-11-27 15:21
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DaemonThreadTest {
	
	@Test(expected = IllegalThreadStateException.class)
	public void whenSetDaemonWhileRunning_thenIllegalThreadStateException() {
		Thread daemonThread = new Thread();
		daemonThread.start();
		daemonThread.setDaemon(true);
	}
	
	@Test
	public void whenCallIsDaemon_thenCorrect() {
		Thread daemonThread = new Thread();
		Thread userThread = new Thread();
		daemonThread.setDaemon(true);
		daemonThread.start();
		userThread.start();
		
		Assert.assertTrue(daemonThread.isDaemon());
		Assert.assertFalse(userThread.isDaemon());
	}
}
