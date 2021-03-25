package com.loserico.io;

import java.io.File;
import java.nio.file.Paths;

/**
 * <p>
 * Copyright: (C), 2021-03-17 14:27
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FileTest {
	
	public static void main(String[] args) {
		File file = Paths.get("D:\\WorkingSource\\SMIC-Projects\\nta-main-service\\src\\main\\resources\\rsa", "rsa_key.pem").toFile();
		System.out.println(file.exists());
		file = Paths.get("D:\\WorkingSource\\SMIC-Projects\\nta-main-service\\src\\main\\resources\\rsa\\", "rsa_key.pem").toFile();
		System.out.println(file.exists());
		
		file = Paths.get("D:\\WorkingSource\\SMIC-Projects\\nta-main-service\\src\\main\\resources\\rsa\\", "rsa_key", ".pem").toFile();
		System.out.println(file.exists());
		
		file = Paths.get("D:\\WorkingSource\\SMIC-Projects\\nta-main-service\\src\\main\\resources\\rsa\\", "rsa_key", "pem").toFile();
		System.out.println(file.exists());
		
		System.out.println(".pem".indexOf("."));
		String suffix = "pem";
		System.out.println(suffix);
	}
}
