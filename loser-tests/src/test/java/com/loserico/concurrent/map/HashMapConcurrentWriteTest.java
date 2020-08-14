package com.loserico.concurrent.map;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020-8-3 0003 16:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class HashMapConcurrentWriteTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		
		Map<String, Object> map = new HashMap<>();
		new Thread(() -> {
			int i = 0;
			while (i < 10000) {
				log.info(">>>>>> put key: {}, value: {}", "key"+i, "value"+i);
				map.put("key"+i, "value"+i);
				i++;
			}
		}, "Producer").start();
		
		new Thread(() -> {
			int i = 0;
			while (i < 10000) {
				Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, Object> entry = it.next();
					log.info("<<<<<< remove key: {}, value: {}", entry.getKey(), entry.getValue());
					it.remove();
				}
				i++;
			}
		}, "Consumer").start();
		
		Thread.currentThread().join();
	}
}
