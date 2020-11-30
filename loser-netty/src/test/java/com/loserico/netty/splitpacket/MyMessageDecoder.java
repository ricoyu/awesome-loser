package com.loserico.netty.splitpacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2020-09-16 9:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class MyMessageDecoder extends ByteToMessageDecoder {
	
	private int length;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		log.info("MyMessageDecoder decode 被调用");
		//需要将得到二进制字节码-> MyMessageProtocol 数据包(对象)
		System.out.println(in);
		
		//int型是4字节
		if (in.readableBytes() >= 4) {
			if (length == 0) {
				length = in.readInt();
			}
			
			if (in.readableBytes() < length) {
				log.info("当前可读数据不够, 继续等待...");
				return;
			}
			//直接读取length个字节长度的数据
			byte[] content = new byte[length];
			in.readBytes(content);
			//封装成MyMessageProtocol对象, 传递到下一个handler业务处理
			MyMessageProtocol messageProtocol = new MyMessageProtocol();
			messageProtocol.setLen(length);
			messageProtocol.setContent(content);
			out.add(messageProtocol);
		}
		
		length = 0;
	}
}
