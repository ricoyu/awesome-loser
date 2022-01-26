package com.loserico.nio.asynchronousdemo;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2022-01-25 16:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ClientChannelHandler implements CompletionHandler<Integer, Attachment> {
	
	@Override
	public void completed(Integer result, Attachment attachment) {
		ByteBuffer buffer = attachment.getBuffer();
		if (attachment.isReadMode()) {
			// 读取来自服务端的数据
			buffer.flip();
			String msg = new String(buffer.array(), UTF_8).trim();
			log.info("收到来自服务端的响应数据: {}", msg);
			
			/*
			 * 接下来, 有以下两种选择
			 *
			 * 1. 向服务端发送新的数据
			 * attachment.setReadMode(false);
			 * buffer.clear();
			 * String newMsg = "new message from client";
			 * byte[] data = newMsg.getBytes(Charset.forName("UTF-8"));
			 * buffer.put(data);
			 * buffer.flip();
			 * attachment.getClient().write(buffer, att, this);
			 *
			 * 2. 关闭连接
			 */
			try {
				attachment.getSocketChannel().close();
			} catch (IOException e) {
				log.error("", e);
			}
		} else {
			//写操作完成后, 会进到这里
			attachment.setReadMode(true);
			buffer.clear();
			attachment.getSocketChannel().read(buffer, attachment, this);
		}
	}
	
	@Override
	public void failed(Throwable e, Attachment attachment) {
		log.error("服务器无响应", e);
	}
}
