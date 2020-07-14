package com.loserico.utils;

import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PropertyUtilsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtilsTest.class);

	@Test
	public void testLoadProperty() throws IOException {
		URL url = Resources.getResource("3月份发生.properties");
		System.out.println(Resources.toString(url, UTF_8));
		ByteSource byteSource = Resources.asByteSource(url);
		Properties properties = new Properties();
		
		try (InputStream inputStream = byteSource.openBufferedStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
			properties.load(inputStreamReader);
			properties.list(System.out);
			
			Map<String, String> centres = Maps.fromProperties(properties);
			centres.keySet().forEach(System.out::println);
		} catch (IOException e) {
			logger.error("msg", e);
		}
	}
}
