package com.loserico.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * 这个代码演示了如何使用Netty的ByteBuf进行基本的读写操作。
 * 首先，创建了一个大小为10字节的ByteBuf。
 * 然后，向ByteBuf中写入字符串"abc"的字节表示，观察写索引的变化。
 * 接下来，从ByteBuf中读取一个字节，观察读索引的变化。
 * 再次写入字符串"def"的字节表示，观察写索引的变化。
 * 最后，从ByteBuf中读取3个字节，观察读索引的变化。
 * 在每个步骤之后，都打印了ByteBuf的读写索引状态，以展示索引是如何随着读写操作而变化的。这有助于理解Netty ByteBuf中读写索引的工作原理。 
 * <p>
 * Copyright: Copyright (c) 2023-12-22 14:06
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ByteBufDemo {
	
	@Test
	public void testByteBufReadWriteIndex() {
		// 创建一个ByteBuf
		ByteBuf buffer = Unpooled.buffer(10);
		System.out.println("初始状态：readIndex=" + buffer.readerIndex() + ", writeIndex=" + buffer.writerIndex()); // 初始状态：readIndex=0, writeIndex=0
		
		// 写入一些数据到ByteBuf
		buffer.writeBytes("abc".getBytes(StandardCharsets.UTF_8));
		System.out.println("写入'abc'后：readIndex=" + buffer.readerIndex() + ", writeIndex=" + buffer.writerIndex()); // 写入'abc'后：readIndex=0, writeIndex=3
		
		// 读取一个字节
		buffer.readByte();
		System.out.println("读取一个字节后：readIndex=" + buffer.readerIndex() + ", writeIndex=" + buffer.writerIndex()); // 读取一个字节后：readIndex=1, writeIndex=3
		
		// 再写入一些数据
		buffer.writeBytes("def".getBytes(StandardCharsets.UTF_8));
		System.out.println("写入'def'后：readIndex=" + buffer.readerIndex() + ", writeIndex=" + buffer.writerIndex()); // 写入'def'后：readIndex=1, writeIndex=6
		
		// 再次读取一些数据
		byte[] readBytes = new byte[3];
		buffer.readBytes(readBytes);
		System.out.println("再次读取3个字节后：readIndex=" + buffer.readerIndex() + ", writeIndex=" + buffer.writerIndex()); // 再次读取3个字节后：readIndex=4, writeIndex=6
	}
	
	@Test
	public void testByteBufToStringWithReadWriteIndex() {
		String packet = "<STX>11;MC001;1;TAMR;evt=LIF;<ETX>";
		// 创建一个ByteBuf
		ByteBuf buffer = Unpooled.buffer(10);
		
		buffer = Unpooled.buffer(10);
		System.out.println("初始状态：readIndex=" + buffer.readerIndex() + ", writeIndex=" + buffer.writerIndex()); // 初始状态：readIndex=0, writeIndex=0
		buffer.writeBytes(packet.getBytes(StandardCharsets.US_ASCII));
		System.out.println("写入报文后：readIndex=" + buffer.readerIndex() + ", writeIndex=" + buffer.writerIndex());// 写入报文后：readIndex=0, writeIndex=34
		String str = buffer.toString(StandardCharsets.US_ASCII);
		System.out.println("从Buffer中拿到的字符串: " + str); //从Buffer中拿到的字符串: <STX>11;MC001;1;TAMR;evt=LIF;<ETX>
		System.out.println("toString后：readIndex=" + buffer.readerIndex() + ", writeIndex=" + buffer.writerIndex()); //toString后：readIndex=0, writeIndex=34
		buffer.writeBytes("<STX>11;MC001;1;TAMR;evt=LIF;<ETX>".getBytes(StandardCharsets.US_ASCII));
		System.out.println("再次写入后：readIndex=" + buffer.readerIndex() + ", writeIndex=" + buffer.writerIndex()); //再次写入后：readIndex=0, writeIndex=68
		str = buffer.toString(StandardCharsets.US_ASCII);
		System.out.println("从Buffer中拿到的字符串: " + str); //从Buffer中拿到的字符串: <STX>11;MC001;1;TAMR;evt=LIF;<ETX><STX>11;MC001;1;TAMR;evt=LIF;<ETX>
	}
}
