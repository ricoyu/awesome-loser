package com.loserico.jvm.deadlock;

import java.io.IOException;

public class DeadLockTest {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		System.in.read();
		System.out.println("死锁了, 老司机走起!");
		AModel am = new AModel("ss11", "ss22");
		
		for (int i = 0; i < 20; i++) {
			new Thread(new MyThread2(am), "线程2"+i).start();
		}
	}

	static class AModel {
		public String s1;
		public String s2;

		public AModel(String s1, String s2) {
			this.s1 = s1;
			this.s2 = s2;
		}
	}

	static class MyThread1 implements Runnable {

		private AModel am = null;

		public MyThread1(AModel am) {
			this.am = am;
		}

		@Override
		public void run() {
			synchronized (am.s1) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Now thread1, begin to get sss222");
				synchronized (am.s2) {
					System.out.println("Thread id == " + Thread.currentThread().getId() + ", s1=="+ am.s1+" , s2=="+am.s2);
				}
			}
		}

	}
	
	static class MyThread2 implements Runnable {
		
		private AModel am = null;
		
		public MyThread2(AModel am) {
			this.am = am;
		}
		
		@Override
		public void run() {
			synchronized (am.s2) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Now thread2, begin to get sss111");
				synchronized (am.s1) {
					System.out.println("Thread id == " + Thread.currentThread().getId() + ", s1=="+ am.s1+" , s2=="+am.s2);
				}
			}
		}
		
	}
}
