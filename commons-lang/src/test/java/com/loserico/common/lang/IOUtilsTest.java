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
}
