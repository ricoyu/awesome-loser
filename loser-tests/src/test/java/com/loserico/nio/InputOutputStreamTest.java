package com.loserico.nio;

import static java.nio.file.StandardOpenOption.READ;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

public class InputOutputStreamTest {
	
	private static final Logger logger = LoggerFactory.getLogger(InputOutputStreamTest.class);

	/**
	 * Java 原生 API
	 * @throws IOException
	 */
	@Test
	public void testPath2InputStream() throws IOException {
		File initialFile = new File("D:\\Loser\\loser-tests\\src\\test\\java\\com\\loserico\\nio\\test.txt");
		InputStream inputStream = new FileInputStream(initialFile);
		System.out.println(inputStream.available());
		inputStream.close();
	}
	
	/**
	 * NIO 方式
	 */
	@Test
	public void testPath2InputStreamUsingNIO() {
		Path path = Paths.get("D:\\Loser\\loser-tests\\src\\test\\java\\com\\loserico\\nio\\test.txt");
		try (InputStream inputStream = java.nio.file.Files.newInputStream(path, READ)) {
			System.out.println(inputStream.available());
		} catch (IOException e) {
			logger.error("msg", e);
		}
	}
	
	/**
	 * Guava 
	 * @throws IOException
	 */
	@Test
	public void testPath2InputStreamUsingGuava() throws IOException {
		File initialFile = new File("D:\\Loser\\loser-tests\\src\\test\\java\\com\\loserico\\nio\\test.txt");
		InputStream inputStream = Files.asByteSource(initialFile).openStream();
		System.out.println(inputStream.available());
	}
	
	/**
	 * Apache Commons IO
	 * @throws IOException 
	 */
	@Test
	public void testPath2InputStreamUsingCommonsIO() throws IOException {
		File initialFile = new File("D:\\Loser\\loser-tests\\src\\test\\java\\com\\loserico\\nio\\test.txt");
		InputStream inputStream = FileUtils.openInputStream(initialFile);
		System.out.println(inputStream.available());
	}
}
