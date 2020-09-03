package com.loserico.nio.buffer;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>
 * Copyright: (C), 2020-08-21 15:28
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BufferUsageTest {
	
	@SneakyThrows
	@Test
	public void test1() {
		RandomAccessFile file = new RandomAccessFile("d://txdm.txt", "rw");
		FileChannel channel = file.getChannel();
		
		//create buffer with capacity of 48 bytes
		ByteBuffer buffer = ByteBuffer.allocate(48);
		
		//read into buffer.
		int read = channel.read(buffer);
		while (read != -1) {
			//make buffer ready for read
			buffer.flip();
			while (buffer.hasRemaining()) {
				// read 1 byte at a time
				System.out.print((char) buffer.get());
			}
			
			buffer.clear();
			read = channel.read(buffer);
		}
		
		file.close();
	}
}
