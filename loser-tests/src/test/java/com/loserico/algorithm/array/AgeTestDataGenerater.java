package com.loserico.algorithm.array;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * <p>
 * Copyright: (C), 2022-06-25 16:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AgeTestDataGenerater {
	
	public static void main(String[] args) throws Exception {
		String fileName = "D:\\age1.txt";
		Random random = new Random();
		BufferedWriter writer = null;
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
		for (int i = 0; i < 1400000000; i++) {
			int age = Math.abs(random.nextInt() % 180);
			writer.write(age + "\r\n");
		}
		writer.flush();
		writer.close();
	}
}
