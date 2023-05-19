package com.loserico.common.lang;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

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
		String content = com.loserico.common.lang.utils.IOUtils.readClassPathFileAsString("application.yml");
		String content2 = com.loserico.common.lang.utils.IOUtils.readClassPathFileAsString("classpath:application.yml");
		assertEquals(content, content2);
	}
	
	@Test
	public void testReadFromWorkDir() {
		String workDir = System.getProperty("user.dir");
		String content = com.loserico.common.lang.utils.IOUtils.readFileAsString(workDir + "/application.yml");
		System.out.println(content);
	}
	
	@Test
	public void testReadFromFileSystem() {
		String content = com.loserico.common.lang.utils.IOUtils.readFileAsString("D:\\Learning\\awesome-loser\\commons-lang\\application.yml");
		System.out.println(content);
	}
	
	@Test
	public void testFileSeparator() {
		System.out.println(com.loserico.common.lang.utils.IOUtils.DIR_SEPARATOR);
		System.out.println(com.loserico.common.lang.utils.IOUtils.readClassPathFileAsString("application.yml"));;
		System.out.println(com.loserico.common.lang.utils.IOUtils.readFileAsString("D:\\Dropbox\\doc\\bw.txt"));;
	}
	
	@Test
	public void testReadParts() {
		byte[] bytes = com.loserico.common.lang.utils.IOUtils.readFileAsBytes("/home/ricoyu/data.txt", 0, 1024*1024);
		com.loserico.common.lang.utils.IOUtils.write("/home/ricoyu/data.txt.part0", bytes);
	}
	
	@Test
	public void testMerge() {
		com.loserico.common.lang.utils.IOUtils.merge("/home/ricoyu/data-back.txt", "/home/ricoyu/data.txt.part0", "/home/ricoyu/data.txt.part1");
	}
}
