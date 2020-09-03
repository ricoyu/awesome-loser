package com.loserico.primary;

/**
 * 八种基本类型的包装类和对象池
 * <p>
 * Copyright: Copyright (c) 2020-08-19 16:02
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PrimaryWrapperObjectPool {
	
	public static void main(String[] args) {
		//5种整形的包装类Byte, Short, Integer, Long, Character的对象
		//在值小于127时可以使用对象池
		//这种调用底层实际是执行的Integer.valueOf(127), 里面用到了IntegerCache对象池
		Integer i1 = 127;
		Integer i2 = 127;
		
		//输出true
		System.out.println(i1 == i2);
		
		//值大于127时, 不会从对象池中取对象
		Integer i3 = 128;
		Integer i4 = 128;
		//输出false
		System.out.println(i3 == i4);
		
		//用new关键词新生成对象不会使用对象池
		Integer i5 = new Integer(127);
		Integer i6 = new Integer(127);
		//输出false
		System.out.println(i5 == i6);
		
		//Boolean类也实现了对象池技术
		Boolean bool1 = true;
		Boolean bool2 = true;
		//输出true
		System.out.println(bool1 == bool2);
		
		//浮点类型的包装类没有实现对象池技术
		Double d1 = 1.0;
		Double d2 = 1.0;
		//输出false
		System.out.println(d1 == d2);
	}
}