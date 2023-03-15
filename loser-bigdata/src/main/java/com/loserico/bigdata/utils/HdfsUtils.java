package com.loserico.bigdata.utils;

import com.loserico.bigdata.constants.HdfsConstants;
import com.loserico.common.lang.resource.PropertyReader;
import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.networking.enums.HttpMethod;
import com.loserico.networking.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.text.MessageFormat.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>
 * Copyright: (C), 2023-03-14 15:53
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class HdfsUtils {
	
	private static final String HTTP_SCHEMA = "http://";
	
	private static final String ROOT_DIR = "/";
	private static final PropertyReader PROPERTY_READER = new PropertyReader("hdfs");
	
	private static String nameNodeHost = null;
	
	static {
		nameNodeHost = PROPERTY_READER.getString("hdfs.host");
		if (!nameNodeHost.startsWith(HTTP_SCHEMA)) {
			nameNodeHost = HTTP_SCHEMA+nameNodeHost;
		}
	}
	/**
	 * 0 - NameNode地址
	 * 1 - 文件路径
	 */
	private static final String OP_URL_TEMPLATE ="{0}/webhdfs/v1{1}?op={2}";
	
	/**
	 * 重命名文件或目录
	 */
	private static final String OP_RENAME_URL_TEMPLATE ="{0}/webhdfs/v1{1}?op={2}&destination={3}";
	
	/**
	 * 上传文件
	 */
	private static final String OP_CREATE_URL_TEMPLATE ="{0}/webhdfs/v1{1}?op={2}&overwrite=true&noredirect=true";
	
	/**
	 * Web HDFS OPEN操作
	 * @param filePath
	 * @return
	 */
	public static String open(String filePath) {
		String url = format(OP_URL_TEMPLATE, nameNodeHost, adjustFilePath(filePath), HdfsConstants.OP_OPEN);
		return HttpUtils.get(url).timeout(5, TimeUnit.SECONDS).request();
	}
	
	/**
	 * 创建目录, Web HDFS MKDIRS操作, 该接口同一个目录可以重复创建, 每次都返回true
	 * @param dir
	 * @return boolean
	 */
	public static boolean mkdir(String dir) {
		String url = format(OP_URL_TEMPLATE, nameNodeHost, adjustFilePath(dir), HdfsConstants.OP_MKDIRS);
		Map<String, Boolean> response = HttpUtils.put(url).timeout(3, TimeUnit.SECONDS).responseType(Map.class).request();
		return response.get("boolean");
	}
	
	/**
	 * 上传文件分两步, 先请求NameNode, 然后NameNode返回DataNode对应的URL, 再上传文件到这个URL
	 * @param sourceLocation
	 * @param targetLocation
	 */
	public static void upload(String sourceLocation, String targetLocation) {
		String url = format(OP_CREATE_URL_TEMPLATE, nameNodeHost, adjustFilePath(targetLocation), HdfsConstants.OP_CREATE);
		Map<String, String> dataNodeLocationMap = HttpUtils.put(url).responseType(Map.class).request();
		HttpUtils.form(dataNodeLocationMap.get("Location")).method(HttpMethod.PUT).file("file", sourceLocation).request();
	}
	
	public static boolean isExists(String file) {
		String url = format(OP_URL_TEMPLATE, nameNodeHost, adjustFilePath(file), HdfsConstants.OP_GETFILESTATUS);
		try {
			Map<String, Object> response = HttpUtils.get(url).responseType(Map.class).request();
			if (response.containsKey("RemoteException")) {
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("", e);
		}
		return false;
	}
	
	/**
	 * 显示目录文件列表
	 * @param dir
	 * @return
	 */
	public static List<String> list(String dir) {
		String url = format(OP_URL_TEMPLATE, nameNodeHost, adjustFilePath(dir), HdfsConstants.OP_LIST_DIR);
		String response = HttpUtils.get(url).timeout(3, TimeUnit.SECONDS).request();
		if (isNotBlank(response)) {
			List<String> files = JsonPathUtils.readListNode(response, "FileStatuses.FileStatus[*].pathSuffix");
			return files;
		}
		return new ArrayList<>();
	}
	
	/**
	 * 重命名文件, 重命名成功返回true, sourceFile不存在返回false
	 * @param sourceFile
	 * @param destFile
	 * @return
	 */
	public static boolean rename(String sourceFile, String destFile) {
		String url = format(OP_RENAME_URL_TEMPLATE, nameNodeHost, adjustFilePath(sourceFile), HdfsConstants.OP_RENAME, adjustFilePath(destFile));
		Map<String, Boolean> response = HttpUtils.put(url).timeout(3, TimeUnit.SECONDS).responseType(Map.class).request();
		return response.get("boolean");
	}
	/**
	 * 删除文件或目录, 删除成功返回true, 删除不存在的文件返回false
	 * @param file
	 * @return boolean
	 */
	public static boolean delete(String file) {
		String url = format(OP_URL_TEMPLATE, nameNodeHost, adjustFilePath(file), HdfsConstants.OP_DELETE);
		Map<String, Boolean> response = HttpUtils.delete(url).timeout(3, TimeUnit.SECONDS).responseType(Map.class).request();
		return response.get("boolean");
	}
	
	private static String adjustFilePath(String fileOrDir) {
		if (isBlank(fileOrDir)) {
			fileOrDir = ROOT_DIR;
		}
		if (!fileOrDir.startsWith(ROOT_DIR)) {
			fileOrDir = ROOT_DIR + fileOrDir;
		}
		
		return fileOrDir;
	}
}
