package com.loserico.common.lang.resource;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>
 * Copyright: (C), 2021-01-21 14:05
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class YamlProfileReaders implements YamlOps {
	
	private YamlReader yamlReader;
	
	private YamlReader profileReader;
	
	/**
	 * yml的文件名, 不带.yml后缀<p>
	 * 支持profile以及工作目录, classpath下不同优先级配置文件读取<p>
	 *
	 * 优先级从高到低
	 * <ol>
	 * <li/>工作目录下config目录下的同名配置文件
	 * <li/>工作目录下的同名配置文件
	 * <li/>classpath下的同名配置文件
	 * </ol>
	 * @param resource
	 */
	public static YamlProfileReaders instance(String resource) {
		YamlReader yamlReader = new YamlReader(resource);
		return new YamlProfileReaders(yamlReader);
	}
	
	public YamlProfileReaders(YamlReader yamlReader) {
		Objects.requireNonNull(yamlReader, "yamlReader cannot be null!");
		this.yamlReader = yamlReader;
		String profile = yamlReader.getString("spring.profiles.active");
		if (isNotBlank(profile)) {
			this.profileReader = new YamlReader(yamlReader.getResource() + "-" + profile);
		}
	}
	
	@Override
	public boolean exists() {
		return yamlReader.exists();
	}
	
	@Override
	public Integer getInt(String path) {
		if (profileReader == null) {
			return yamlReader.getInt(path);
		}
		
		Integer value = profileReader.getInt(path);
		if (value != null) {
			return value;
		}
		
		return yamlReader.getInt(path);
	}
	
	@Override
	public Integer getInt(String path, Integer defaultValue) {
		Integer value = getInt(path);
		if (value == null) {
			return defaultValue;
		}
		
		return value;
	}
	
	@Override
	public String getString(String path) {
		if (profileReader == null) {
			return yamlReader.getString(path);
		}
		
		String value = profileReader.getString(path);
		if (isNotBlank(value)) {
			return value;
		}
		
		return yamlReader.getString(path);
	}
	
	@Override
	public String getString(String path, String defaultValue) {
		String value = getString(path);
		if (isBlank(value)) {
			return defaultValue;
		}
		
		return value;
	}
}
