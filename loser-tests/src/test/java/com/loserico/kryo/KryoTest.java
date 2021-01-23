package com.loserico.kryo;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.loserico.serialize.User;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * <p>
 * Copyright: (C), 2021-01-19 20:23
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class KryoTest {
	
	@SneakyThrows
	@Test
	public void test1() {
		User obj = new User();
		obj.setName("张三");
		
		Kryo kryo = new Kryo();
		Output output = new Output(new FileOutputStream("D:/file.bin"));
		kryo.writeClassAndObject(output, obj);
		output.close();
		
		//读取
		Input input = new Input(new FileInputStream("D:/file.bin"));
		User user = (User) kryo.readClassAndObject(input);
		input.close();
		
		System.out.println(user.getName());
	}
}
