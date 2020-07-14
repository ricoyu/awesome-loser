package com.loserico.pattern.structural.proxy;

/**
 * <p>
 * Copyright: (C), 2020/2/13 10:45
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProxyPatternDemo {
	
	public static void main(String[] args) {
		Image image = new ProxyImage("test_10mb.jpg");
		// 图像将从磁盘加载
		image.display();
		System.out.println("");
		// 图像不需要从磁盘加载
		image.display();
	}
}
