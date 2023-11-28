package com.loserico.nio.server;

import com.loserico.common.lang.utils.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2023-10-31 16:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NioServerHandler implements Runnable {
	
	private volatile boolean started;
	
	private ServerSocketChannel serverSocketChannel;
	
	private Selector selector;
	
	/**
	 * 构造方法
	 *
	 * @param port 指定端口号
	 */
	public NioServerHandler(int port) {
		try {
			/**
			 * 创建选择器
			 */
			this.selector = Selector.open();
			this.serverSocketChannel = ServerSocketChannel.open();
			//设置通道为非阻塞模式
			serverSocketChannel.configureBlocking(false);
			//绑定端口 backlog设为1024
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			started = true;
			System.out.println("服务器已启动，端口号：" + port);
			//注册accept事件, 表示我们关注的是客户端连接事件
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public void run() {
		while (started) {
			/*
			 * 这是一个阻塞方法, 一直等到有事件发生才会返回
			 */
			try {
				//System.out.println("不断轮询事件...");
				selector.select();
				System.out.println("有事件发生了...");
				//selector.select(1000); //阻塞1000毫秒
				//selector.selectNow(); // 非阻塞版本, 立即返回
				//返回一个事件集
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					//移除当前已经处理的事件, 不然下次循环还会继续处理
					iterator.remove();
					//处理事件
					handleEvent(selectionKey);
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	private void handleEvent(SelectionKey key) throws IOException, InterruptedException {
		if (!key.isValid()) {
			System.out.println("key is invalid");
			return;
		}
		if (key.isAcceptable()) {
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			SocketChannel sc = ssc.accept();
			System.out.println("接收到客户端连接: " + sc.getRemoteAddress() + ":" + sc.socket().getPort());
			//设置为非阻塞模式
			sc.configureBlocking(false);
			//注册读事件
			sc.register(selector, SelectionKey.OP_READ);
		} else if (key.isReadable()) { //处理对端发送的消息
			System.out.println("处理读事件");
			SocketChannel sc = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			
			int readBytes;
			try {
				readBytes = sc.read(buffer);
			}catch (IOException e) {
				System.out.println("客户端关闭了连接");
				//通道关闭后也不用关注这个事件了
				key.cancel();
				sc.close();
				return;
			}
			String msg = null;
			
			if (readBytes > 0) {
				buffer.flip();
				byte[] bytes = new byte[buffer.remaining()];
				buffer.get(bytes);
				msg = new String(bytes, UTF_8);
			}
			//通道关闭了或者发生了异常
			else if (readBytes < 0) {
				System.out.println("客户端关闭了连接");
				//通道关闭后也不用关注这个事件了
				key.cancel();
				sc.close();
				return;
			}
			
			System.out.println("服务端收到消息: " + msg);
			TimeUnit.MILLISECONDS.sleep(100);
			IOUtils.slientUserInput("请输入数据:", (input) -> {
				byte[] bytes = input.getBytes(UTF_8);
				ByteBuffer responseBuffer = ByteBuffer.allocate(bytes.length);
				responseBuffer.put(bytes);
				//接下来sc从buffer中读取数据, 所以要先flip一下
				responseBuffer.flip();
				//写回客户端
				System.out.println("服务端发送消息: " + input);
				try {
					sc.write(responseBuffer);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
			//注册写事件
			//sc.register(selector, SelectionKey.OP_WRITE);
		}
	}
	
	
	public void stop() {
		started = false;
	}
}
