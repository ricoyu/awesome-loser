package com.loserico.map;

/**
 * <p>
 * Copyright: (C), 2019/11/25 17:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HashMapTest {
	
	public static void main(String[] args) {
		HashMap hashMap = new HashMap();
		for (int i = 0; i < 17; i++) {
			hashMap.put("k"+i, "v"+i);
		}
	}
}
