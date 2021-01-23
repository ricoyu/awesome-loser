package com.loserico.common.lang.utils;

import org.nustaq.serialization.FSTConfiguration;

/**
 * 基于Fst实现的基于字节码的序列化/反序列化工具, 传统Java序列化/反序列化的替代方案
 * 限制: 对象必须实现Serializble接口
 * 优势: 对象不需要有默认构造函数
 * 
 * https://github.com/RuedigerMoeller/fast-serialization/wiki
 * 
 * <p>
 * Copyright: (C), 2021-01-17 20:48
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class FstUtils {
	
	private static FSTConfiguration fst = FSTConfiguration.createDefaultConfiguration();
	
	/**
	 * 将对象序列化为byte[]
	 * 限制: 对象必须实现Serializble接口
	 * 
	 * @param obj
	 * @return byte[]
	 */
	public static byte[] toBytes(Object obj) {
		if (obj == null) {
			return null;
		}
		
		return fst.asByteArray(obj);
	}
	
	/**
	 * 将byte[]反序列化为Java对象
	 * @param bytes
	 * @param <T>
	 * @return T
	 */
	public static <T> T toObject(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		
		return (T)fst.asObject(bytes);
	}
}
