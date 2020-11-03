package com.loserico.concurrent.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * Copyright: (C), 2020-09-30 11:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ControlSubThread implements Runnable {
	
	private Thread worker;
	private int interval;
	private AtomicBoolean running = new AtomicBoolean(false);
	private AtomicBoolean stopped = new AtomicBoolean(true);
	
	public static void main(String[] args) {
		ControlSubThread controlSubThread = new ControlSubThread(1000);
		controlSubThread.start();
	}
	
	public ControlSubThread(int sleepInterval) {
		this.interval = sleepInterval;
	}
	
	public void start() {
		worker = new Thread(this);
		worker.start();
	}
	
	public void stop() {
		running.set(false);
	}
	
	public void interrupt() {
		running.set(false);
		worker.interrupt();
	}
	
	public boolean isRunning() {
		return running.get();
	}
	
	public boolean isStopped() {
		return stopped.get();
	}
	
	@Override
	public void run() {
		running.set(true);
		stopped.set(false);
		
		while (running.get()) {
			try {
				TimeUnit.MILLISECONDS.sleep(interval);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Thread was interrupted, Failed to complete operation");
			}
		}
		
		stopped.set(true);
	}
}
