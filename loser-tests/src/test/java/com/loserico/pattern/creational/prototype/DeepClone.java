package com.loserico.pattern.creational.prototype;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeepClone{

	@Test
	public void testDeepClone() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		User rico = new User();
		rico.setName("yuxuehua");
		rico.setPasswd("123456");

		oos.writeObject(rico);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		User yuxuehua = (User) new ObjectInputStream(bais).readObject();
		System.out.println(yuxuehua);
	}
}
