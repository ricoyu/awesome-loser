package com.loserico.io;

import com.loserico.common.lang.utils.IOUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Copyright: (C), 2021-03-04 9:54
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class WriteAnyPieceContentTest {
	
	public static void main(String[] args) {
		//String content = IOUtils.readFileAsString("D:\\Learning\\awesome-loser\\loser-tests\\src\\test\\resources\\suricata.yaml");
		//t1();
		t3();
		//t2();
	}
	
	@SneakyThrows
	private static void t3() {
		String content = IOUtils.readFileAsString("D:\\suricata.yaml");
		
		String requestRegex = "^\\s*(request-body-limit:\\s+([0-9]+[a-zA-Z]*)).*";
		String responseRegex = "^\\s*(response-body-limit:\\s+([0-9]+[a-zA-Z]*)).*";
		Pattern requestBodyPattern = Pattern.compile(requestRegex);
		Pattern responseBodyPattern = Pattern.compile(responseRegex);
		
		StringBuilder result = new StringBuilder();
		File file = new File("D:\\suricata.yaml");
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				
				Matcher matcher = requestBodyPattern.matcher(line);
				if (matcher.matches()) {
					String group2 = matcher.group(2);
					String group1 = matcher.group(1);
					String group0 = matcher.group(0);
					System.out.println(group0);
					System.out.println(group1);
					System.out.println(group2);
					
					String newLine = line.replaceAll(group2, "20mb");
					content = content.replaceAll(line, newLine);
				}
				
				matcher = responseBodyPattern.matcher(line);
				if (matcher.matches()) {
					String group2 = matcher.group(2);
					String group1 = matcher.group(1);
					String group0 = matcher.group(0);
					System.out.println(group0);
					System.out.println(group1);
					System.out.println(group2);
					
					String newLine = line.replaceAll(group2, "20mb");
					content = content.replaceAll(line, newLine);
				}
			}
			scanner.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(content);
		writer.flush();
		writer.close();
	}
	
	public static void t2() {
		String content = "request-body-limit: 100kb";
		String regex = "request-body-limit:.*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.matches()) {
			System.out.println(matcher.group(0));
		}
	}
	
	private static void t1() {
		String content = IOUtils.readFileAsString("D:\\Learning\\awesome-loser\\loser-tests\\src\\test\\resources\\test.yml");
		String regex = ".*(request-body-limit:\\s{1}([0-9a-zA-Z]+)).*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.matches()) {
			String group2 = matcher.group(2);
			String group1 = matcher.group(1);
			String group0 = matcher.group(0);
			System.out.println(group0);
			System.out.println(group1);
			System.out.println(group2);
		}
	}
}
