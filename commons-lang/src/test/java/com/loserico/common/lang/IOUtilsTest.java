package com.loserico.common.lang;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static com.loserico.common.lang.utils.IOUtils.*;
import static org.junit.Assert.*;

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
	
	@Test
	public void testLineSeparator() {
		String lineSeparator = IOUtils.LINE_SEPARATOR;
		System.out.println(lineSeparator);
	}
	
	@Test
	public void testReadClasspathFile() {
		String content = readClassPathFileAsString("application.yml");
		String content2 = readClassPathFileAsString("classpath:application.yml");
		assertEquals(content, content2);
	}
	
	@Test
	public void testReadFromWorkDir() {
		String workDir = System.getProperty("user.dir");
		String content = readFileAsString(workDir + "/application.yml");
		System.out.println(content);
	}
	
	@Test
	public void testReadFromFileSystem() {
		String content = readFileAsString("D:\\Learning\\awesome-loser\\commons-lang\\application.yml");
		System.out.println(content);
	}
	
	@Test
	public void testFileSeparator() {
		System.out.println(DIR_SEPARATOR);
		System.out.println(readClassPathFileAsString("application.yml"));;
		System.out.println(readFileAsString("D:\\Dropbox\\doc\\bw.txt"));;
	}
	
	@Test
	public void testReadParts() {
		byte[] bytes = readFileAsBytes("/home/ricoyu/data.txt", 0, 1024*1024);
		write("/home/ricoyu/data.txt.part0", bytes);
	}
	
	@Test
	public void testMerge() {
		merge("/home/ricoyu/data-back.txt", "/home/ricoyu/data.txt.part0", "/home/ricoyu/data.txt.part1");
	}

	@Test
	public void testReadClasspathFileAsStr() {
		String str = readClassPathFileAsString("invalidJson.txt");
		System.out.println(str);
	}
}
