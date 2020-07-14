package com.loserico.nio.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class ChannelDemo {

	public static void main(String[] args) {
		ReadableByteChannel src = Channels.newChannel(System.in);
		WritableByteChannel dest = Channels.newChannel(System.out);
		try {
			// copy(src, dest);
			copyAlt(src, dest);
		} catch (IOException ioe) {
			System.err.println("I/O error: " + ioe.getMessage());
		} finally {
			try {
				src.close();
				dest.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	static void copy(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocateDirect(2048);
		// read一般会将buffer的剩余容量大小的bytes写入buffer
		while (src.read(buffer) != -1) {
			buffer.flip();
			// write一般会将buffer中剩余的bytes写入到Channel中
			dest.write(buffer);
			buffer.compact();
		}
		buffer.flip();
		while (buffer.hasRemaining())
			dest.write(buffer);
	}

	static void copyAlt(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocateDirect(2048);
		while (src.read(buffer) != -1) {
			buffer.flip();
			while (buffer.hasRemaining())
				dest.write(buffer);
			buffer.clear();
		}
	}
}