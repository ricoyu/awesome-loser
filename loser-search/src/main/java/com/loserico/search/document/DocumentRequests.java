package com.loserico.search.document;

import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.exception.DocumentSaveException;
import com.loserico.search.exception.IndexCreateException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest.OpType;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

/**
 * Elasticsearch 文档相关操作
 * <p>
 * Copyright: (C), 2020-12-03 21:02
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class DocumentRequests {
	
	private RestHighLevelClient client;
	
	public DocumentRequests(RestHighLevelClient client) {
		this.client = client;
	}
	
	/**
	 * 创建文档
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public String create(String index, String doc) {
		IndexRequest request = new IndexRequest(index);
		request.opType(OpType.CREATE);
		request.source(doc, XContentType.JSON);
		IndexResponse response = null;
		try {
			response = client.index(request, DEFAULT);
		} catch (IOException e) {
			throw new IndexCreateException(e);
		}
		return response.getId();
	}
	
	/**
	 * 创建文档
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public String create(String index, String id, String doc) {
		IndexRequest request = new IndexRequest(index);
		request.opType(OpType.CREATE);
		request.id(id).source(doc, XContentType.JSON);
		IndexResponse response = null;
		try {
			response = client.index(request, DEFAULT);
		} catch (IOException e) {
			throw new IndexCreateException(e);
		}
		return response.getId();
	}
	
	/**
	 * Index方式创建文档
	 * 如果索引不存在, 就创建新的文档;
	 * 如果文档存在, 就删除文档, 新的文档将被索引, id不变
	 * 版本信息+1
	 *
	 * @param index
	 * @param doc
	 * @return String 文档ID
	 */
	public String save(String index, String doc, String id) {
		IndexRequest request = new IndexRequest(index);
		request.source(doc, XContentType.JSON);
		request.opType(OpType.INDEX);
		if (isNotBlank(id)) {
			request.id(id);
		}
		
		try {
			IndexResponse indexResponse = client.index(request, DEFAULT);
			String docId = indexResponse.getId();
			if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
				log.info("已创建 {}", docId);
			} else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
				log.info("已更新 {}", docId);
			}
			return docId;
		} catch (ElasticsearchException e) {
			log.error("", e);
			throw new DocumentSaveException(e);
		} catch (IOException e) {
			log.error("保存文档失败", e);
			throw new DocumentSaveException(e);
		}
	}
	
	private String toJson(Object doc) {
		if (doc == null) {
			return null;
		}
		
		if (doc instanceof String) {
			return (String) doc;
		}
		
		return JacksonUtils.toJson(doc);
	}
}
