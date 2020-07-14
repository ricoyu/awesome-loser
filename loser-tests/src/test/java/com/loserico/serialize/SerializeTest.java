package com.loserico.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import org.junit.Test;

/**
 * https://github.com/EsotericSoftware/kryo#using-standard-java-serialization
 * https://www.javacodegeeks.com/2015/09/built-in-serialization-techniques.html
 * https://github.com/magro/kryo-serializers
 * <p>
 * Copyright: Copyright (c) 2018-03-11 13:54
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class SerializeTest {

	@Test
	public void testJavaBuiltinSerialize() throws IOException {
		User rico = new User();
		rico.setId(1);
		rico.setName("rico");
		rico.setBirthday(LocalDate.of(1982, 11, 9));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(os);
		out.writeObject(rico);
		byte[] bytes = os.toByteArray();
		System.out.println(bytes);
		System.out.println(bytes.length);

	}
	
	@Test
	public void testStringGetBytes() throws UnsupportedEncodingException {
		String titleName = "业绩提成详细数据全部.xls";
		String name = new String(titleName.getBytes("GB2312"), "ISO8859-1");
		System.out.println(name);
	}

}
