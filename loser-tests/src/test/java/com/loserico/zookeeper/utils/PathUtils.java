package com.loserico.zookeeper.utils;

import java.util.Objects;

/**
 * 处理Zookeeper路径相关功能
 * <p>
 * Copyright: Copyright (c) 2019-04-08 16:10
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public final class PathUtils {

	public static final String UNIX_PATH_SEPARATOR = "/";

	/**
	 * 首先检查nodePath不为空并且以/开头, 然后用/分割路径的各个部分
	 * 
	 * @param nodePath
	 * @return String[]
	 */
	public static String[] pathSplit(String nodePath) {
		Objects.requireNonNull(nodePath, "Node path cannot be null");
		if (nodePath.indexOf(UNIX_PATH_SEPARATOR) != 0) {
			throw new RuntimeException("Node path should start with /");
		}
		String[] paths = nodePath.split(UNIX_PATH_SEPARATOR);
		String[] pathArray = new String[paths.length - 1];
		for (int i = 1; i < paths.length; i++) {
			String path = paths[i];
			pathArray[i - 1] = path;
		}
		return pathArray;
	}
}
