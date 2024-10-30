package com.loserico.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static java.nio.channels.SelectionKey.OP_CONNECT;
import static java.nio.channels.SelectionKey.OP_READ;

/**
 * <p>
 * Copyright: (C), 2019/12/21 17:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NioClient {
	
	// 通道管理器
	private Selector selector;
	
	public static void main(String[] args) throws IOException {
		NioClient nioClient = new NioClient();
		nioClient.initClient("127.0.0.1", 8080);
		nioClient.connect();
	}
	
	/**
	 * 获得一个Socket通道，并对该通道做一些初始化的工作
	 *
	 * @param ip
	 * @param port
	 */
	public void initClient(String ip, int port) throws IOException {
		// 获得一个Socket通道
		SocketChannel socketChannel = SocketChannel.open();
		// 设置通道为非阻塞
		socketChannel.configureBlocking(false);
		// 获得一个通道管理器
		this.selector = Selector.open();
		
		/*
		 * 客户端连接服务器, 其实方法执行并没有实现连接, 需要在listen()方法中调用channel.finishConnect();才能完成连接
		 */
		socketChannel.connect(new InetSocketAddress(ip, port));
		// 将通道管理器和该通道绑定, 并为该通道注册SelectionKey.OP_CONNECT事件
		socketChannel.register(this.selector, OP_CONNECT);
	}
	
	/**
	 * 采用轮询的方式监听selector上是否有需要处理的事件, 如果有, 则进行处理
	 */
	public void connect() throws IOException {
		// 轮询访问selector
		while (true) {
			/*
			 * 选择一组可以进行I/O操作的事件, 放在selector中, 客户端的该方法不会阻塞, 这里和服务端的方法不一样,
			 * 查看api注释可以知道, 当至少一个通道被选中时, selector的wakeup方法被调用, 方法返回, 而对于客户端来说, 通道一直是被选中的
			 */
			selector.select();
			// 获得selector中选中的项的迭代器
			Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				// 删除已选的key,以防重复处理
				iterator.remove();
				// 连接事件发生
				if (key.isConnectable()) {
					SocketChannel socketChannel = (SocketChannel) key.channel();
					// 如果正在连接, 则完成连接
					if (socketChannel.isConnectionPending()) {
						socketChannel.finishConnect();
					}
					//设置成非阻塞
					socketChannel.configureBlocking(false);
					//在这里可以给服务端发送信息
					ByteBuffer buffer = ByteBuffer.wrap("HelloServer".getBytes());
					socketChannel.write(buffer);
					
					//在和服务端连接成功之后, 为了可以接收到服务端的信息, 需要给通道设置读的权限
					socketChannel.register(this.selector, OP_READ); // 注册可读的事件
				} else if (key.isReadable()) {
					read(key);
				}
			}
		}
	}
	
	/**
	 * 处理读取服务端发来的信息的事件
	 *
	 * @param key
	 */
	private void read(SelectionKey key) throws IOException {
		/*
		 * 和服务端的read方法一样
		 * 服务器可读取消息:得到事件发生的Socket通道
		 */
		SocketChannel socketChannel = (SocketChannel) key.channel();
		// 创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int len = socketChannel.read(buffer);
		if (len != -1) {
			System.out.println("客户端收到信息：" + new String(buffer.array(), 0, len));
		}
	}
}
