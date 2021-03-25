package com.loserico.common.lang.resource;

import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * 读取yml文件
 * <p>
 * Copyright: (C), 2021-01-21 11:10
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class YamlReader implements YamlOps {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertyReader.class);
	
	private static final String WORKING_DIR = System.getProperty("user.dir");
	
	private static final String FILE_SEPRATOR = System.getProperty("file.separator");
	
	/**
	 * 属性文件后缀, 不是CLASSPATH下的属性文件需要用全路径名称
	 */
	private static final String RESOURCE_SUFFIX = ".yml";
	
	private String resource;
	
	/**
	 * 这三个Yaml优先级: 低 -- 高
	 * yaml1 classpath root下
	 * yaml2 工作目录下
	 * yaml3 工作目录的config目录
	 */
	private Map<String, Object> yaml1 = null;
	private Map<String, Object> yaml2 = null;
	private Map<String, Object> yaml3 = null;
	
	/**
	 * yml的文件名, 不带.yml后缀<p>
	 * 推荐使用: YamlOps yamlOps = YamlProfileReaders.instance("application");<p>
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
	public YamlReader(String resource) {
		this.resource = resource;
		
		Yaml yaml = new Yaml();
		try {
			/*
			 * 读取classpath下的yml
			 */
			InputStream inputStream = IOUtils.readClasspathFileAsInputStream(resource + RESOURCE_SUFFIX);
			if (inputStream != null) {
				yaml1 = yaml.load(inputStream);
				inputStream.close();
			}
			/*
			 * 读取工作目录下的yml
			 */
			inputStream = IOUtils.readFileAsStream(WORKING_DIR + FILE_SEPRATOR + resource + RESOURCE_SUFFIX);
			if (inputStream != null) {
				yaml2 = yaml.load(inputStream);
				inputStream.close();
			}
			/*
			 * 读取工作目录config下的yml
			 */
			inputStream = IOUtils.readFileAsStream(WORKING_DIR + FILE_SEPRATOR + "config" + FILE_SEPRATOR + resource + RESOURCE_SUFFIX);
			if (inputStream != null) {
				yaml3 = yaml.load(inputStream);
				inputStream.close();
			}
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * 判断yml是否存在
	 *
	 * @return
	 */
	@Override
	public boolean exists() {
		return yaml1 != null || yaml2 != null || yaml3 != null;
	}
	
	@Override
	public Integer getInt(String path) {
		try {
			Object value = get(path);
			return Transformers.convert(value, Integer.class);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
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
		Object value = get(path);
		return Transformers.convert(value, String.class);
	}
	
	@Override
	public String getString(String path, String defaultValue) {
		String value = getString(path);
		if (value == null) {
			return defaultValue;
		}
		
		return value;
	}
	
	public String getResource() {
		return this.resource;
	}
	
	private Object get(String path) {
		/*
		 * 把spring.profiles.active这种key根据.拆开来
		 */
		String[] paths = path.split("\\.");
		
		List<Map<String, Object>> yamls = asList(yaml3, yaml2, yaml1);
		for (Map<String, Object> yaml : yamls) {
			Object temp = yaml;
			
			/*
			 * 先取工作目录config目录下的yaml文件
			 * 如果读到对应的配置项, 那么直接返回, 因为它的优先级最高
			 */
			if (temp != null) {
				for (int i = 0; i < paths.length; i++) {
					String property = paths[i];
					if (temp == null) {
						log.debug("属性 {} 不存在", property);
						return null;
					}
					temp = ((Map) temp).get(property);
				}
				
				if (temp != null) {
					return temp;
				}
				
			}
		}
		
		return null;
	}
	
}
