package com.loserico.mongo.support;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.mongo.exception.DuplicateScriptFileException;
import com.loserico.mongo.exception.InvalidScriptException;
import com.loserico.mongo.exception.ScriptFileNotExistsException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import static com.loserico.common.lang.utils.Assert.notNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2020-09-22 14:16
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class ExternalScriptsHelper {
	
	private static final String DOT = ".";
	
	/**
	 * Mongo shell 脚本的内容都是JSON对象, 所以以{开始
	 */
	private static final String MONGO_SHELL_OPEN_TOKEN = "{";
	
	private ConcurrentHashMap<String, String[]> scriptCache = new ConcurrentHashMap<>(12);
	
	/**
	 * mongo shell脚本位置
	 */
	private String location;
	
	/**
	 * mongo shell脚本文件名后缀
	 */
	private String fileSuffix;
	
	/**
	 * 如果以{开始, 认为是mongo shell脚本内容<br/>
	 * 否则认为是mongo shell脚本文件名
	 * @param s
	 * @return boolean
	 */
	public boolean isFileName(String s) {
		notNull(s, "param cannot be null");
		return s.indexOf(MONGO_SHELL_OPEN_TOKEN) != 0;
		
	}
	
	/**
	 * 根据fileName获取脚本内容
	 * @param fileName
	 * @return String[]
	 */
	public String[] get(String fileName) {
		String[] scripts = scriptCache.get(fileName);
		if (scripts == null || scripts.length == 0) {
			throw new ScriptFileNotExistsException("File " + filename(fileName, fileSuffix) + " not exists or file content is empty");
		}
		return scripts;
	}
	
	/**
	 * 根据fileName获取脚本内容
	 * @param fileName
	 * @return String
	 */
	public String getSingle(String fileName) {
		String[] scripts = scriptCache.get(fileName);
		if (scripts == null || scripts.length == 0) {
			throw new ScriptFileNotExistsException("File " + filename(fileName, fileSuffix) + " not exists or file content is empty");
		}
		
		return scripts[0];
	}
	
	@PostConstruct
	public void init() {
		//先把指定目录下的文件列出来, 有重名的则报错
		List<String> fileNames = null;
		if (isBlank(fileSuffix)) {
			fileNames = IOUtils.listClasspathFileNames(location);
		} else {
			String filePattern = null;
			if (fileSuffix.startsWith(DOT)) {
				filePattern = "*" + fileSuffix;
			} else {
				filePattern = "*." + fileSuffix;
			}
			fileNames = IOUtils.listClasspathFileNames(location, filePattern);
		}
		
		List<String> distinctFileNames = fileNames.stream().distinct().collect(Collectors.toList());
		//有重复的文件名
		if (fileNames.size() != distinctFileNames.size()) {
			throw new DuplicateScriptFileException("There are duplicate files under " + location);
		}
		
		/*
		 * 读取每个文件的内容, 一个文件里面可以写多个mongo shell 脚本, 比如在聚合操作的时候
		 * 所以一个文件名可以对应多个mongo shell scripts
		 */
		for (String fileName : fileNames) {
			List<String> strings = readAsArray(fileName);
			String[] scripts = strings.stream().toArray(String[]::new);
			/*
			 * 如果fileName是带后缀的, 如searchStudent.txt
			 * 那么往scriptCache中put两个key: searchStudent.txt 和 searchStudent
			 * value都是scripts, 这样方便客户端传脚本名称, 他可以带或者不带后缀
			 */
			scriptCache.put(fileName, scripts);
			scriptCache.put(filePrefix(fileName), scripts);
		}
		
	}
	
	/**
	 * 脚本文件里面可以包含一个或多个脚本, 如
	 * <ul>
	 *    <li>{"_id": 1}</li>
	 * </ul>
	 * <ul>
	 * 或者像聚合查询, 可以放多个查询条件
	 *    <li>{"$match": {"score": {"$gte": 80}}}<br/>{"$project": {"studentId": 1}}</li>
	 * </ul>
	 * <ul>
	 * 又或者一个查询条件, 一个排序
	 *    <li>{"job": "programmer"}</li>
	 *    <li>{"salary": -1, "dep": 1}</li>
	 * </ul>
	 *
	 * @param scriptName
	 * @return
	 */
	private List<String> readAsArray(String scriptName) {
		String content = IOUtils.readClassPathFileAsString(location, scriptName);
		
		//记录一个完整脚本的开始位置
		int start = -1;
		//记录一个完整脚本的结束位置
		int end = -1;
		
		/*
		 * Mongo脚本都是以{开头, 以}结尾的, 所以即使一个文件里面写了多个mongo脚本,
		 * 只要能标记出每个脚本的开始/结束位置就可以了, 用栈就可以实现
		 *
		 * 每遇到一个{就入栈, 没遇到一个}就出栈, 栈为空表示一段mondo脚本结束了,
		 * 根据记录的开始/结束位置substring一个整个脚本文件内容就可以了
		 */
		ConcurrentLinkedDeque<Character> stack = new ConcurrentLinkedDeque();
		//将整个脚本文件的内容转成字符数组
		char[] chars = content.toCharArray();
		List<String> scripts = new ArrayList<>();
		
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			//是{就入栈
			if (c == '{') {
				stack.push(c);
				if (start == -1) {
					//记录最开始一个{的位置
					start = i;
				}
			} else if (c == '}') {
				//如果出现}但是栈已经空了, 说明{}没有成对出现, mongo脚本语法肯定写得有问题
				if (stack.isEmpty()) {
					throw new InvalidScriptException("script " + content + " is invalid, one extra }");
				}
				stack.pop();
			}
			
			if (stack.isEmpty()) {
				//表示还没有遇到{开始符, 那么继续
				if (start == -1) {
					continue;
				}
				end = i;
				scripts.add(content.substring(start, end + 1));
				//reset
				start = -1;
				end = -1;
			}
			
		}
		
		//到这里, stack应该是空的, 如果不是, 那么表示脚本内容少了}, 写得语法有问题
		if (!stack.isEmpty()) {
			throw new InvalidScriptException("script " + content + " is invalid, missing }");
		}
		
		return scripts;
	}
	
	private String filename(String filename, String suffix) {
		if (isBlank(suffix)) {
			return filename;
		}
		
		if (suffix.startsWith(DOT)) {
			return filename + suffix;
		}
		
		return filename + DOT + suffix;
	}
	
	private String filePrefix(String filename) {
		if (isBlank(filename)) {
			return null;
		}
		
		int i = filename.lastIndexOf(DOT);
		//认为这个文件名没有后缀部分
		if (i == -1) {
			return filename;
		}
		
		return filename.substring(0, i);
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getFileSuffix() {
		return fileSuffix;
	}
	
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
}
