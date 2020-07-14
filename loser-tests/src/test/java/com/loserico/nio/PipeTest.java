package com.loserico.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java.nio.channels.Pipe is a pipe that reads and writes data in a sequence.
 * Pipe ensures that data will be read in same order in which it is written to
 * Pipe. Pipe has Pipe.SinkChannel and Pipe.SourceChannel. Pipe.SinkChannel
 * represents writable channel and Pipe.SourceChannel represents readable
 * channel. In our example we have a test data, we will write it into Pipe using
 * Pipe.SinkChannel and then we will read it by using Pipe.SourceChannel.
 * 
 * Java NIO 管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取。
 * 
 * @author Rico Yu
 * @since 2016-12-11 13:48
 * @version 1.0
 *
 */
public class PipeTest {
	private static final Logger logger = LoggerFactory.getLogger(PipeTest.class);

	public static void main(String[] args) {
		try {
			Pipe pipe = Pipe.open();

			// 要向管道写数据，需要访问sink通道
			SinkChannel sinkChannel = pipe.sink();

			String testData = "Test Data to Check java NIO Channels Pipe.";

			ByteBuffer buffer = ByteBuffer.allocate(512);
			buffer.put(testData.getBytes());

			buffer.flip();
			/*
			 * write data into sink channel.
			 * 通过调用SinkChannel的write()方法，将数据写入SinkChannel
			 */
			while (buffer.hasRemaining()) {
				sinkChannel.write(buffer);
			}

			// gets pipe's source channel
			SourceChannel sourceChannel = pipe.source();
			buffer = ByteBuffer.allocate(512);

			// write data into console
			while (sourceChannel.read(buffer) > 0) {
				buffer.flip();

				while (buffer.hasRemaining()) {
					System.out.print((char) buffer.get());
				}

				// position is set to zero and limit is set to capacity to clear
				// the buffer.
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
