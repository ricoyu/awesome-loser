package com.loserico.serialize;

import com.loserico.common.lang.utils.*;
import org.junit.*;
import org.nustaq.serialization.*;

import java.nio.file.*;
import java.time.*;
import java.util.*;

import static com.loserico.json.jackson.JacksonUtils.toJson;

/**
 * <p>
 * Copyright: (C), 2021-01-17 20:41
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FstTest {
	
	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	
	@Test
	public void testSerialize() {
		User rico = new User();
		rico.setId(1);
		rico.setName("rico");
		rico.setBirthday(LocalDate.of(1982, 11, 9));
		
		byte[] data = conf.asByteArray(rico);
		IOUtils.write(Paths.get("D:/data"), data);
		User user = (User) conf.asObject(data);
		System.out.println(toJson(user));
		
		byte[] bytes = IOUtils.readFileAsBytes(Paths.get("D:/data"));
		user = (User) conf.asObject(data);
		System.out.println(toJson(user));
	}
	
	@Test
	public void testSerializeList() {
		User rico = new User();
		rico.setId(1);
		rico.setName("rico");
		rico.setBirthday(LocalDate.of(1982, 11, 9));
		
		User tom = new User();
		tom.setId(2);
		tom.setName("tom");
		tom.setBirthday(LocalDate.of(1992, 11, 9));
		
		List users = new ArrayList();
		users.add(rico);
		users.add(tom);
		
		byte[] bytes = FstUtils.toBytes(users);
		users = (List)FstUtils.toObject(bytes);
		users.forEach(System.out::println);
	}
}
