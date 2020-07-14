package com.loserico.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

/**
 * NIO 缓冲区测试
 * <p>
 * Copyright: Copyright (c) 2019-03-21 11:12
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class NIOBufferTest {

	@Test
	public void testBufferWrite() throws IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile("nio-data.txt", "rw");
		FileChannel fileChannel = randomAccessFile.getChannel();
		// 创建容量为48字节的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(48);
		int byteRead = fileChannel.read(buffer);

		while (byteRead != -1) {
			buffer.flip();
			if (buffer.hasRemaining()) {
				System.out.println((char) buffer.get()); // 一次读取1个字节
			}
			buffer.clear();
			byteRead = fileChannel.read(buffer);
		}
		randomAccessFile.close();
	}
}
