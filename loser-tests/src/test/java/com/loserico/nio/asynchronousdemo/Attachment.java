package com.loserico.nio.asynchronousdemo;

import lombok.Data;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * <p>
 * Copyright: (C), 2022-01-25 15:26
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class Attachment {
	
	private AsynchronousServerSocketChannel serverSocketChannel;
	
	private AsynchronousSocketChannel socketChannel;
	
	private boolean isReadMode;
	
	private ByteBuffer buffer;
}
