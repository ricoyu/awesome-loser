package com.loserico.common.lang.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Protostuff是基于ProtoBuf实现的, 主要用于Java领域的序列化框架。
 * <ul>
 * <li/>可以直接使用Java类, 而不是.proto文件
 * <li/>它较于protobuf最明显的好处是, 在几乎不损耗性能的情况下做到了不用我们写.proto文件来实现序列化。
 * <li/>Protostuff基于Protobuf。它使用Protobuf的核心概念, 但添加了更易用的API和额外的功能。
 * <li/>Protobuf是面向多种语言的更通用解决方案, 而Protostuff主要针对Java开发者, 提供更简洁的使用方式。
 * <li/>Protobuf需要.proto文件来定义数据结构, 而Protostuff可以直接利用Java类进行序列化。
 * <li/>Protostuff在Protobuf的基础上提供了更多的功能, 如schema-less序列化和数据压缩。
 * </ul>
 * 优势: 对象不需要有默认构造函数也不需要实现Serializble接口<p/>
 * 限制: 反序列化需要提供Class对象<p>
 * <p>
 * Copyright: (C), 2020-10-10 17:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ProtostuffUtils {
	
	/**
	 * 缓存Schema
	 */
	private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();
	
	/**
	 * 序列化方法，把指定对象序列化成字节数组
	 *
	 * @param obj
	 * @param <T>
	 * @return byte[]
	 */
	public static <T> byte[] toBytes(T obj) {
		if (obj == null) {
			log.info("obj is null, return byte[0]");
			return new byte[0];
		}
		
		Class<T> clazz = (Class<T>) obj.getClass();
		Schema<T> schema = getSchema(clazz);
		LinkedBuffer buffer = LinkedBuffer.allocate();
		try {
			return ProtobufIOUtil.toByteArray(obj, schema, buffer);
		} finally {
			buffer.clear();
		}
	}
	
	/**
	 * 反序列化方法，将字节数组反序列化成指定Class类型
	 * @param bytes
	 * @param clazz
	 * @param <T>
	 * @return T
	 */
	public static <T> T toObject(byte[] bytes, Class<T> clazz) {
		Schema<T> schema = getSchema(clazz);
		T obj = schema.newMessage();
		ProtobufIOUtil.mergeFrom(bytes, obj, schema);
		return obj;
	}
	
	private static <T> Schema<T> getSchema(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
		if (schema == null) {
			/*
			 * 这个schema通过RuntimeSchema进行懒创建并缓存
			 * 所以可以一直调用RuntimeSchema.getSchema(), 这个方法是线程安全的
			 */
			schema = RuntimeSchema.getSchema(clazz);
			if (schema != null) {
				schemaCache.put(clazz, schema);
			}
		}
		
		return schema;
	}
}
