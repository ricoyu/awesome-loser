package com.loserico.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @of
 * A channel serves as a conduit for communicating (via the operating system) with a DMA controller to
 * efficiently drain byte buffers to or fill byte buffers from a disk. JDK 1.4’s java.nio.channels.Channel
 * interface, its subinterfaces, and various classes implement the channel architecture.
 * 
 * One of these classes is called java.nio.channels.FileChannel, and it abstracts a channel for reading,
 * writing, mapping, and manipulating a file. One interesting feature of FileChannel is its support for file
 * locking, upon which sophisticated applications such as database management systems rely.
 * 
 * File locking lets a process prevent or limit access to a file while the process is accessing the file.
 * Although file locking can be applied to an entire file, it is often narrowed to a smaller region. A lock
 * ranges from a starting byte offset in the file and continues for a specific number of bytes.
 * 
 * Another interesting FileChannel feature is memory-mapped file I/O via the map() method. map() returns a
 * java.nio.MappedByteBuffer whose content is a memory-mapped region of a file. File content is accessed via
 * memory accesses; buffer copies and read-write system calls are eliminated.
 * 
 * You can obtain a channel by calling the java.nio.channels.Channels class’s methods or the methods in
 * classic I/O classes such as RandomAccessFile.
 * 
 * Channel的子类分为两大块: 
 * 
 * FileChannel
 * 		针对文件IO的Channel, 可以通过FileInputStream, FileOutputStream和RandomAccessFile来获得, 不支持非阻塞模式, 进而也就不支持readiness selection 
 * SelectableChannel
 * 		除File以外, 像对Socket IO做支持的Channel都属于SelectableChannel, 支持非阻塞模式和readiness selection
 * 
 * SelectionKey
 * 接下来说说在Channel和Selector之间的关联对象SelectionKey。既然是关联对象, 那肯定是可以得到连接的两个对象的: 
 * public abstract SelectableChannel channel()
 * public abstract Selector selector()
 * 
 * 还有支持的感兴趣的事件, 以及已经准备好IO的事件, 感兴趣的事件的方法是同名重载, 一个为get另一个为set:
 * public abstract int interestOps( );
 * public abstract void interestOps(int ops);
 * public abstract int readyOps( );

 * Java NIO的通道类似流, 但又有些不同: 
 * 	既可以从通道中读取数据, 又可以写数据到通道。但流的读写通常是单向的。
 * 	通道可以异步地读写。
 * 	通道中的数据总是要先读到一个Buffer, 或者总是要从一个Buffer中写入。
 * 
 * @author Rico Yu
 * @since 2016-11-27 12:36
 * @version 1.0
 * @on
 */
public class ChannelsTest {

	private static final Logger logger = LoggerFactory.getLogger(ChannelsTest.class);

	@Test
	public void testSelectorChannel() throws IOException {
		int port = 30;
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.socket().bind(new InetSocketAddress(port));
		Selector selector = Selector.open();
		serverChannel.configureBlocking(false);
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		while (true) {
			int n = selector.select();
			if (n == 0) {
				continue; // nothing to do
			}
			Iterator it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				if (key.isAcceptable()) {
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					SocketChannel channel = server.accept();
					if (channel == null) {
						;// handle code, could happen
					}
					channel.configureBlocking(false);
					channel.register(selector, SelectionKey.OP_READ);

				}
				if (key.isReadable()) {
					// readDataFromSocket (key);
				}
				it.remove();
			}
		}
	}

	@Test
	public void testFileChannel() {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile("nio-data.txt", "rw")) {
			FileChannel fileChannel = randomAccessFile.getChannel();

			ByteBuffer byteBuffer = ByteBuffer.allocate(48);
			int bytesRead = fileChannel.read(byteBuffer);

			while (bytesRead != -1) {
				System.out.println("Read " + bytesRead);
				// 注意 buf.flip() 的调用, 首先读取数据到Buffer, 然后反转Buffer,接着再从Buffer中读取数据。
				byteBuffer.flip();

				while (byteBuffer.hasRemaining()) {
					System.out.print((char) byteBuffer.get());
				}

				byteBuffer.clear();
				bytesRead = fileChannel.read(byteBuffer);
			}
			fileChannel.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 在Java NIO中, 如果两个通道中有一个是FileChannel, 那你可以直接将数据从一个channel传输到另外一个channel。
	 * FileChannel的transferFrom()方法可以将数据从源通道传输到FileChannel中
	 */
	@Test
	public void testChannelTransfer() {
		try (RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
				RandomAccessFile toFile = new RandomAccessFile("tofile.txt", "rw");) {
			FileChannel fromChannel = fromFile.getChannel();
			FileChannel toChannel = toFile.getChannel();
			
			long position = 0;
			long count = fromChannel.size();
			
//			toChannel.transferFrom(fromChannel, position, count);
			fromChannel.transferTo(position, count, toChannel);
		} catch (IOException e) {
			logger.error(null, e);
		}
	}
}
