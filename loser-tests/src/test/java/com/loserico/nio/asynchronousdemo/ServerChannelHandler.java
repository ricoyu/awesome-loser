package com.loserico.nio.asynchronousdemo;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2022-01-25 15:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ServerChannelHandler implements CompletionHandler<Integer, Attachment> {
	
	@Override
	public void completed(Integer result, Attachment attachment) {
		if (attachment.isReadMode()) {
			//读取来自客户端的数据
			ByteBuffer buffer = attachment.getBuffer();
			buffer.flip();
			//byte[] data = new byte[buffer.limit()];
			//buffer.get(data);
			String msg = new String(buffer.array(), UTF_8).trim();
			log.info("收到来自客户端的数据: {}", msg);
			
			//响应客户端请求, 返回数据
			buffer.clear();
			buffer.put("Response from server!".getBytes(UTF_8));
			attachment.setReadMode(false);
			buffer.flip();
			// 写数据到客户端也是异步
			attachment.getSocketChannel().write(buffer, attachment, this);
		} else {
			/*
			 * 到这里，说明往客户端写数据也结束了，有以下两种选择:
			 * 1. 继续等待客户端发送新的数据过来
			 *    attachment.setReadMode(true);
			 *    attachment.getBuffer().clear();
			 *    attachment.getClient().read(att.getBuffer(), att, this);
			 * 
			 * 2. 既然服务端已经返回数据给客户端，断开这次的连接
			 */
			try {
				attachment.getSocketChannel().close();
			} catch (IOException e) {
				log.error("", e);
			}
		}
	}
	
	@Override
	public void failed(Throwable exc, Attachment attachment) {
		log.info("连接断开!");
	}
}
