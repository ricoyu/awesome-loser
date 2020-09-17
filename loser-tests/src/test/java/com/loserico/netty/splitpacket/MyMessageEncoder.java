package com.loserico.netty.splitpacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2020-09-16 9:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class MyMessageEncoder extends MessageToByteEncoder<MyMessageProtocol> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, MyMessageProtocol msg, ByteBuf out) throws Exception {
		log.info("MyMessageEncoder encode 方法被调用");
		out.writeInt(msg.getLen());
		out.writeBytes(msg.getContent());
	}
}
