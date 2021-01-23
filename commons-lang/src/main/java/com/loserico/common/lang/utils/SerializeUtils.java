package com.loserico.common.lang.utils;

import org.slf4j.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 传统JDK的序列化/反序列化
 * <p>
 * Copyright: Copyright (c) 2021-01-17 20:59
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class SerializeUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(SerializeUtils.class);
	
	public static byte[] serialize(Object object) {
		ObjectOutputStream objectOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			byte[] bytes = byteArrayOutputStream.toByteArray();
			return bytes;
		} catch (Exception e) {
			logger.error("序列化对象异常[" + e.getMessage() + "]", e);
		} finally {
			close(objectOutputStream, byteArrayOutputStream);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(bytes);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			return (T) objectInputStream.readObject();
		} catch (Exception e) {
			logger.error("反序列化对象异常[" + e.getMessage() + "]", e);
		} finally {
			close(objectInputStream, byteArrayInputStream);
		}
		return null;
	}
	
	private static void close(ObjectOutputStream objectOutputStream, ByteArrayOutputStream byteArrayOutputStream) {
		try {
			if (byteArrayOutputStream != null) {
				byteArrayOutputStream.close();
			}
			if (objectOutputStream != null) {
				objectOutputStream.close();
			}
		} catch (Exception e) {
			logger.error("关闭IO资源异常[" + e.getMessage() + "]", e);
		}
	}
	
	private static void close(ObjectInputStream objectInputStream, ByteArrayInputStream byteArrayInputStream) {
		try {
			if (objectInputStream != null) {
				objectInputStream.close();
			}
			if (byteArrayInputStream != null) {
				byteArrayInputStream.close();
			}
		} catch (Exception e) {
			logger.error("关闭IO资源异常[" + e.getMessage() + "]", e);
		}
	}
}
