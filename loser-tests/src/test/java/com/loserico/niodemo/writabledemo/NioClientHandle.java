package com.loserico.niodemo.writabledemo;

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
 * Copyright: (C), 2022-08-04 16:08
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NioClientHandle implements Runnable {
	
	private String host;
	
	private int port;
	
	private volatile boolean started;
	
	private Selector selector;
	
	private SocketChannel socketChannel;
	
	public NioClientHandle(String host, int port) {
		this.host = host;
		this.port = port;
		
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			started = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		started = false;
	}
	
	@Override
	public void run() {
		try {
			doConnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (started) {
			try {
				//无论是否有读写事件发生，selector每隔1s被唤醒一次
				selector.select(1000);
				//获取当前有哪些事件可以使用
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeys.iterator();
				SelectionKey key = null;
				while (iterator.hasNext()) {
					key = iterator.next();
					/*我们必须首先将处理过的 SelectionKey 从选定的键集合中删除。
                    如果我们没有删除处理过的键，那么它仍然会在主集合中以一个激活
                    的键出现，这会导致我们尝试再次处理它。*/
					iterator.remove();
					try {
						handleInput(key);
					} catch (Exception e) {
						if (key != null) {
							key.cancel();
							if (key.channel() != null) {
								key.channel().close();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (selector != null) {
				try {
					selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			//获得关心当前事件的channel
			SocketChannel sc = (SocketChannel) key.channel();
			//连接事件
			if (key.isConnectable()) {
				if (sc.finishConnect()) {
					socketChannel.register(selector, SelectionKey.OP_READ);
				} else {
					System.exit(1);
				}
			}
			//有数据可读事件
			if (key.isReadable()) {
				//创建ByteBuffer，并开辟一个1K的缓冲区
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				//读取请求码流，返回读取到的字节数
				int readBytes = sc.read(buffer);
				//读取到字节，对字节进行编解码
				if (readBytes > 0) {
					//将缓冲区当前的limit设置为position,position=0，
					// 用于后续对缓冲区的读取操作
					buffer.flip();
					//根据缓冲区可读字节数创建字节数组
					byte[] bytes = new byte[buffer.remaining()];
					//将缓冲区可读字节数组复制到新建的数组中
					buffer.get(bytes);
					String result = new String(bytes, UTF_8);
					System.out.println("客户端收到消息：" + result);
				} else if (readBytes < 0) {
					key.cancel();
					sc.close();
				}
			}
		}
	}
	
	private void doWrite(SocketChannel sc, String request) throws IOException {
		//将消息编码为字节数组
		byte[] bytes = request.getBytes(UTF_8);
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		//发送缓冲区的字节数组
		/*关心事件和读写网络并不冲突*/
		sc.write(buffer);
	}
	
	private void doConnect() throws IOException {
		if (socketChannel.connect(new InetSocketAddress("localhost", 12345))) {
			socketChannel.register(selector, SelectionKey.OP_READ);
		} else {
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}
	
	//写数据对外暴露的API
	public void sendMsg(String msg) throws Exception {
		doWrite(socketChannel, msg);
	}
}
