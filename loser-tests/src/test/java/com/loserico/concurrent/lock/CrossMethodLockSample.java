package com.loserico.concurrent.lock;

/**
 * <p>
 * Copyright: (C), 2019/11/17 11:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CrossMethodLockSample {
	
	private Object object = new Object();
	
	public String decStockLock() {
		//在 decStockLock方法加锁
		//UnsafeInstanceTest.reflectGetUnsafe().monitorEnter(object);
		System.out.println("扣减库存等操作");
		return "下单从成功";
	}
	
	public String test() {
		//test方法里面解锁
		//UnsafeInstanceTest.reflectGetUnsafe().monitorExit(object);
		return null;
	}
}
