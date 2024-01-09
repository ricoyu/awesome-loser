package com.loserico.concurrent.blockingQueue;

import java.util.concurrent.PriorityBlockingQueue;

class Task implements Comparable<Task> {
	private int priority;
	private String name;
	
	public Task(int priority, String name) {
		this.priority = priority;
		this.name = name;
	}
	
	public int getPriority() {
		return priority;
	}
	
	@Override
	public int compareTo(Task other) {
		return Integer.compare(this.priority, other.getPriority());
	}
	
	@Override
	public String toString() {
		return "Task{" +
				"priority=" + priority +
				", name='" + name + '\'' +
				'}';
	}
}

public class PriorityBlockingQueueExample {
	
	public static void main(String[] args) throws InterruptedException {
		PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
		
		// 添加任务到队列
		queue.put(new Task(5, "LowPriority"));
		queue.put(new Task(1, "HighPriority"));
		queue.put(new Task(3, "MediumPriority"));
		
		// 从队列中取出任务
		while (!queue.isEmpty()) {
			Task task = queue.take(); // 会按照优先级取出
			System.out.println("Processing task: " + task);
		}
	}
}
