package com.loserico.common.lang.resource;

import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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
	
	@Test
	public void testK8sYaml() throws IOException {
		InputStream inputStream = IOUtils.readFileAsStream("D:\\Dropbox\\Docker & Kubernetes\\tulingmall-gateway-ingress.yaml");
		Map<String, Object> map = new Yaml().load(inputStream);
		for (String s : map.keySet()) {
			System.out.println(s+": " + map.get(s));
		}
	}
	
	@Test
	public void testbootstrapYaml() throws IOException {
		InputStream inputStream = IOUtils.readFileAsStream("D:\\Learning\\awesome-plus\\awesome-order\\src\\main\\resources\\bootstrap.yaml");
		Map<String, Object> map = new Yaml().load(inputStream);
		for (String s : map.keySet()) {
			System.out.println(s+": " + map.get(s));
		}
	}
}
