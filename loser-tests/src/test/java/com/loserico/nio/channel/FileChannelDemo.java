package com.loserico.nio.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

public class FileChannelDemo {

	@Test
	public void test1() throws IOException {
		RandomAccessFile raf = new RandomAccessFile("temp", "rw");
		FileChannel fc = raf.getChannel();
		long pos;
		System.out.println("Position = " + (pos = fc.position()));
		System.out.println("size: " + fc.size());
		String msg = "This is a test message.";
		ByteBuffer buffer = ByteBuffer.allocateDirect(msg.length() * 2);
		buffer.asCharBuffer().put(msg);
		fc.write(buffer);
		fc.force(true);
		System.out.println("position: " + fc.position());
		System.out.println("size: " + fc.size());
		buffer.clear();
		fc.position(pos);
		fc.read(buffer);
		buffer.flip();
		while (buffer.hasRemaining()) {
			System.out.print(buffer.getChar());
		}
	}

	@Test
	public void test2() throws IOException {
		RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
		FileChannel inChannel = aFile.getChannel();

		ByteBuffer buffer = ByteBuffer.allocate(48);

		int bytesRead = inChannel.read(buffer);
		while (bytesRead != -1) {
			System.out.println("Read " + bytesRead);
			buffer.flip();

			while (buffer.hasRemaining()) {
				System.out.println((char) buffer.get());
			}

			buffer.clear();
			bytesRead = inChannel.read(buffer);
		}

		aFile.close();
	}
}