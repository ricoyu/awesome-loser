package com.loserico.search.support;

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
	
	
	private Long ifSeqNo;
	
	private Long ifPrimaryTerm;
	
	private Result result;
	
	public Long getIfSeqNo() {
		return ifSeqNo;
	}
	
	public void setIfSeqNo(Long ifSeqNo) {
		this.ifSeqNo = ifSeqNo;
	}
	
	public Long getIfPrimaryTerm() {
		return ifPrimaryTerm;
	}
	
	public void setIfPrimaryTerm(Long ifPrimaryTerm) {
		this.ifPrimaryTerm = ifPrimaryTerm;
	}
	
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
		NOOP,
		
		/**
		 * 版本冲突, 更新失败
		 */
		VERSION_CONFLICT;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public void setResult(Result result) {
		this.result = result;
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
		long ifSeqNo = response.getSeqNo();
		long ifPrimaryTerm = response.getPrimaryTerm();
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
		updateResult.setIfSeqNo(ifSeqNo);
		updateResult.setIfPrimaryTerm(ifPrimaryTerm);
		return updateResult;
	}
}
