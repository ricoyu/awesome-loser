package com.loserico.netty.utils;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Netty ByteBuf工具类, 处理了一些ByteBuf曾经踩坑的操作, 避免再次踩坑
 * <p>
 * Copyright: (C), 2023-12-22 14:28
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ByteBufUtils {
	private static final Logger log = LoggerFactory.getLogger(ByteBufUtils.class);
	
	/**
	 * 将ByteBuf转换成byte数组, 注意不能直接用byteBuf.array()来获得
	 * @param byteBuf
	 * @return byte[]
	 */
	public static byte[] toBytes(Object byteBuf) {
		if (byteBuf == null || !(byteBuf instanceof ByteBuf)) {
			log.warn("参数byteBuf不是一个ByteBuf对象, 直接返回null");
			return null;
		}
		
		ByteBuf buf = (ByteBuf) byteBuf;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		return bytes;
	}
	
	/**
	 * 用UTF-8将ByteBuf转换成字符串, 注意不能直接用byteBuf.toString()来获得, 
	 * 这个toString()不会更新readerIndex, 会导致下次读取的时候, 读到的是不断累积的数据
	 * @param byteBuf
	 * @return String
	 */
	public static String toString(Object byteBuf) {
		if (byteBuf == null || !(byteBuf instanceof ByteBuf)) {
			log.warn("参数byteBuf不是一个ByteBuf对象, 直接返回null");
			return null;
		}
		
		ByteBuf buf = (ByteBuf) byteBuf;
		return buf.toString(US_ASCII);
	}
	
	/**
	 * 用指定的字符集将ByteBuf转换成字符串, 如果charset为null, 则默认使用UTF-8, 
	 * 注意不能直接用byteBuf.toString()来获得, 
	 * 这个toString()不会更新readerIndex, 会导致下次读取的时候, 读到的是不断累积的数据
	 * @param byteBuf
	 * @param charset
	 * @return String
	 */
	public static String toString(Object byteBuf, Charset charset) {
		if (charset == null) {
			log.warn("charset为null, 默认使用UTF-8");
			charset = UTF_8;
		}
		
		if (byteBuf == null || !(byteBuf instanceof ByteBuf)) {
			log.warn("参数byteBuf不是一个ByteBuf对象, 直接返回null");
			return null;
		}
		
		ByteBuf buf = (ByteBuf) byteBuf;
		return buf.toString(charset);
	}
	
	/**
	 * 用指定的字符集将ByteBuf转换成字符串, 如果charset为null, 或者不是合法的字符集, 则默认使用UTF-8
	 * 注意不能直接用byteBuf.toString()来获得, 
	 * 这个toString()不会更新readerIndex, 会导致下次读取的时候, 读到的是不断累积的数据
	 * @param byteBuf
	 * @param charset
	 * @return String
	 */
	public static String toString(Object byteBuf, String charset) {
		if (StringUtils.isBlank(charset)) {
			charset = "UTF-8";
		}
		
		Charset charset1 = null;
		try {
			charset1 = Charset.forName(charset);
		} catch (UnsupportedCharsetException e) {
			charset1 = UTF_8;
		}
		
		if (byteBuf == null || !(byteBuf instanceof ByteBuf)) {
			log.warn("参数byteBuf不是一个ByteBuf对象, 直接返回null");
			return null;
		}
		
		ByteBuf buf = (ByteBuf) byteBuf;
		
		return buf.toString(charset1);
	}
}
