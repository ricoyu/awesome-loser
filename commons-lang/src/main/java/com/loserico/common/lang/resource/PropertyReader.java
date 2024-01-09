package com.loserico.common.lang.resource;

import com.google.common.collect.Sets;
import com.loserico.common.lang.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 读取属性文件帮助类
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:08
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class PropertyReader {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertyReader.class);
	
	private static final String WORKING_DIR = System.getProperty("user.dir");
	
	private static final String FILE_SEPRATOR = System.getProperty("file.separator");
	
	/**
	 * 属性文件后缀, 不是CLASSPATH下的属性文件需要用全路径名称
	 */
	private static final String RESOURCE_SUFFIX = ".properties";
	
	/**
	 * 这三个ResourceBundle优先级: 低 -- 高
	 * resourceBundle classpath root下
	 * resourceBundle2 工作目录下
	 * resourceBundle3 工作目录的config目录
	 */
	private ResourceBundle resourceBundle = null;
	private ResourceBundle resourceBundle2 = null;
	private ResourceBundle resourceBundle3 = null;
	
	public PropertyReader(String resource) {
		try {
			//默认读resource.properties
			this.resourceBundle = ResourceBundle.getBundle(resource);
		} catch (Throwable e) {
			logger.debug("找不到{}", resource+".properties");
		}
		try {
			this.resourceBundle2 = new PropertyResourceBundle(new FileInputStream(WORKING_DIR + FILE_SEPRATOR + resource + RESOURCE_SUFFIX));
		} catch (Throwable e) {
			if (logger.isDebugEnabled()) {
				logger.debug("找不到{}", WORKING_DIR + "/" + resource);
			}
		}
		try {
			this.resourceBundle3 =
					new PropertyResourceBundle(new FileInputStream(WORKING_DIR + FILE_SEPRATOR + "config" + FILE_SEPRATOR + resource + RESOURCE_SUFFIX));
		} catch (Throwable e) {
			if (logger.isDebugEnabled()) {
				logger.debug("找不到{}", WORKING_DIR + "/config/" + resource);
			}
		}
	}
	
	public PropertyReader() {
	}
	
	/**
	 * 判断是否找打了resource文件
	 * @return
	 */
	public boolean resourceExists() {
		return resourceBundle != null || resourceBundle2 != null || resourceBundle3 != null;
	}
	
	/**
	 * 返回属性对应的int值，值不存在或者不是数字则返回-1
	 *
	 * @param property
	 * @return int
	 */
	public int getInt(String property) {
		if (!isResourcePresent()) {
			return -1;
		}
		String value = getString(property);
		if (value == null || "".equals(value.trim())) {
			return -1;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			logger.error("", e);
		}
		return -1;
	}
	
	/**
	 * 返回属性对应的int值，值不存在或者不是数字则返回 defaultValue
	 *
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	public int getInt(String property, int defaultValue) {
		if (!isResourcePresent()) {
			return defaultValue;
		}
		int value = getInt(property);
		return value == -1 ? defaultValue : value;
	}
	
	/**
	 * 属性不存在则返回null， 否则返回属性值（首位空格去掉）
	 *
	 * @param property
	 * @return String
	 */
	public String getString(String property) {
		String value = null;
		
		/*
		 * 先从 当前工作目录/config 下找
		 */
		if (resourceBundle3 != null) {
			try {
				value = resourceBundle3.getString(property);
			} catch (MissingResourceException e) {
			}
			if (value != null) {
				return value.trim();
			}
		}
		
		/*
		 * 再从 当前工作目录 下找
		 */
		if (resourceBundle2 != null) {
			try {
				value = resourceBundle2.getString(property);
			} catch (MissingResourceException e) {
			}
			if (value != null) {
				return value.trim();
			}
		}
		
		if (resourceBundle == null) {
			return null;
		}
		try {
			value = resourceBundle.getString(property);
		} catch (MissingResourceException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("resourceBundle中没有对应的key: {}", property);
			}
		}
		return value == null ? null : value.trim();
	}
	
	/**
	 * 属性不存在或者属性值为空字符串，则返回默认值，否则返回属性值
	 *
	 * @param property
	 * @return String
	 */
	public String getString(String property, String defaultValue) {
		String value = getString(property);
		return empty(value) ? defaultValue : value;
	}
	
	/**
	 * 根据属性名获取属性值, 然后用逗号分隔, 去掉两边空格和空元素, 返回List
	 *
	 * @param property
	 * @return List<String>
	 */
	public List<String> getStrList(String property) {
		String value = getString(property);
		if (isBlank(value)) {
			return emptyList();
		}
		
		String[] values = value.split(",");
		List<String> items = new ArrayList<>();
		for (int i = 0; i < values.length; i++) {
			String item = values[i];
			if (isNotBlank(item)) {
				items.add(item.trim());
			}
		}
		return items;
	}
	
	/**
	 * 属性值是逗号隔开的字符串
	 *
	 * @param property
	 * @return Set
	 */
	public Set<String> getStringAsSet(String property) {
		if (!isResourcePresent()) {
			return Collections.emptySet();
		}
		String value = getString(property);
		if (empty(value)) {
			return Collections.emptySet();
		}
		
		String[] arr = split(value);
		return Sets.newHashSet(arr);
	}
	
	/**
	 * 属性不存在返回false
	 *
	 * @param property
	 * @return boolean
	 */
	public boolean getBoolean(String property) {
		if (!isResourcePresent()) {
			return false;
		}
		String value = getString(property);
		
		if (empty(value)) {
			return false;
		}
		return Boolean.parseBoolean(value.trim());
	}
	
	/**
	 * 属性不存在返回默认值
	 *
	 * @param property
	 * @return boolean
	 */
	public boolean getBoolean(String property, boolean defaultValue) {
		if (!isResourcePresent()) {
			return defaultValue;
		}
		String value = getString(property);
		return empty(value) ? defaultValue : Boolean.parseBoolean(value.trim());
	}
	
	public LocalDate getLocalDate(String property) {
		String dateVal = getString(property);
		return DateUtils.toLocalDate(dateVal);
	}
	
	/**
	 * 将格式为: company.accounts=RBKSCC:712579648001, CMTSCC:, JESCC:
	 * 转成Map<String, String>
	 *
	 * @param property
	 * @return Map<String, String>
	 * @on
	 */
	public Map<String, String> getMap(String property) {
		String value = getString(property);
		if (isBlank(value)) {
			return emptyMap();
		}
		
		Map<String, String> resultMap = new HashMap<>();
		String[] keyValues = value.split(",");
		for (int i = 0; i < keyValues.length; i++) {
			String keyValue = keyValues[i];
			if (isBlank(keyValue)) {
				continue;
			}
			String[] keyValuePair = keyValue.split(":");
			String key = keyValuePair[0].trim();
			if (isNotBlank(key)) {
				if (keyValuePair.length == 1) {
					resultMap.put(key, null);
				} else {
					resultMap.put(key, keyValuePair[1].trim());
				}
			}
		}
		
		return resultMap;
	}
	
	public String[] split(String value) {
		if (empty(value)) {
			return new String[0];
		}
		String[] tempArr = value.split(",");
		int actualElements = 0;
		for (int i = 0; i < tempArr.length; i++) {
			String str = tempArr[i];
			if (!empty(str)) {
				actualElements++;
				tempArr[i] = str.trim();
			}
		}
		
		String[] resultArr = new String[actualElements];
		for (int i = 0; i < resultArr.length; i++) {
			for (int j = 0; j < tempArr.length; j++) {
				String v = tempArr[j];
				if (!empty(v)) {
					resultArr[i] = v;
					tempArr[j] = null;
					break;
				}
			}
		}
		
		return resultArr;
	}
	
	
	private boolean empty(String value) {
		return value == null || "".equals(value.trim());
	}
	
	/**
	 * 表示默认三个位置下属性文件是否存在
	 *
	 * @return
	 */
	private boolean isResourcePresent() {
		return resourceBundle != null || resourceBundle2 != null || resourceBundle3 != null;
	}
}
