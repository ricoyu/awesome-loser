package com.loserico.jvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HotThreadTest {

	public static void main(String[] args) throws IOException {
		System.out.println("1111111111111111");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
		
		System.out.println("22222222222222");
		Thread t1 = new Thread(() -> {
			
			while (true) {
				try {
					if (System.currentTimeMillis() % 100 == 0) {
						Thread.sleep(10L);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t1.setName("Mythread11111111111");
		t1.start();
		
		System.out.println("33333333333333");
		
		//构建一个等待线程
		final Object obj = new Object();
		Thread t2 = new Thread(() -> {
			synchronized (obj) {
				try {
					obj.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t2.setName("MyThread2222222222222222");
		t2.start();
		System.out.println("44444444444444");
		
		reader.readLine();
	}
}
