package com.loserico.search.builder;

import com.loserico.search.ElasticUtils;
import com.loserico.search.support.UpdateResult;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.engine.VersionConflictEngineException;

import java.util.Objects;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static com.loserico.search.ElasticUtils.ONLY_TYPE;
import static com.loserico.search.support.UpdateResult.Result.VERSION_CONFLICT;

/**
 * <p>
 * Copyright: (C), 2021-09-08 16:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUpdateBuilder {
	
	private String index;
	
	private String id;
	
	/**
	 * 更新文档的一部分, 文档不存在时创建文档
	 */
	private Boolean upsert;
	
	/**
	 * 文档
	 */
	private Object doc;
	
	/**
	 * 更新后是否立即刷新? 立即刷新可以马上搜索到, 不立即刷新可能要1s过后
	 */
	private Boolean refresh;
	
	private Long ifSeqNo;
	
	private Long ifPrimaryTerm;
	
	public ElasticUpdateBuilder(String index) {
		Objects.requireNonNull(index, "index cannot be null!");
		this.index = index;
	}
	
	/**
	 * 要更新的文档的ID
	 *
	 * @param id
	 * @return ElasticUpdateBuilder
	 */
	public ElasticUpdateBuilder id(String id) {
		Objects.requireNonNull(id, "id cannot be null!");
		this.id = id;
		return this;
	}
	
	/**
	 * 更新文档的一部分, 文档不存在时创建文档
	 *
	 * @param upsert
	 * @return ElasticUpdateBuilder
	 */
	public ElasticUpdateBuilder upsert(Boolean upsert) {
		this.upsert = upsert;
		return this;
	}
	
	/**
	 * 要更新的内容
	 *
	 * @param doc
	 * @return ElasticUpdateBuilder
	 */
	public ElasticUpdateBuilder doc(Object doc) {
		Objects.requireNonNull(doc, "doc cannot be null!");
		this.doc = doc;
		return this;
	}
	
	/**
	 * 更新后是否立即刷新? 立即刷新可以马上搜索到, 不立即刷新可能要1s过后
	 *
	 * @param refresh
	 * @return ElasticUpdateBuilder
	 */
	public ElasticUpdateBuilder refresh(Boolean refresh) {
		this.refresh = refresh;
		return this;
	}
	
	
	public ElasticUpdateBuilder ifSeqNo(Long ifSeqNo) {
		this.ifSeqNo = ifSeqNo;
		return this;
	}
	
	public ElasticUpdateBuilder ifPrimaryTerm(Long ifPrimaryTerm) {
		this.ifPrimaryTerm = ifPrimaryTerm;
		return this;
	}
	
	public UpdateResult update() {
		String document = null;
		if (doc instanceof String) {
			document = (String) doc;
		} else {
			document = toJson(doc);
		}
		UpdateRequestBuilder updateRequestBuilder = ElasticUtils.CLIENT.prepareUpdate(index, ONLY_TYPE, id).setDoc(document, XContentType.JSON);
		if (refresh != null && refresh.booleanValue()) {
			updateRequestBuilder.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
		}
		
		if (upsert != null && upsert.booleanValue()) {
			updateRequestBuilder.setDocAsUpsert(true);
		}
		
		if (ifSeqNo != null) {
			updateRequestBuilder.setIfSeqNo(ifSeqNo);
		}
		if (ifPrimaryTerm != null) {
			updateRequestBuilder.setIfPrimaryTerm(ifPrimaryTerm);
		}
		
		try {
			UpdateResponse response = updateRequestBuilder.get();
			return UpdateResult.from(response);
		} catch (VersionConflictEngineException e) {
			UpdateResult updateResult = new UpdateResult();
			updateResult.setResult(VERSION_CONFLICT);
			return updateResult;
		}
	}
}
