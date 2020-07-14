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
public class VolatileReorderSample {
	
	private static int x = 0, y = 0;
	private static int a = 0, b = 0;
	
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
				a = 1; //step1
				x = b; //step2
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
			 * 但实际执行会出现0 0的情况
			 *
			 * 这是因为cpu或者jit对我们的代码进行了指令重排
			 * 因为step 1 2没有依赖关系, 所以实际执行顺序可能是 step 2 1
			 * step 3 4 同理
			 * 出现 0 0 的情况执行顺序可能是这样 step 2 4 1 3
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
