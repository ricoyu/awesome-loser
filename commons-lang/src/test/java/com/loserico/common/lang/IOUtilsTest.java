package com.loserico.common.lang;

import com.loserico.common.lang.utils.IOUtils;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

import static com.loserico.common.lang.utils.IOUtils.toByteArray;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2020-08-21 14:10
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class IOUtilsTest {
	
	@SneakyThrows
	@Test
	public void testReadLargeFileToByteArray() {
		FileInputStream inputStream = new FileInputStream(new File("D:\\boot-commandline-websocket-0.0.1-SNAPSHOT.jar"));
		byte[] bytes = toByteArray(inputStream);
		System.out.println("bytes read: " + bytes.length);
		IOUtils.write(Paths.get("D:\\boot-commandline-websocket-0.0.2-SNAPSHOT.jar" ), bytes);
	}
	
	@SneakyThrows
	@Test
	public void testReadSmallFileToByteArray() {
		FileInputStream inputStream = new FileInputStream(new File("D:\\hi.txt"));
		byte[] bytes = toByteArray(inputStream);
		System.out.println("bytes read: " + bytes.length);
		IOUtils.write(Paths.get("D:\\hello.txt" ), new String(bytes, UTF_8));
	}
	
	@SneakyThrows
	@Test
	public void testReadEmptyFileToByteArray() {
		FileInputStream inputStream = new FileInputStream(new File("D:\\empty.txt"));
		byte[] bytes = toByteArray(inputStream);
		System.out.println("bytes read: " + bytes.length);
		IOUtils.write(Paths.get("D:\\empty2.txt" ), new String(bytes, UTF_8));
	}
	
	@SneakyThrows
	@Test
	public void testReadBytesFromChannel() {
		RandomAccessFile file = new RandomAccessFile("d:\\boot-commandline-websocket-0.0.1-SNAPSHOT.jar", "r");
		FileChannel channel = file.getChannel();
		byte[] bytes = toByteArray(channel);
		System.out.println(bytes.length);
		IOUtils.write("d:\\boot-commandline-websocket-0.0.3-SNAPSHOT.jar", bytes);
	}
}
