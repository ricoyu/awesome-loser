package com.loserico.jmm;

/**
 * 指令重排
 * <p>
 * Copyright: (C), 2019/11/15 8:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class VolatileReorderSample2 {
	
	private static int x = 0, y = 0;
	private static volatile int a = 0, b = 0;
	
	public static void main(String[] args) throws InterruptedException {
		int i = 0;
		for (; ; ) {
			i++;
			x = 0;
			y = 0;
			a = 0;
			b = 0;
			
			Thread t1 = new Thread(() -> {
				//由于线程one先启动，下面这句话让它等一等线程two. 可根据自己电脑的实际性能适当调整等待时间.
				shortWait(10000);
				a = 1; //step1  这是写 store
				//storeload读写屏障, 不允许volatile写与第二步volatile读发生重排
				x = b; //step2  这是先读后写(先读volatile变量b, 再写普通变量x)
			});
			
			Thread t2 = new Thread(() -> {
				b = 1; //step3
				y = a; //step4
			});
			
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			
			/**
			 * 1: 刚好按 step 1 2 3 4 顺序执行
			 *    t1先执行完 a 1, b 0, x 0, y 1
			 *    t2接着执行 a 1, b 1, x 0, y 1
			 * 2: step 1 3 2 4
			 *    a 1, b 1, x 1, y 1
			 * 3: step 1 3 4 2
			 *    a 1, b 1, x 1, y 1
			 * 4: step 3 1 2 4
			 *    a 1, b 1, x 1, y 1
			 * 5: step 3 4 1 2
			 *    a 1, b 1, x 1, y 0
			 *
			 * 所以 x y 可能得值有:
			 * 0 1
			 * 1 1
			 * 1 0
			 * 
			 * a, b, 加上volatile关键字之后就不会出现指令重排, 所以不会出现 0 0 这种情况了
			 */
			String result = "第" + i + "次 (" + x + "," + y + "）";
			if (x == 0 && y == 0) {
				System.err.println(result);
				break;
			} else {
				System.out.println(result);
			}
		}
	}
	
	public static void shortWait(long interval) {
		long start = System.nanoTime();
		long end;
		do {
			end = System.nanoTime();
		} while (start + interval >= end);
	}
}
