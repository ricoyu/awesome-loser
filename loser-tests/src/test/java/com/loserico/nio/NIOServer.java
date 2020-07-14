package com.loserico.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO模型的selector 就像一个大总管，负责监听各种IO事件，然后转交给后端线程去处理
 * <p>
 * NIO相对于BIO非阻塞的体现就在: 
 * BIO的后端线程需要阻塞等待客户端写数据(比如read方法), 如果客户端不写数据线程就要阻塞
 * NIO把等待客户端操作的事情交给了大总管 selector, selector 负责轮询所有已注册的客户端, 
 * 发现有事件发生了才转交给后端线程处理, 后端线程不需要做任何阻塞等待, 直接处理客户端事件的数据即可, 
 * 处理完马上结束, 或返回线程池供其他客户端事件继续使用. 
 * 还有就是 channel 的读写是非阻塞的。
 * <p>
 * Copyright: (C), 2019/12/21 16:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NIOServer {
	
	public static void main(String[] args) throws IOException {
		/*
		 * 1: 创建一个 ServerSocketChannel 和 Selector ，并将 ServerSocketChannel 注册到 Selector 上
		 */
		
		//创建一个在本地端口进行监听的服务Socket通道.并设置为非阻塞方式
		ServerSocketChannel ssc = ServerSocketChannel.open();
		// 必须配置为非阻塞才能往selector上注册，否则会报错，selector模式本身就是非阻塞模式
		ssc.configureBlocking(false);
		ssc.socket().bind(new InetSocketAddress(8080));
		
		//创建一个选择器并将serverSocketChannel注册到它上面
		Selector selector = Selector.open();
		// 把channel注册到selector上，并且selector对客户端accept连接操作感兴趣
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		
		while (true) {
			System.out.println("等待事件发生...");
			/*
			 * 2: selector 通过 select() 方法监听 channel 事件, 当客户端连接时, selector 监听到连接事件,
			 * 获取到 ServerSocketChannel 注册时绑定的 selectionKey
			 * 轮询监听key，select是阻塞的，accept()也是阻塞的
			 *
			 * 7: selector 继续通过 select() 方法监听事件, 当客户端发送数据给服务端, selector 监听到read事件,
			 * 获取到 SocketChannel 注册时绑定的 selectionKey
			 */
			selector.select();
			System.out.println("有事件发生了...");
			//有客户端请求，被轮询监听到
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				handle(key);
				//删除本次已处理的key，防止下次select重复处理
				iterator.remove();
			}
		}
	}
	
	private static void handle(SelectionKey key) throws IOException {
		if (key.isAcceptable()) {
			System.out.println("有客户端连接事件发生了...");
			/*
			 * 3: selectionKey 通过 channel() 方法可以获取绑定的 ServerSocketChannel
			 */
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			/*
			 * 4: ServerSocketChannel 通过 accept() 方法得到 SocketChannel
			 * 此处accept方法是阻塞的，但是这里因为是发生了连接事件，所以这个方法会马上执行完
			 */
			SocketChannel socketChannel = ssc.accept();
			socketChannel.configureBlocking(false);
			
			/*
			 * 5: 将 SocketChannel 注册到 Selector 上, 关心 read 事件
			 * 6: 注册后返回一个 SelectionKey,, 会和该 SocketChannel 关联
			 */
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
		} else if (key.isReadable()) {
			System.out.println("有客户端数据可读事件发生了...");
			/*
			 * 8: selectionKey 通过 channel() 方法可以获取绑定的 socketChannel
			 */
			SocketChannel socketChannel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			buffer.clear();
			/*
			 * 9: 将 socketChannel 里的数据读取出来
			 */
			int length = socketChannel.read(buffer);
			if (length != -1) {
				System.out.println("读取到客户端发送的数据：" + new String(buffer.array(), 0, length));
			}
			
			ByteBuffer bufferToWrite = ByteBuffer.wrap("HelloClient".getBytes());
			/*
			 * 10: 用 socketChannel 将服务端数据写回客户端
			 */
			socketChannel.write(bufferToWrite);
			
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			socketChannel.close();
		}
	}
}
