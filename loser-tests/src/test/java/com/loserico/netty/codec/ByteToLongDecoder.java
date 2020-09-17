package com.loserico.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2020-09-11 9:16
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ByteToLongDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		log.info("ByteToLongDecoder decode 被调用");
		// 因为 long 8个字节, 需要判断有8个字节，才能读取一个long
		if (in.readableBytes() >= 8) {
			out.add(in.readLong());
		}
	}
}
