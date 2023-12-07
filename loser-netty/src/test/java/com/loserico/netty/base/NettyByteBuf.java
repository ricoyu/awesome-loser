package com.loserico.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2020-09-02 11:28
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class NettyByteBuf {
	
	public static void main(String[] args) {
		/*
		 * 创建byteBuf对象, 该对象内部包含一个字节数组byte[10]
		 * 通过readerindex, writerIndex和capacity, 将buffer分成三个区域
		 * 已经读取的区域: [0, readerindex)
		 * 可读取的区域:   [readerindex, writerIndex)
		 * 可写的区域:     [writerIndex, capacity)
		 */
		ByteBuf buf = Unpooled.buffer(10);
		log.info("byteBuf={}", buf);
		
		for (int i = 0; i < 8; i++) {
			buf.writeInt(i);
			log.info("byteBuf={}", buf);
		}
		log.info("byteBuf={}", buf);
		
		for (int i = 0; i < 5; i++) {
			log.info(buf.getByte(i) + ""); //调getByte方法, 其readIndex不会变
		}
		log.info("byteBuf={}", buf);
		
		for (int i = 0; i < 5; i++) {
			log.info(buf.readByte() + "");//调readByte方法, 其readIndex会增长
		}
		log.info("byteBuf={}", buf);
		
		//用Unpooled工具类创建ByteBuf
		ByteBuf byteBuf = Unpooled.copiedBuffer("hello rico", UTF_8);
		//使用相关的方法
		if (byteBuf.hasArray()) {
			byte[] content = byteBuf.array();
			
			//将 content 转成字符串
			log.info(new String(content, UTF_8));
			log.info("byteBuf={}", byteBuf);
			log.info("{}", byteBuf.readerIndex());
			log.info("{}", byteBuf.writerIndex());
			log.info("{}", byteBuf.capacity());
			
			//获取数组0这个位置的字符h的ascii码，h=104
			log.info("{}", byteBuf.getByte(0));
			
			//可读的字节数  12
			int len = byteBuf.readableBytes();
			log.info("len={}", len);
			
			//使用for取出各个字节
			for (int i = 0; i < len; i++) {
				log.info("{}", (char) byteBuf.getByte(i));
			}
			
			//范围读取
			log.info("{}", byteBuf.getCharSequence(0, 6, UTF_8));
			log.info("{}", byteBuf.getCharSequence(6, 6, UTF_8));
		}
	}
}
