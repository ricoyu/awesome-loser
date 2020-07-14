package com.loserico.nio;

import static java.nio.file.StandardOpenOption.READ;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class FileSizeTest {

	@Test
	public void testStandardWay() {
		File initialFile = new File("D:\\Loser\\loser-tests\\src\\test\\java\\com\\loserico\\nio\\test.txt");
		System.out.println(initialFile.length());
	}
	
	@Test
	public void testNIOFileChannel() throws IOException {
		Path path = Paths.get("D:\\Loser\\loser-tests\\src\\test\\java\\com\\loserico\\nio\\test.txt");
		FileChannel fileChannel = FileChannel.open(path, READ);
		long size = fileChannel.size();
		System.out.println(size);
	}
	
	@Test
	public void testCommonsIO() {
		File initialFile = new File("D:\\Loser\\loser-tests\\src\\test\\java\\com\\loserico\\nio\\test.txt");
		long size = FileUtils.sizeOf(initialFile);
		System.out.println(size);
		System.out.println(FileUtils.byteCountToDisplaySize(size));
	}
}
