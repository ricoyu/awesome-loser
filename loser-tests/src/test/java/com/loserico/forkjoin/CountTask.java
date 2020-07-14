package com.loserico.forkjoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask<Integer> {
	private static final long serialVersionUID = 6517416379838948983L;
	private static final Logger log = LoggerFactory.getLogger(CountTask.class);
	
	private static final int THRESHOLD = 2;// 阈值
	private int start;
	private int end;

	public CountTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		// 如果任务足够小就计算任务
		boolean canCompute = (end - start) <= THRESHOLD;

		if (canCompute) {
			for (int i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			// 如果任务大于阀值，就分裂成两个子任务计算
			int middle = (start + end) / 2;
			CountTask leftTask = new CountTask(start, middle);
			CountTask rightTask = new CountTask(middle + 1, end);
			// 执行子任务
			leftTask.fork();
			rightTask.fork();
			// 等待子任务执行完，并得到其结果
			int leftResult = (Integer) leftTask.join();
			int rightResult = (Integer) rightTask.join();
			// 合并子任务
			sum = leftResult + rightResult;
		}
		/*if (true) {
			throw new RuntimeException("exception!");
		}*/
		return sum;
	}

	public static void main(String[] args) {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		// 生成一个计算任务，负责计算1+2+3+4
		CountTask task = new CountTask(1, 4);
		// 执行一个任务
		Future<Integer> result = forkJoinPool.submit(task);
		/*if (task.isCompletedAbnormally()) {
			log.debug(task.getException().getMessage());
		}*/
		try {
			System.out.println(result.get());
		} catch (InterruptedException e) {
			System.out.println(e);
		} catch (ExecutionException e) {
			System.out.println(e);
		}
	}

}
