package com.loserico.algorithm.array;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>
 * Copyright: (C), 2022-06-25 17:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AgeStats {
	
	public static void main(String[] args) throws Exception {
		String line = "";
		String fileName = "D:\\agew1.txt";
		InputStreamReader in = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
		
		long start = System.currentTimeMillis();
		int[] data = new int[200];
		int totalTount = 0;
		try (BufferedReader bufferedReader = new BufferedReader(in)) {
		    while ((line = bufferedReader.readLine()) != null) {
		        int age = Integer.valueOf(line);
		        data[age]++;
			    totalTount++;
		    }
		    System.out.println("总的数据量: " + totalTount);
		} catch (IOException e) {
		    // ... handle IO exception
		}
		
		for (int i = 0; i < 200; i++) {
			System.out.println(i+": " + data[i]);
		}
		System.out.println("计算花费时间为: " +(System.currentTimeMillis() - start));
	}
}
