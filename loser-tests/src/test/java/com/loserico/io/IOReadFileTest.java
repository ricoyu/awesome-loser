package com.loserico.io;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * In this tutorial, we'll explore different ways to read from a File in Java.
 * <p>
 * First, we'll learn how to load a file from the classpath, a URL, or from a JAR file using standard Java classes.
 * <p>
 * Second, we'll see how to read the content with BufferedReader, Scanner, StreamTokenizer, DataInputStream, SequenceInputStream, and FileChannel. We will also discuss how to read a UTF-8 encoded file.
 * <p>
 * Finally, we’ll explore the new techniques to load and read a file in Java 7 and Java 8.
 *
 * <p>
 * Copyright: (C), 2021-07-08 15:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IOReadFileTest {
	
	/**
	 * This section explains how to read a file that is available on a classpath. We'll read the “fileTest.txt” available under src/main/resources:
	 * <p>
	 * we used the current class to load a file using getResourceAsStream method and passed the absolute path of the file to load.
	 * 
	 * The main difference is that when using the getResourceAsStream on a ClassLoader instance, 
	 * the path is treated as absolute starting from the root of the classpath.
	 *
	 * When used against a Class instance, the path could be relative to the package, or an absolute path, which is hinted by the leading slash.
	 * clazz.getResourceAsStream 路径可以是相对路径, 
	 */
	@SneakyThrows
	@Test
	public void testgivenFileNameAsAbsolutePath_whenUsingClasspath_thenFileData() {
		String expectedData = "Hello, world!";
		
		Class clazz = IOReadFileTest.class;
		InputStream inputStream = clazz.getResourceAsStream("/fileTest.txt");
		String data = readFromInputStream(inputStream);
		assertThat(data, containsString("Hello, world!"));
		
		ClassLoader classLoader = getClass().getClassLoader();
		inputStream = classLoader.getResourceAsStream("fileTest.txt");
		data = readFromInputStream(inputStream);
		assertThat(data, containsString("Hello, world!"));
	}
	
	@Test
	@SneakyThrows
	public void testgivenFileName_whenUsingFileUtils_thenFileData() {
		String expectedData = "Hello, world!";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("fileTest.txt").getFile());
		String data = FileUtils.readFileToString(file, "UTF-8");
		
		assertEquals(expectedData, data.trim());
	}
	
	@Test
	public void testwhenReadWithBufferedReader_thenCorrect() {
		String expected_value = "Hello, world!";
		String file ="src/test/resources/fileTest.txt";
	}
	
	private String readFromInputStream(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		}
		
		return sb.toString();
	}
}
