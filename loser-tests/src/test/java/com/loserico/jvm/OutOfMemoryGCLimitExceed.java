package com.loserico.jvm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OutOfMemoryGCLimitExceed {

	public static void main(String[] args) {
		Map<Date, Date> dataMap = new HashMap<>();
		Random r = new Random();
		int i = 0;
		while (true) {
			System.out.println(++i);
			dataMap.put(new Date(), new Date());
		}
	}
}
