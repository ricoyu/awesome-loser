package com.loserico.common.lang.utils;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * Kryo序列化/反序列化
 * <p>
 * 优势: 不需要实现Serializble接口; 反序列化不需要提供Class对象
 * 限制: 对象需要有默认构造函数; 只能在Java生态圈用, 不能跨语言
 *
 * <p>
 * Copyright: (C), 2021-01-19 20:35
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class KryoUtils {
	
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	private static final Kryo kryo = new Kryo();
	
	/**
	 * 不要轻易改变这里的配置, 更改之后, 序列化的格式就会发生变化
	 * 上线的同时就必须清除 Redis 里的所有缓存, 否则那些缓存再回来反序列化的时候就会报错
	 *
	 */
	static {
		/*
		 * 支持对象循环引用(否则会栈溢出)
		 * 默认值就是 true, 不要改变这个配置
		 */
		kryo.setReferences(true);
		
		/*
		 * 不强制要求注册类
		 * 注册行为无法保证多个 JVM 内同一个类的注册编号相同
		 * 而且业务系统中大量的 Class 也难以一一注册
		 * 默认值就是 false
		 */
		kryo.setRegistrationRequired(false);
		
		//Fix the NPE bug when deserializing Collections.
		kryo.getInstantiatorStrategy();
		
	}
	
	/**
	 * 将对象【及类型】序列化为字节数组
	 *
	 * @param obj 任意对象
	 * @param <T> 对象的类型
	 * @return 序列化后的字节数组
	 */
	public static <T> byte[] toBytes(T obj) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Output output = new Output(byteArrayOutputStream);
		
		kryo.writeClassAndObject(output, obj);
		output.flush();
		
		return byteArrayOutputStream.toByteArray();
	}
	
	/**
	 * 将字节数组反序列化为原对象
	 *
	 * @param bytes toBytes 方法序列化后的字节数组
	 * @param <T>   原对象的类型
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toObject(byte[] bytes) {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		Input input = new Input(byteArrayInputStream);
		
		return (T) kryo.readClassAndObject(input);
	}
	
	/**
	 * 将对象【及类型】序列化为 String
	 * 利用了 Base64 编码
	 *
	 * @param obj 任意对象
	 * @param <T> 对象的类型
	 * @return 序列化后的字符串
	 */
	public static <T> String writeToString(T obj) {
		return Base64.getUrlEncoder().encodeToString(toBytes(obj));
	}
	
	/**
	 * 将 String 反序列化为原对象
	 * 利用了 Base64 编码
	 *
	 * @param str writeToString 方法序列化后的字符串
	 * @param <T> 原对象的类型
	 * @return 原对象
	 */
	public static <T> T readFromString(String str) {
		return toObject(Base64.getUrlDecoder().decode(str));
	}
}
