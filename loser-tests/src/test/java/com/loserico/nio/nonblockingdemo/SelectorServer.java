package com.loserico.nio.nonblockingdemo;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2022-01-25 14:35
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SelectorServer {
	
	public static void main(String[] args) throws IOException {
		Selector selector = Selector.open();
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 8080));
		
		//将其注册到 Selector 中, 监听 OP_ACCEPT 事件
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		while (true) {
			// 需要不断地去调用 select() 方法获取最新的准备好的通道
			int readyChannels = selector.select();
			if (readyChannels == 0) {
				continue;
			}
			
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				
				if (key.isAcceptable()) { //KEY是16 1<<4
					// 已经有新的连接进来
					SocketChannel socketChannel = serverSocketChannel.accept();
					
					// 有新的连接并不代表这个通道就有数据, 这里将这个新的 SocketChannel 注册到 Selector, 监听OP_READ事件, 等待数据
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ);
				} else if (key.isReadable()) {
					// 有数据可读, 上面一个 if 分支中注册了监听OP_READ事件的 SocketChannel
					SocketChannel socketChannel = (SocketChannel) key.channel();
					ByteBuffer readBuffer = ByteBuffer.allocate(1024);
					int num = socketChannel.read(readBuffer);
					
					if (num > 0) {
						// 处理收到的数据
						log.info("收到数据: {}", new String(readBuffer.array(), UTF_8).trim());
						socketChannel.register(selector, SelectionKey.OP_WRITE);
					} else if (num == -1) {
						// -1 代表连接已经关闭
						socketChannel.close();
					}
				} else if (key.isWritable()) {
					// 给用户返回数据的通道可以进行写操作了
					SocketChannel socketChannel = (SocketChannel) key.channel();
					ByteBuffer writeBuffer = ByteBuffer.wrap("这是返回给客户的数据".getBytes(UTF_8));
					socketChannel.write(writeBuffer);
					
					// 重新注册这个通道, 监听 OP_READ 事件, 客户端还可以继续发送内容过来
					socketChannel.register(selector, SelectionKey.OP_READ);
				}
			}
		}
	}
}
