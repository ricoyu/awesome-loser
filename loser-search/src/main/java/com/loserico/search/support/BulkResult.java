package com.loserico.search.support;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-01-01 12:14
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class BulkResult {
	
	/**
	 * 成功数量
	 */
	private int successCount;
	
	/**
	 * 失败数量
	 */
	private int failCount;
	
	/**
	 * 文档id
	 */
	private List<String> ids = new ArrayList<>();
	
	/**
	 * 创建失败描述
	 */
	private List<String> failMessages = new ArrayList<>();
	
	public void fail() {
		failCount++;
	}
	
	public void success() {
		successCount++;
	}
	
	public void addFailMessage(String message) {
		failMessages.add(message);
	}
	
	public void addId(String id) {
		ids.add(id);
	}
	
	public int getSuccessCount() {
		return successCount;
	}
	
	public int getFailCount() {
		return failCount;
	}
	
	public List<String> getIds() {
		return ids;
	}
	
	public List<String> getFailMessages() {
		return failMessages;
	}
}
