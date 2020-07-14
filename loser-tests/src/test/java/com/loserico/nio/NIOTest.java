package com.loserico.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

public class NIOTest {

	@Test
	public void testChannel() {
		RandomAccessFile randonAccessFile = null;
		try {
			randonAccessFile = new RandomAccessFile("src/nio.txt", "rw");
			FileChannel fileChannel = randonAccessFile.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(1024);

			int bytesRead = fileChannel.read(buf);
			System.out.println(bytesRead);

			while (bytesRead != -1) {
				buf.flip();
				while (buf.hasRemaining()) {
					System.out.print((char) buf.get());
				}

				buf.compact();
				bytesRead = fileChannel.read(buf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (randonAccessFile != null) {
					randonAccessFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
