package com.loserico.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2020-09-11 9:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class LongToByteEncoder extends MessageToByteEncoder<Long> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
		log.info("LongToByteEncoder encode被调用");
		log.info("msg={}", msg);
		out.writeLong(msg);
	}
}
