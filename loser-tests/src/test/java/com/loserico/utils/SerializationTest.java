package com.loserico.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializationTest {
	private static final Logger log = LoggerFactory.getLogger(SerializationTest.class);

	public static byte[] serialize(Object obj) {
		if (obj == null) {
			return new byte[0];
		}
		try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
				ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut)) {
			objectOut.writeObject(obj);
			return byteArrayOut.toByteArray();
		} catch (IOException e) {
			log.error("msg", e);
			return new byte[0];
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] objectBytes, Class<T> type) {
		if (objectBytes == null || objectBytes.length == 0) {
			return null;
		}

		try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(objectBytes);
				ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn)) {
			return (T) objectIn.readObject();
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
