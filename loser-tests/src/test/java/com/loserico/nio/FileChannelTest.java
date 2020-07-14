package com.loserico.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java NIO中的FileChannel是一个连接到文件的通道。可以通过文件通道读写文件。
 * 
 * FileChannel无法设置为非阻塞模式，它总是运行在阻塞模式下。
 * 
 * 打开FileChannel <p>
 * 在使用FileChannel之前，必须先打开它。但是，我们无法直接打开一个FileChannel，需要通过使用一个InputStream、OutputStream或RandomAccessFile来获取一个FileChannel实例
 * 
 * @author Rico Yu
 * @since 2016-12-10 20:31
 * @version 1.0
 *
 */
public class FileChannelTest {

	private static final Logger logger = LoggerFactory.getLogger(FileChannelTest.class);

	@Test
	public void testFileChannel() {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile("nio-data.txt", "rw")) {
			FileChannel fileChannel = randomAccessFile.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(48);
			/*
			 * 调用FileChannel.read()方法。该方法将数据从FileChannel读取到Buffer中。read()
			 * 方法返回的int值表示了有多少字节被读到了Buffer中。如果返回-1，表示到了文件末尾。
			 */
			fileChannel.read(buffer);
			buffer.flip();

			while (buffer.hasRemaining()) {
				System.out.print((char) buffer.get());
			}
			System.out.println("");

			String newData = "New String to write to file..." + System.currentTimeMillis();
			buffer.clear();
			buffer.put(newData.getBytes());
			buffer.flip();

			while (buffer.hasRemaining()) {
				System.out.print((char) buffer.get());
			}

			buffer.flip();

			/*
			 * 使用FileChannel.write()方法向FileChannel写数据，该方法的参数是一个Buffer
			 * 注意FileChannel.write()是在while循环中调用的。因为无法保证write()
			 * 方法一次能向FileChannel写入多少字节，因此需要重复调用write()方法，直到Buffer中已经没有尚未写入通道的字节。
			 */
			while (buffer.hasRemaining()) {
				fileChannel.write(buffer);
			}
			
			//用完FileChannel后必须将其关闭。如：
			//fileChannel.close();
		} catch (IOException e) {
			logger.error("msg", e);
		}
	}
}
