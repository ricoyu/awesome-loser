package com.loserico.niodemo.writabledemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2022-08-04 15:24
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NioServerHandleWriteable implements Runnable {
	
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private volatile boolean started;
	
	public NioServerHandleWriteable(int port) {
		try {
			//创建选择器
			this.selector = Selector.open();
			//打开监听通道
			this.serverSocketChannel = ServerSocketChannel.open();
			//如果为 true，则此通道将被置于阻塞模式；
			// 如果为 false，则此通道将被置于非阻塞模式
			this.serverSocketChannel.configureBlocking(false);
			//绑定端口 backlog设为1024
			serverSocketChannel.socket()
					.bind(new InetSocketAddress("192.168.2.3", port), 1024);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			started = true;
			System.out.println("服务器已启动，端口号：" + port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	@Override
	public void run() {
		while (started) {
			//阻塞,只有当至少一个注册的事件发生的时候才会继续.
			try {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				SelectionKey key = null;
				System.out.println("--------由线程 " + Thread.currentThread().getName() + " 处理-------");
				while (iterator.hasNext()) {
					key = iterator.next();
					iterator.remove();
					handleInput(key);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void handleInput(SelectionKey key) throws IOException {
		System.out.println("当前通道的事件：" + key.interestOps());
		if (key.isValid()) {
			//处理新接入的请求消息
			if (key.isAcceptable()) {
				//获得关心当前事件的channel
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				//通过ServerSocketChannel的accept创建SocketChannel实例
				//完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
				SocketChannel sc = ssc.accept();
				System.out.println("socket channel 建立连接");
				//设置为非阻塞的
				sc.configureBlocking(false);
				//连接已经完成了，可以开始关心读事件了
				sc.register(selector, SelectionKey.OP_READ);
			}
			if (key.isReadable()) {
				System.out.println("socket channel 数据准备完成，可以去读");
				SocketChannel sc = (SocketChannel) key.channel();
				//创建ByteBuffer，并开辟一个1K的缓冲区
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				//读取请求码流，返回读取到的字节数
				int readBytes = sc.read(buffer);
				//读取到字节，对字节进行编解码
				if (readBytes > 0) {
					//将缓冲区当前的limit设置为position,position=0，用于后续对缓冲区的读取操作
					buffer.flip();
					//根据缓冲区可读字节数创建字节数组
					byte[] bytes = new byte[buffer.remaining()];
					//将缓冲区可读字节数组复制到新建的数组中
					buffer.get(bytes);
					String message = new String(bytes, UTF_8);
					System.out.println("服务器收到消息：" + message);
					//处理数据
					String result = response(message);
					//发送应答消息
					doWrite(sc, result);
				} else if (readBytes < 0) {
					//链路已经关闭，释放资源
					sc.close();
				}
			}
			if (key.isWritable()) {
				System.out.println("socket channel 缓冲为空，准备写");
				SocketChannel sc = (SocketChannel) key.channel();
				//这个ByteBuffer是doWrite方法里面register时候添加进去的attachment
				ByteBuffer buffer = (ByteBuffer) key.attachment();
				if (buffer != null && buffer.hasRemaining()) {
					int count = sc.write(buffer);
					System.out.println("write :" + count
							+ "byte, remaining:" + buffer.hasRemaining());
				} else {
					key.attach(null);
					/*取消对写的注册，只关注读*/
					System.out.println("没有数据需要写，取消写注册");
					key.interestOps(SelectionKey.OP_READ);
				}
			}
		}
	}
	
	private void doWrite(SocketChannel sc, String response) throws ClosedChannelException {
		byte[] bytes = response.getBytes(UTF_8);
		//根据数组容量创建ByteBuffer
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		sc.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, buffer);
	}
	
	public void stop() {
		started = false;
	}
	
	public static String response(String msg) {
		return "Hello," + msg + ",Now is " + new java.util.Date(
				System.currentTimeMillis()).toString();
	}
}
