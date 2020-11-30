package com.loserico.search;

import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.loserico.json.jackson.JacksonUtils.toJson;

/**
 * <p>
 * Copyright: (C), 2020-11-21 10:03
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ESTest {
	
	private static RestHighLevelClient client = null;
	
	@BeforeClass
	@SneakyThrows
	public static void init() {
		RestClientBuilder restClientBuilder;
		client = new RestHighLevelClient(RestClient.builder(
				new HttpHost("192.168.100.104", 9200, "http")));
	}
	
	@AfterClass
	@SneakyThrows
	public static void close() {
		client.close();
	}
	
	/**
	 * 查看索引相关信息
	 * GET kibana_sample_data_ecommerce
	 */
	@Test
	@SneakyThrows
	public void testGetIndex() {
		//The index whose information we want to retrieve
		GetIndexRequest request = new GetIndexRequest("kibana_sample_data_ecommerce");
		//If true, defaults will be returned for settings not explicitly set on the index
		request.includeDefaults(true);
		request.indicesOptions(IndicesOptions.lenientExpandOpen());
		//同步调用
		GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
		
		//Retrieve a Map of different types to MappingMetadata for index.
		MappingMetaData indexMapping = response.getMappings().get("kibana_sample_data_ecommerce");
		Map<String, Object> indexTypeMappings = indexMapping.getSourceAsMap();
		List<AliasMetaData> indexAliases = response.getAliases().get("kibana_sample_data_ecommerce");
		Settings settings = response.getSettings().get("kibana_sample_data_ecommerce");
		CompressedXContent mappings = indexMapping.source();
		
		JSONObject getIndexObject = new JSONObject();
		System.out.println(indexTypeMappings.toString());
		System.out.println(toJson(mappings));
		System.out.println(toJson(indexAliases));
		System.out.println(settings.toString());
	}
	
	/**
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-count.html
	 * 
	 * 查看索引文档总数
	 * GET kibana_sample_data_ecommerce/_count
	 */
	@Test
	@SneakyThrows
	public void testIndexDocCount() {
		CountRequest countRequest = new CountRequest("kibana_sample_data_ecommerce");
		CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
		long count = countResponse.getCount();
		System.out.println(count);
		int failedShards = countResponse.getFailedShards();
		System.out.println(failedShards);
	}
	
	/**
	 * https://www.javacodegeeks.com/2020/01/elasticsearch-sql.html
	 */
	@Test
	public void testSqlQuery() {
		
	}
}
