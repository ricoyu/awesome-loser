package com.loserico.jvm;

import java.util.Map;
import java.util.Random;

public class Wrapper {
	public static void main(String args[]) throws Exception {
		Map map = System.getProperties();
		Random r = new Random();
		while (true) {
			map.put(r.nextInt(), "value");
		}
	}
}