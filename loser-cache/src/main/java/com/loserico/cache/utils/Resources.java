package com.loserico.cache.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 11:21
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class Resources {

	public static List<File> getResources(String fileName) {
		List<File> retval = new ArrayList<File>();
		String classPath = System.getProperty("java.class.path", ".");
		String[] classPathElements = classPath.split(System.getProperty("path.separator"));
		for (String element : classPathElements) {
			retval.addAll(getResources(element, fileName));
		}
		return retval;
	}

	private static List<File> getResources(String element, String fileName) {
		List<File> files = new ArrayList<File>();
		File file = new File(element);
		if (file.isDirectory()) {
			files.addAll(getResourcesFromDirectory(file, fileName));
		}
		return files;
	}

	/**
	 * 如果fileOrDirectory是普通文件，则比较文件名是否与fileName相同
	 * 否则递归查询fileOrDirectory的子目录，重复以上步骤
	 * 找到则加入files
	 * 
	 * @on
	 * @param fileOrDirectory
	 * @param fileName
	 * @return
	 */
	private static List<File> getResourcesFromDirectory(File fileOrDirectory, String fileName) {
		List<File> files = new ArrayList<>();
		File[] fileList = fileOrDirectory.listFiles();
		for (File file : fileList) {
			if (file.isDirectory()) {
				files.addAll(getResourcesFromDirectory(file, fileName));
			} else {
				String foundFileName = file.getName();
				if (foundFileName.equals(fileName)) {
					files.add(file);
				}
			}
		}
		return files;
	}

}