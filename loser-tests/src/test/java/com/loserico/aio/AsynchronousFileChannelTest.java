package com.loserico.aio;

import static java.nio.file.StandardOpenOption.READ;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * The AsynchronousFileChannel makes it possible to read data from, and write data to
 * files asynchronously.
 * 
 * @author Rico Yu
 * @since 2016-12-11 14:17
 * @version 1.0
 *
 */
public class AsynchronousFileChannelTest {

	/**
	 * This example creates an AsynchronousFileChannel and then creates a ByteBuffer
	 * which is passed to the read() method as parameter, along with a position of 0.
	 * After calling read() the example loops until the isDone() method of the
	 * returned Future returns true. Of course, this is not a very efficient use of
	 * the CPU - but somehow you need to wait until the read operation has completed.
	 * 
	 * Once the read operation has completed the data read into the ByteBuffer and
	 * then into a String and printed to System.out .
	 */
	@Test
	public void testRead() {
		Path path = Paths.get("test.txt");
		try {
			/*
			 * The first parameter to the open() method is a Path instance pointing to
			 * the file the AsynchronousFileChannel is to be associated with.
			 * 
			 * The second parameter is one or more open options which tell the
			 * AsynchronousFileChannel what operations is to be performed on the
			 * underlying file. In this example we used the StandardOpenOption.READ
			 * which means that the file will be opened for reading.
			 */
			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, READ);

			ByteBuffer buffer = ByteBuffer.allocate(4);

			/*
			 * This version of the read() method takes ByteBuffer as first parameter.
			 * The data read from the AsynchronousFileChannel is read into this
			 * ByteBuffer. The second parameter is the byte position in the file to
			 * start reading from.
			 */
			Future<Integer> operation = fileChannel.read(buffer, 0);

			/*
			 * The read() method return immediately, even if the read operation has
			 * not finished. You can check the when the read operation is finished by
			 * calling the isDone() method of the Future instance returned by the
			 * read() method.
			 */
			while (!operation.isDone()) {
				;
			}

			buffer.flip();
			byte[] data = new byte[buffer.limit()];
			buffer.get(data);
			System.out.println(new String(data));
			buffer.clear();

			fileChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The second method of reading data from an AsynchronousFileChannel is to call
	 * the read() method version that takes a CompletionHandler as a parameter.
	 * 
	 * Once the read operation finishes the CompletionHandler's completed() method
	 * will be called. As parameters to the completed() method are passed an Integer
	 * telling how many bytes were read, and the "attachment" which was passed to the
	 * read() method. The "attachment" is the third parameter to the read() method. In
	 * this case it was the ByteBuffer into which the data is also read. You can
	 * choose freely what object to attach.
	 * 
	 * If the read operation fails, the failed() method of the CompletionHandler will
	 * get called instead.
	 */
	@Test
	public void testReadWithCompletionHandler() {
		Path path = Paths.get("test.txt");
		try {
			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, READ);
			ByteBuffer buffer = ByteBuffer.allocate(4);
			ByteBuffer attachment = ByteBuffer.allocate(48);
			attachment.put("This is attached data".getBytes());
			fileChannel.read(buffer, 0, attachment, new CompletionHandler<Integer, ByteBuffer>() {

				@Override
				public void completed(Integer result, ByteBuffer attachment) {
					System.out.println("result = " + result);
					attachment.flip();
					byte[] data = new byte[attachment.limit()];
					attachment.get(data);
					System.out.println(new String(data));
					attachment.clear();
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
				}

			});

			fileChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReadWithInSufficientBufferSize() {
		Path path = Paths.get("test.txt");
		try {
			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, READ);
			ByteBuffer buffer = ByteBuffer.allocate(4);
			fileChannel.read(buffer, 0, null, new CompletionHandler<Integer, Object>() {

				@Override
				public void completed(Integer result, Object attachment) {
					System.out.println("result = " + result);
					byte[] data = new byte[buffer.limit()];
					buffer.get(data);
					System.out.println(new String(data));
					buffer.clear();
				}

				@Override
				public void failed(Throwable e, Object attachment) {
					e.printStackTrace();
				}

			});

			fileChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * AsynchronousFileChannels don’t belong to groups. However, they are associated
	 * with a thread pool to which tasks are submitted, to handle I/O events and to
	 * dispatch to completion handlers that consume the results of I/O operations on
	 * the channel.
	 * 
	 * The completion handler for an I/O operation initiated on a channel is
	 * guaranteed to be invoked by one of the threads in the thread pool, which
	 * ensures that the completion handler is run by a thread with the expected
	 * identity. If an I/O operation completes immediately and the initiating thread
	 * is itself a thread in the thread pool, the completion handler may be invoked
	 * directly by the initiating thread.
	 * 
	 * When an asynchronous file channel is created without specifying a thread pool,
	 * the channel is associated with a system-dependent default thread pool that may
	 * be shared with other channels. The default thread pool is configured by the
	 * system properties defined by AsynchronousChannelGroup.
	 * 
	 * An asynchronous file channel created by AsynchronousFileChannel’s
	 * AsynchronousFileChannel open(Path file, OpenOption... options) class method is
	 * associated with the default thread pool. You can associate a file channel with
	 * another thread pool by calling the AsynchronousFileChannel open(Path file,
	 * Set<? extends OpenOption> options, ExecutorService executor,
	 * FileAttribute<?>... attrs) class method. Here, executor identifies the desired
	 * thread pool.
	 */
	@Test
	public void testAsynchronousFileChannelThreadPool() {

	}
}
