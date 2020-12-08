package com.loserico.search.index;

import com.loserico.search.exception.IndexCreateException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;

/**
 * Elasticsearch 索引相关操作
 * <p>
 * Copyright: (C), 2020-12-03 20:33
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class IndexRequests {
	
	private RestHighLevelClient client;
	
	public IndexRequests(RestHighLevelClient client) {
		this.client = client;
	}
	
	/**
	 * 创建索引, 默认1个分片, 0个副本
	 *
	 * @param index 索引名
	 * @return boolean 创建成功失败标识
	 */
	public boolean create(String index) {
		return create(index, 1, 0);
	}
	
	/**
	 * 创建索引
	 *
	 * @param index    索引名
	 * @param shards   分片数
	 * @param replicas 副本数
	 * @return boolean 创建成功失败标识
	 */
	public boolean create(String index, int shards, int replicas) {
		CreateIndexRequest request = new CreateIndexRequest(index);
		request.settings(Settings.builder()
				.put("index.number_of_shards", shards) 
				.put("index.number_of_replicas", replicas));
		try {
			CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
			return response.isAcknowledged();
		} catch (ElasticsearchStatusException e) {
			log.warn("创建索引失败, {}", e.getMessage());
			return false;
		} catch (IOException e) {
			log.error("创建索引:" + index + "失败", e);
			throw new IndexCreateException(e);
		}
		
	}
}
