package com.loserico.niodemo.writabledemo;

/**
 * <p>
 * Copyright: (C), 2022-08-04 15:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NIoServerWritable {
	
	private static NioServerHandleWriteable nioServerHandleWriteable;
	
	public static void main(String[] args) {
		nioServerHandleWriteable = new NioServerHandleWriteable(12345);
		new Thread(nioServerHandleWriteable,"NIO_Server").start();
	}
}
