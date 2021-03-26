package com.loserico.searchlegacy.support;

import org.elasticsearch.action.update.UpdateResponse;

/**
 * <p>
 * Copyright: (C), 2021-01-01 20:13
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class UpdateResult {
	
	private Long version;
	
	private Result result;
	
	public static enum Result {
		
		/**
		 * 创建了文档
		 */
		CREATED,
		
		/**
		 * 更新了文档
		 */
		UPDATED,
		
		/**
		 * 没有做任何操作
		 */
		NOOP;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public Result getResult() {
		return result;
	}
	
	/**
	 * 封装UpdateResult, 避免业务代码直接操作Elasticsearch底层API
	 * @param response
	 * @return UpdateResult
	 */
	public static UpdateResult from(UpdateResponse response) {
		UpdateResult updateResult = new UpdateResult();
		updateResult.version = response.getVersion();
		switch (response.getResult()) {
			case CREATED:
				updateResult.result = Result.CREATED;
				break;
			case UPDATED:
				updateResult.result = Result.UPDATED;
				break;
			default:
				updateResult.result = Result.NOOP;
		}
		
		return updateResult;
	}
}
