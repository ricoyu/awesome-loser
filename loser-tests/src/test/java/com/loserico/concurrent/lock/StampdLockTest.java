package com.loserico.concurrent.lock;

import java.util.concurrent.locks.StampedLock;

/**
 * 计算坐标点Point对象，包含点移动方法move及计算此点到原点的距离的方法distanceFromOrigin。
 * 在方法distanceFromOrigin中，首先，通过tryOptimisticRead方法获取乐观读标记；然后从主内存中加载点的坐标值 (x,y)；
 * 而后通过StampedLock的validate方法校验锁状态，判断坐标点(x,y)从主内存加载到线程工作内存过程中，主内存的值是否已被其他线程通过move方法修改，
 * 如果validate返回值为true，证明(x, y)的值未被修改，可参与后续计算；否则，需加悲观读锁，再次从主内存加载(x,y)的最新值，然后再进行距离计算。
 * 
 * 其中，校验锁状态这步操作至关重要，需要判断锁状态是否发生改变，从而判断之前copy到线程工作内存中的值是否与主内存的值存在不一致。
 * <p>
 * Copyright: (C), 2020/3/9 13:41
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class StampdLockTest {
	
	public static class Point {
		private double x, y;
		
		private final StampedLock stampedLock = new StampedLock();
		
		public void move(double deltaX, double deltaY) {
			long stamp = stampedLock.writeLock(); //使用写锁, 独占操作
			try {
				x += deltaX;
				y += deltaY;
			} finally {
				stampedLock.unlock(stamp);
			}
		}
		
		public double distanceFromOrigin() {
			long stamp = stampedLock.tryOptimisticRead(); //1 获取乐观读锁
			double currentX = x, currentY = y;            //2 copy变量到工作内存
			
			if (!stampedLock.validate(stamp)) {           //3 检验锁状态, 判断是否数据不一致, 即上次拿stamp后锁没有被别的线程获取过, 是的话返回true
				stamp = stampedLock.readLock();           //4 表示stamp已经不一致了, 获取悲观读锁
				try {
					currentX = x;                         //5 copy变量到工作内存
					currentY = y;
				} finally {
					stampedLock.unlock(stamp);            //6 释放悲观读锁
				}
			}
			
			return Math.sqrt(currentX * currentX + currentY * currentY);  //7 线程本地计算
		}
	}
}
