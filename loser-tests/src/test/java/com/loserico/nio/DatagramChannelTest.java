package com.loserico.nio;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java
 * NIO中的DatagramChannel是一个能收发UDP包的通道。因为UDP是无连接的网络协议，所以不能像其它通道那样读取和写入。它发送和接收的是数据包。
 * 
 * @author Rico Yu
 * @since 2016-12-11 13:02
 * @version 1.0
 *
 */
public class DatagramChannelTest {
	private static final Logger logger = LoggerFactory.getLogger(DatagramChannelTest.class);

	/*
	 * 这个例子打开的 DatagramChannel可以在UDP端口9999上接收数据包。
	 */
	@Test
	public void testOpen() {
		try (DatagramChannel datagramChannel = DatagramChannel.open()) {
			datagramChannel.socket().bind(new InetSocketAddress(9999));

			/*
			 * receive()方法会将接收到的数据包内容复制到指定的Buffer. 如果Buffer容不下收到的数据，多出的数据将被丢弃。
			 */
			ByteBuffer buffer = ByteBuffer.allocate(48);
			datagramChannel.receive(buffer);
		} catch (IOException e) {
			logger.error("msg", e);
		}
	}

	/**
	 * 这个例子发送一串字符到”jenkov.com”服务器的UDP端口80。
	 * 因为服务端并没有监控这个端口，所以什么也不会发生。也不会通知你发出的数据包是否已收到，因为UDP在数据传送方面没有任何保证。
	 */
	@Test
	public void testSend() {
		String newData = "New String to write to file..." + System.currentTimeMillis();
		try (DatagramChannel datagramChannel = DatagramChannel.open()) {
			datagramChannel.socket().bind(new InetSocketAddress(9999));

			/*
			 * receive()方法会将接收到的数据包内容复制到指定的Buffer. 如果Buffer容不下收到的数据，多出的数据将被丢弃。
			 */
			ByteBuffer buffer = ByteBuffer.allocate(48);
			buffer.put(newData.getBytes());
			buffer.flip();

			int bytesSent = datagramChannel.send(buffer, new InetSocketAddress("192.168.1.7", 80));
		} catch (IOException e) {
			logger.error("msg", e);
		}
	}

	/**
	 * 连接到特定的地址
	 * 
	 * 可以将DatagramChannel“连接”到网络中的特定地址的。由于UDP是无连接的，连接到特定地址并不会像TCP通道那样创建一个真正的连接。而是锁住DatagramChannel
	 * ，让其只能从特定地址收发数据。
	 */
	@Test
	public void testConnect() {
		try (DatagramChannel datagramChannel = DatagramChannel.open()) {
			datagramChannel.connect(new InetSocketAddress("192.168.1.7", 80));
			ByteBuffer buffer = ByteBuffer.allocate(48);

			// ...

			/*
			 * 当连接后，也可以使用read()和write()方法，就像在用传统的通道一样。只是在数据传送方面没有任何保证。这里有几个例子：
			 */
			int bytesRead = datagramChannel.read(buffer);
			int bytesWrite = datagramChannel.write(buffer);
		} catch (IOException e) {
			logger.error("msg", e);
		}
	}
}
