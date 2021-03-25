package com.loserico.unsafe;

import com.loserico.concurrent.concurrentLinkedQueue.ConcurrentLinkedQueue;
import lombok.SneakyThrows;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * <p>
 * Copyright: (C), 2020-12-10 15:59
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UnsafeTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		//testGetUnsafe();
		testConcurrentLinkedQueue();
		//testUnsafePutObject();
	}
	
	@SneakyThrows
	public static void testGetUnsafe() {
		System.out.println(UnsafeTest.class.getClassLoader());
		Field field = Unsafe.class.getDeclaredField("theUnsafe");
		field.setAccessible(true);
		Unsafe unsafe = (Unsafe) field.get(null);
		System.out.println(UnsafeTest.class.getClassLoader());
	}
	
	@SneakyThrows
	public static void testConcurrentLinkedQueue() {
		ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue();
		queue.add("first");
		
		System.out.println("first".equals(queue.poll()));
	}
	
	public static void testUnsafePutObject() {
		Unsafe unsafe = Unsafe.getUnsafe();
		Node<String> node = new Node<>("first");
		Object object = unsafe.getObject(node, node.itemOffset);
		System.out.println(object.equals("first"));
	}
	
	private static class Node<E> {
		volatile E item;
		volatile Node<E> next;
		
		// Unsafe mechanics
		
		private static final sun.misc.Unsafe UNSAFE;
		public static final long itemOffset;
		public static final long nextOffset;
		
		/**
		 * Constructs a new node.  Uses relaxed write because item can
		 * only be seen after publication via casNext.
		 */
		Node(E item) {
			UNSAFE.putObject(this, itemOffset, item);
		}
		
		boolean casItem(E cmp, E val) {
			return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
		}
		
		void lazySetNext(Node<E> val) {
			UNSAFE.putOrderedObject(this, nextOffset, val);
		}
		
		boolean casNext(Node<E> cmp, Node<E> val) {
			return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
		}
		
		
		static {
			try {
				UNSAFE = sun.misc.Unsafe.getUnsafe();
				Class<?> k = Node.class;
				itemOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("item"));
				nextOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("next"));
			} catch (Exception e) {
				throw new Error(e);
			}
		}
	}
}
