package com.loserico.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2023-10-31 17:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NioClientHandler implements Runnable {
	
	private String host;
	
	private int port;
	
	private Selector selector;
	
	private SocketChannel sc;
	
	private volatile boolean started;
	
	public NioClientHandler(String host, int port) {
		this.host = host;
		this.port = port;
		
		try {
			this.sc = SocketChannel.open();
			//设置为非阻塞模式
			sc.configureBlocking(false);
			//创建选择器
			this.selector = Selector.open();
			sc.connect(new InetSocketAddress(host, port));
			//注册连接事件
			sc.register(selector, SelectionKey.OP_CONNECT);
			started = true;
			System.out.println("客户端已启动...");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void stop() {
		started = false;
	}
	
	@Override
	public void run() {
		while (started) {
			//物理是否有事件发生, Selector没1秒唤醒一次
			try {
				//System.out.println("客户端等待事件发生...");
				selector.select();
				System.out.println("客户端有事件发生了...");
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					/*
					 * 我们必须先将它从集合中删除, 因为下次该通道上的事件到来时, 它又会被添加到集合中
					 */
					it.remove();
					
					handleInput(key);
				}
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}
	}
	
	private void handleInput(SelectionKey key) {
		if (!key.isValid()) {
			System.out.println("key is invalid...");
			return;
		}
		SocketChannel sc = (SocketChannel) key.channel();
		if (key.isConnectable()) {
			try {
				if (sc.finishConnect()) {
					System.out.println("客户端连接成功...");
					sc.register(selector, SelectionKey.OP_READ);
				} else {
					System.exit(1);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (key.isReadable()) {
			System.out.println("客户端读事件发生...");
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int readBytes = 0;
			try {
				readBytes = sc.read(buffer);
			} catch (IOException e) {
				System.out.println("服务端关闭了连接");
				//通道关闭后也不用关注这个事件了
				key.cancel();
				try {
					sc.close();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				System.exit(1);
			}
			
			if (readBytes < 0) {
				key.cancel();
				try {
					sc.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (readBytes > 0) {
				buffer.flip();
				byte[] bytes = new byte[buffer.remaining()];
				buffer.get(bytes);
				String msg = new String(bytes, UTF_8);
				System.out.println("客户端收到消息: " + msg);
			}
		}
		
	}
	
	public boolean sendMsg(String msg) {
		byte[] bytes = msg.getBytes(UTF_8);
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length).put(bytes);
		//buffer.put(bytes);
		buffer.flip();
		try {
			System.out.println("客户端发送消息: " + msg);
			sc.write(buffer);
			return true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
}
