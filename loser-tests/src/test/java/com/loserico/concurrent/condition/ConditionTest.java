package com.loserico.concurrent.condition;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://examples.javacodegeeks.com/core-java/util/concurrent/locks-concurrent/condition/java-util-concurrent-locks-condition-example/
 * <p>
 * Copyright: (C), 2021-07-30 13:56
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ConditionTest {
	
	/**
	 * A Condition object, also known as condition variable, provides a thread with the ability to suspend its execution, until the condition is true.
	 *
	 * Condition object is necessarily bound to a Lock and can be obtained using the newCondition() method.
	 */
	@Test
	public void testCondition() {
		
	}
	
	static class SharedFiFoQueue {
		private Object[] elems = null;
		private int current = 0;
		private int placeIndex = 0;
		private int removeIndex = 0;
		
		private final Lock lock = new ReentrantLock();
		private final Condition isEmpty = lock.newCondition();
	}
}
