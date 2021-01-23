package com.loserico.common.lang.resource;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-01-21 11:28
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class YamlReaderTest {
	
	@Test
	public void test() {
		YamlReader yamlReader = new YamlReader("application");
		String activeProfile = yamlReader.getString("spring.profiles.active");
		System.out.println(activeProfile);
		System.out.println(System.getProperty("user.dir"));
	}
}
