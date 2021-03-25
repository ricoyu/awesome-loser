package com.loserico.search.builder;

import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.exception.DocumentGetException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetRequest.Item;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * Copyright: (C), 2020-12-25 8:58
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
@Deprecated
public final class MultiGetBuilder<T> {
	
	private RestHighLevelClient client;
	
	private List<Item> items = new ArrayList<>();
	
	private Class<T> clazz;
	
	public MultiGetBuilder(RestHighLevelClient client) {
		this.client = client;
	}
	
	public MultiGetBuilder add(String index, String id) {
		items.add(new Item(index, id));
		return this;
	}
	
	public MultiGetBuilder add(String index, List<String> ids) {
		ids.stream().map(id -> new Item(index, id))
				.forEach(item -> items.add(item));
		return this;
	}
	
	public MultiGetBuilder add(Map<String, String> params) {
		for(Map.Entry<String, String> entry : params.entrySet()) {
			items.add(new Item(entry.getKey(), entry.getValue()));
		}
		return this;
	}
	
	public MultiGetBuilder resultType(Class<T> clazz) {
		this.clazz = clazz;
		return this;
	}
	
	public List<T> request() {
		MultiGetRequest request = new MultiGetRequest();
		items.forEach(item -> request.add(item));
		try {
			MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
			MultiGetItemResponse[] responses = response.getResponses();
			return Arrays.asList(responses).stream()
					.map((multigetItemResponse) -> {
						String source = multigetItemResponse.getResponse().getSourceAsString();
						return JacksonUtils.toObject(source, clazz);
					}).collect(toList());
		} catch (IOException e) {
			log.error("", e);
			throw new DocumentGetException(e);
		}
	}
}
