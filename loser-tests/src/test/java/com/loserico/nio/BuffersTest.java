package com.loserico.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Buffers are the foundation for NIO operations. Essentially, NIO is all about moving
 * data into and out of buffers.
 * 
 * Java NIO中的Buffer用于和NIO通道进行交互。如你所知, 数据是从通道读入缓冲区, 从缓冲区写入到通道中的。
 * 
 * 缓冲区本质上是一块可以写入数据, 然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象, 并提供了一组方法, 用来方便的访问该块内存。
 * 
 * @author Rico Yu
 * @since 2016-11-27 12:27
 * @version 1.0
 *
 */
public class BuffersTest {

	/**
	 * @throws IOException 
	 * @of
	 * 使用Buffer读写数据一般遵循以下四个步骤：
	 * 
	 * 写入数据到Buffer 
	 * 调用flip()方法 
	 * 从Buffer中读取数据 
	 * 调用clear()方法或者compact()方法
	 * 
	 * 当向buffer写入数据时, buffer会记录下写了多少数据。一旦要读取数据, 需要通过flip()方法将Buffer从写模式切换到读模式。
	 * 在读模式下, 可以读取之前写入到buffer的所有数据。
	 * 
	 * 一旦读完了所有的数据, 就需要清空缓冲区, 让它可以再次被写入。有两种方式能清空缓冲区：调用clear()或compact()方法。clear()方法会清空整个缓冲区。
	 * compact()方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处, 新写入的数据将放到缓冲区未读数据的后面。
	 * 
	 * 缓冲区本质上是一块可以写入数据, 然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象, 并提供了一组方法, 用来方便的访问该块内存。
	 * 为了理解Buffer的工作原理, 需要熟悉它的三个属性：
	 * capacity
	 * position
	 * limit
	 * 
	 * position和limit的含义取决于Buffer处在读模式还是写模式。不管Buffer处在什么模式, capacity的含义总是一样的。
	 * 
	 * capacity
	 * 作为一个内存块, Buffer有一个固定的大小值, 也叫“capacity”.你只能往里写capacity个byte、long, char等类型。一旦Buffer满了, 需要将其清空（通过读数据或者清除数据）才能继续写数据往里写数据。
	 * 
	 * position
	 * 当你写数据到Buffer中时, position表示当前的位置。初始的position值为0.当一个byte、long等数据写到Buffer后,  position会向前移动到下一个可插入数据的Buffer单元。
	 * position最大可为capacity – 1.
	 * 当读取数据时, 也是从某个特定位置读。当将Buffer从写模式切换到读模式, position会被重置为0. 当从Buffer的position处读取数据时, position向前移动到下一个可读的位置。
	 * 
	 * limit
	 * 在写模式下, Buffer的limit表示你最多能往Buffer里写多少数据。 写模式下, limit等于Buffer的capacity。
	 * 当切换Buffer到读模式时,  limit表示你最多能读到多少数据。因此, 当切换Buffer到读模式时, limit会被设置成写模式下的position值。
	 * 换句话说, 你能读到之前写入的所有数据（limit被设置成已写数据的数量, 这个值在写模式下就是position）
	 * @on
	 */
	@Test
	public void testBasicUsage() throws IOException {
		RandomAccessFile aFile = new RandomAccessFile("nio-data.txt", "rw");
		FileChannel inChannel = aFile.getChannel();

		/*
		 * @of
		 * Java NIO 有以下Buffer类型
		 * 
		 * ByteBuffer 
		 * MappedByteBuffer 
		 * CharBuffer 
		 * DoubleBuffer 
		 * FloatBuffer 
		 * IntBuffer 
		 * LongBuffer
		 * ShortBuffer
		 * 
		 * 如你所见, 这些Buffer类型代表了不同的数据类型。换句话说, 就是可以通过char, short, int, long, float 或 double类型来操作缓冲区中的字节。
		 * @on
		 */
		ByteBuffer buf = ByteBuffer.allocate(48);

		int bytesRead = inChannel.read(buf); // read into buffer.
		while (bytesRead != -1) {

			/*
			 * flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0, 并将limit设置成之前position的值。
			 * 换句话说, position现在用于标记读的位置, limit表示之前写进了多少个byte、char等 —— 现在能读取多少个byte、char等
			 */
			buf.flip(); // make buffer ready for read

			while (buf.hasRemaining()) {
				System.out.print((char) buf.get()); // read 1 byte at a time
				if (buf.position() == 5) {
					// 通过调用Buffer.mark()方法, 可以标记Buffer中的一个特定position。之后可以通过调用Buffer.reset()方法恢复到这个position。
					buf.mark();
				}
			}
			System.out.println();

			buf.reset();
			while (buf.hasRemaining()) {
				System.out.print((char) buf.get()); // read 1 byte at a time
				if (buf.position() == 5) {
					buf.mark();
				}
			}
			System.out.println();

			/*
			 * Buffer.rewind()将position设回0, 所以你可以重读Buffer中的所有数据。limit保持不变, 
			 * 仍然表示能从Buffer中读取多少个元素（byte、 char等）。
			 */
			buf.rewind();
			while (buf.hasRemaining()) {
				System.out.print((char) buf.get()); // read 1 byte at a time
			}

			/*
			 * 一旦读完Buffer中的数据, 需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成。
			 * 
			 * 如果调用的是clear()方法, position将被设回0, limit被设置成 capacity的值。换句话说, Buffer
			 * 被清空了。Buffer中的数据并未清除, 只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
			 * 
			 * 如果Buffer中有一些未读的数据, 调用clear()方法, 数据将“被遗忘”, 意味着不再有任何标记会告诉你哪些数据被读过, 哪些还没有。
			 * 
			 * 如果Buffer中仍有未读的数据, 且后续还需要这些数据, 但是此时想要先先写些数据, 那么使用compact()方法。
			 * 
			 * compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。
			 * limit属性依然像clear()方法一样,  设置成capacity。现在Buffer准备好写数据了, 但是不会覆盖未读的数据。
			 */
			buf.clear(); // make buffer ready for writing
			bytesRead = inChannel.read(buf);
		}
		aFile.close();
	}

	@Test
	public void testAllocateDirect() throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16 * 1024);
		ReadableByteChannel srcChannel = null;
		WritableByteChannel destChannel = null;

		while (srcChannel.read(byteBuffer) != -1) {
			/*
			 * prepare the buffer to be drained
			 * flip()只是调整position到0,limit到当前的position, 以准备读取buffer中的数据 compact()
			 * compact()同样会调整position到0,limit到当前的position, 但它还会将数据移动到buffer的开头, 
			 * 以便后续写入数据到这个buffer
			 * 下面的代码中destChannel.write(byteBuffer)可能只是将byteBuffer的一部分数据写入了destChannel, 
			 * 还有一部分数据没有被写入
			 * 这时候又开始下一轮从srcChannel中读取数据到byteBuffer, 这时候就需要调用byteBuffer.compact()方法
			 * 将还未写入destChannel那部分数据移动到byteBuffer开头位置, 然后从新位置开始将数据写入byteBuffer
			 */
			byteBuffer.flip();
			// write to the channel, may block
			destChannel.write(byteBuffer);
			// If partial transfer, shift remainder down
			// If buffer is empty, same as doing clear()
			byteBuffer.compact();
		}
		// EOF will leave buffer in fill state
		byteBuffer.flip();

		// make sure the buffer is fully drained.
		while (byteBuffer.hasRemaining()) {
			destChannel.write(byteBuffer);
		}
	}

	@Test
	public void testPut() {
		ByteBuffer buffer = ByteBuffer.allocate(48 * 1024);
		List<Object> results = new ArrayList<Object>();
		for (Object n : results) {
			if (n instanceof Byte) {
				buffer.put((Byte) n);
			} else if (n instanceof Short) {
				buffer.putShort((Short) n);
			} else if (n instanceof Character) {
				buffer.putChar((Character) n);
			} else if (n instanceof Integer) {
				buffer.putInt((Integer) n);
			} else if (n instanceof Long) {
				buffer.putLong((Long) n);
			} else if (n instanceof Float) {
				buffer.putFloat((Float) n);
			} else if (n instanceof Double) {
				buffer.putDouble((Double) n);
			} else if (n instanceof Short) {
				buffer.putShort((Short) n);
			}
		}
	}
}
