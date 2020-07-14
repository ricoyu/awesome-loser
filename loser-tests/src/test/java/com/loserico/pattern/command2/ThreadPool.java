package com.loserico.pattern.command2;

import static java.util.concurrent.TimeUnit.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The below is the ThreadPool class used to create pool of threads and allow a
 * thread to fetch and execute the job from the job queue.
 * 
 * The above class is used to create n threads (worker threads). Each worker thread
 * will wait for a job in a queue and then execute the job and will go back to
 * waiting state. The class contains a job queue; when a new job will be added into
 * the queue, a worker thread from the pool will execute the job.
 * 
 * We also include a shutdownPool method which will used to shut down the pool by
 * interrupting all the worker threads only when the job queue is empty. The addJob
 * method is used to add jobs to the queues.
 * 
 * @author Loser
 * @since Aug 24, 2016
 * @version
 *
 */
public class ThreadPool {

	private final BlockingQueue<Job> jobQueue;
	private final Thread[] jobThreads;
	private volatile boolean shutdown;

	public ThreadPool(int n) {
		jobQueue = new LinkedBlockingQueue<>();
		jobThreads = new Thread[n];

		for (int i = 0; i < n; i++) {
			jobThreads[i] = new Worker("Pool Thread " + i);
			jobThreads[i].start();
		}
	}

	public void addJob(Job r) {
		try {
			jobQueue.put(r);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void shutdownPool() {
		while (!jobQueue.isEmpty()) {
			try {
				SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		shutdown = true;
		for (Thread workerThread : jobThreads) {
			workerThread.interrupt();
		}
	}

	private class Worker extends Thread {
		public Worker(String name) {
			super(name);
		}

		public void run() {
			while (!shutdown) {
				try {
					Job r = jobQueue.take();
					r.run();
				} catch (InterruptedException e) {
				}
			}
		}
	}

}