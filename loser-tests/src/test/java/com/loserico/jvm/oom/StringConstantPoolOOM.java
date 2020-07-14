package com.loserico.jvm.oom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringConstantPoolOOM {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		while (true) {
			list.add(randomStr(123).intern());
		}
	}
	
	public static String randomStr(int targetStringLength) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}
}
