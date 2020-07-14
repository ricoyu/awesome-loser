package com.loserico.jvm;

import java.util.ArrayList;
import java.util.List;

public class GCMonitorTest {

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000L);

		List<A> list = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			list.add(new A());

			if (i % 20 == 0) {
				long milis = 100L;
				Thread.sleep(milis);
			}
		}
		
		System.gc();
		System.out.println("Over=======================");
		Thread.sleep(100000L);
	}

}

class A {
	private byte[] bs = new byte[10 * 1024];
}
