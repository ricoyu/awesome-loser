package com.loserico.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * -Xms10M -Xmx10M -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=d:/
 * <p>
 * Copyright: (C), 2019 2019-09-21 21:25
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OOMHeapDumpOnOutOfMemory {

	public static List<Object> list = new ArrayList<>();

	public static void main(String[] args) {
		List<Object> list = new ArrayList<>();
		int i = 0;
		int j = 0;
		while (true) {
			list.add(new User(i++, UUID.randomUUID().toString()));
			new User(j--, UUID.randomUUID().toString());
		}
	}
}

class User {
	private int id;
	private String uuid;

	public User(int id, String uuid) {
		this.id = id;
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "id: " + this.id + ", uuid: " + this.uuid;
	}
}
