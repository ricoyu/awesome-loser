package com.loserico.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

import org.junit.Test;

/**
 * @of
 * I/O is classified as block-oriented or stream-oriented. 
 * Reading from or writing to a file is an example of block-oriented I/O. 
 * In contrast, reading from the keyboard or writing to a network connection is an example of stream oriented I/O.
 * 
 * Stream I/O is often slower than block I/O. 
 * Furthermore, input tends to be intermittent. 
 * For example, the user might pause while entering a stream of characters or momentary slowness in a network connection
 * causes a playing video to proceed in a jerky fashion.
 * 
 * Many operating systems allow streams to be configured to operate in nonblocking mode in which a thread
 * continually checks for available input without blocking when no input is available. 
 * The thread can handle incoming data or perform other tasks until data arrives.
 * 
 * This “polling for available input” activity can be wasteful, especially when the thread needs to monitor
 * many input streams (such as in a web server context). 
 * Modern operating systems can perform this checking efficiently, which is known as readiness selection, and which is often built on top of nonblocking mode.
 * 
 * The operating system monitors a collection of streams and returns an indication to the thread of which streams are ready to perform I/O. 
 * As a result, a single thread can multiplex many active streams via common code and makes it possible, 
 * in a web server context, to manage a huge number of network connections.
 * 
 * JDK 1.4 supports readiness selection by providing selectors, which are instances of the java.nio.channels.Selector class 
 * that can examine one or more channels and determine which channels are ready for reading or writing. 
 * This way a single thread can manage multiple channels (and, therefore, multiple network connections) efficiently. 
 * Being able to use fewer threads is advantageous where thread
 * creation and thread context switching is expensive in terms of performance and/or memory use.
 * 
 * Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。
 * 
 * 为什么使用Selector?
 * 仅用单个线程来处理多个Channels的好处是, 只需要更少的线程来处理通道。事实上, 可以只用一个线程处理所有的通道。
 * 对于操作系统来说, 线程之间上下文切换的开销很大, 而且每个线程都要占用系统的一些资源(如内存), 因此, 使用的线程越少越好。
 * 但是, 需要记住, 现代的操作系统和CPU在多任务方面表现的越来越好, 所以多线程的开销随着时间的推移, 变得越来越小了。
 * 实际上, 如果一个CPU有多个内核, 不使用多任务可能是在浪费CPU能力。在这里，只要知道使用Selector能够处理多个通道就足够了。
 * @on
 * 
 * @author Rico Yu
 * @since 2016-11-27 12:38
 * @version 1.0
 *
 */
public class SelectorsTest {

	@Test
	public void testSelectorCreate() {
		// 通过调用Selector.open()方法创建一个Selector
		try {
			Selector selector = Selector.open();
			/*
			 * 向Selector注册通道
			 * 
			 * 为了将Channel和Selector配合使用，必须将channel注册到selector上。通过SelectableChannel.register()
			 * 方法来实现
			 */
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			/*
			 * 与Selector一起使用时, Channel必须处于非阻塞模式下。这意味着不能将FileChannel与Selector一起使用, 因为FileChannel不能切换到非阻塞模式。
			 * 而套接字通道都可以。
			 */
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(8080));

			/* 
			 * 注意register()方法的第二个参数。这是一个"interest集合", 意思是在通过Selector监听Channel时对什么事件感兴趣。
			 * 
			 * 可以监听四种不同类型的事件:
			 * 	Connect 
			 * 	Accept 
			 * 	Read 
			 * 	Write 
			 * 
			 * 通道触发了一个事件意思是该事件已经就绪。
			 * 所以:
			 * 	某个channel成功连接到另一个服务器称为"连接就绪"。
			 * 	一个server socket channel准备好接收新进入的连接称为"接收就绪"。
			 * 	一个有数据可读的通道可以说是"读就绪"。
			 * 	等待写数据的通道可以说是"写就绪"。
			 * 
			 * 这四种事件用SelectionKey的四个常量来表示:
			 * 	SelectionKey.OP_CONNECT 
			 * 	SelectionKey.OP_ACCEPT 
			 * 	SelectionKey.OP_READ 
			 * 	SelectionKey.OP_WRITE
			 * 
			 * 如果你对不止一种事件感兴趣，那么可以用"位或"操作符将常量连接起来
			 * 如下:
			 * 	int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
			 * @on
			 */
			SelectionKey key = serverSocketChannel.register(selector,
					SelectionKey.OP_READ | SelectionKey.OP_WRITE);

			/*
			 * 就像向Selector注册通道一节中所描述的, interest集合是你所选择的感兴趣的事件集合。
			 * 可以通过SelectionKey读写interest集合
			 * 可以看到, 用"位与"操作interest 集合和给定的SelectionKey常量, 可以确定某个确定的事件是否在interest 集合中。
			 */
			int interestSet = key.interestOps();
			boolean isInterestedInAccept = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT;
			boolean isInterestedInConnect = (interestSet & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT;
			boolean isInterestedInRead = (interestSet & SelectionKey.OP_READ) == SelectionKey.OP_READ;
			boolean isInterestedInWrite = (interestSet & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE;

			/*
			 * ready 集合是通道已经准备就绪的操作的集合。
			 * 在一次选择(Selection)之后, 你会首先访问这个ready set
			 * @on
			 */
			int readySet = key.readyOps();

			/*
			 * 可以用像检测interest集合那样的方法, 来检测channel中什么事件或操作已经就绪。
			 * 但是, 也可以使用以下四个方法, 它们都会返回一个布尔类型:
			 */
			boolean isAcceptable = key.isAcceptable();
			boolean isConnectable = key.isConnectable();
			boolean isReadable = key.isReadable();
			boolean isWritable = key.isWritable();

			/*
			 * 从SelectionKey访问Channel和Selector很简单
			 */
			Channel channel = key.channel();
			Selector selector2 = key.selector();

			/*
			 * 可以将一个对象或者更多信息附着到SelectionKey上, 这样就能方便的识别某个给定的通道。
			 * 例如，可以附加 与通道一起使用的Buffer, 或是包含聚集数据的某个对象。
			 * 使用方法如下:
			 */
			AttachedInfo info = new AttachedInfo();
			info.setMessage("这是一条额外的Message");
			key.attach(info);
			Object attachment = key.attachment();

			while (true) {
				/*
				 * 还可以在用register()方法向Selector注册Channel的时候附加对象
				 * SelectionKey key = channel.register(selector, SelectionKey.OP_READ, theObject);
				 * 
				 * @of
				 * 通过Selector选择通道
				 * 
				 * 一旦向Selector注册了一或多个通道，就可以调用几个重载的select()方法。这些方法返回你所感兴趣的事件（如连接、接受、读或写）已经准备就绪的那些通道。
				 * 换句话说, 如果你对"读就绪" 的通道感兴趣, select()方法会返回读事件已经就绪的那些通道。
				 * 
				 * 下面是select()方法：
				 * 	int select() 
				 * 	int select(long timeout) 
				 * 	int selectNow()
				 * 
				 * select()阻塞到至少有一个通道在你注册的事件上就绪了。
				 * select(long timeout)和select()一样，除了最长会阻塞timeout毫秒(参数)。
				 * selectNow()不会阻塞，不管什么通道就绪都立刻返回（译者注：此方法执行非阻塞的选择操作。如果自从前一次选择操作后，没有通道变成可选择的，则此方法直接返回零。）。
				 * 
				 * select()方法返回的int值表示有多少通道已经就绪。
				 * 亦即, 自上次调用select()方法后有多少通道变成就绪状态。
				 * 如果调用select()方法, 因为有一个通道变成就绪状态, 返回了1，
				 * 若再次调用select()方法, 如果另一个通道就绪了, 它会再次返回1。
				 * 如果对第一个就绪的channel没有做任何操作, 现在就有两个就绪的通道, 但在每次select()方法调用之间，只有一个通道就绪了。
				 * 
				 * wakeUp()
				 * 
				 * 某个线程调用select()方法后阻塞了, 即使没有通道已经就绪, 也有办法让其从select()方法返回。
				 * 只要让其它线程在第一个线程调用select()方法的那个对象上调用 Selector.wakeup()方法即可, 阻塞在select()方法上的线程会立马返回。
				 * 
				 * 如果有其它线程调用了wakeup()方法, 但当前没有线程阻塞在select()方法上, 下个调用select()方法的线程会立即"醒来(wake up)"。
				 * @on
				 */

				int readyChannels = selector.select();
				if (readyChannels > 0) {
					/*
					 * @of
					 * selectedKeys()
					 * 
					 * 一旦调用了select()方法，并且返回值表明有一个或更多个通道就绪了，然后可以通过调用selector的selectedKeys()方法，访问“已选择键集（selected key set）”中的就绪通道。
					 * 如下所示：
					 * 	Set selectedKeys = selector.selectedKeys();
					 * 当像Selector注册Channel时，Channel.register()方法会返回一个SelectionKey对象。
					 * 这个对象代表了注册到该Selector的通道。可以通过SelectionKey的selectedKeySet()方法访问这些对象。
					 * 
					 * 可以遍历这个已选择的键集合来访问就绪的通道。如下：
					 * @on
					 */
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					for (SelectionKey selectionKey : selectedKeys) {
						if (selectionKey.isAcceptable()) {
							// a connection was accepted by a ServerSocketChannel.
						} else if (selectionKey.isConnectable()) {
							// a connection was established with a remote server.
						} else if (selectionKey.isReadable()) {
							// a channel is ready for reading
						} else if (selectionKey.isWritable()) {
							// a channel is ready for writing
							/*
							 * SelectionKey.channel()方法返回的通道需要转型成你要处理的类型，如ServerSocketChannel或SocketChannel等。
							 */
							ServerSocketChannel serverSocketChannel2 = (ServerSocketChannel) selectionKey.channel();
						}
						/*
						 * 这个循环遍历已选择键集中的每个键，并检测各个键所对应的通道的就绪事件。
						 * 
						 * 注意每次迭代末尾的keyIterator.remove()调用。Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除。
						 * 下次该通道变成就绪时，Selector会再次将其放入已选择键集中。
						 */
						selectedKeys.remove(selectionKey);
					}
				}

				/*
				 * close()
				 * 
				 * 用完Selector后调用其close()方法会关闭该Selector，且使注册到该Selector上的所有SelectionKey实例无效。通道本身并不会关闭。
				 */
				selector.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class AttachedInfo {
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
