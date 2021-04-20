package com.loserico.searchlegacy.builder;

import com.loserico.json.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest.Item;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.loserico.searchlegacy.constants.ElasticConstants.ONLY_TYPE;
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
public final class ElasticMultiGetBuilder<T> {
	
	private TransportClient client;
	
	private List<Item> items = new ArrayList<>();
	
	private Class<T> clazz;
	
	public ElasticMultiGetBuilder(TransportClient client) {
		this.client = client;
	}
	
	public ElasticMultiGetBuilder add(String index, String id) {
		items.add(new Item(index, ONLY_TYPE, id));
		return this;
	}
	
	public ElasticMultiGetBuilder add(String index, List<String> ids) {
		ids.stream().map(id -> new Item(index, ONLY_TYPE, id))
				.forEach(item -> items.add(item));
		return this;
	}
	
	public ElasticMultiGetBuilder resultType(Class<T> clazz) {
		this.clazz = clazz;
		return this;
	}
	
	public List<T> request() {
		MultiGetRequestBuilder multiGetRequestBuilder = client.prepareMultiGet();
		items.forEach(item -> multiGetRequestBuilder.add(item));
		
		MultiGetResponse multiGetItemResponses = multiGetRequestBuilder.get();
		MultiGetItemResponse[] itemResponses = multiGetItemResponses.getResponses();
		List<String> resultJsons = Arrays.asList(itemResponses).stream()
				.map((itemResponse) -> {
					GetResponse response = itemResponse.getResponse();
					if (response.isExists()) {
						return response.getSourceAsString();
					}
					return null;
				})
				.filter(Objects::nonNull)
				.collect(toList());
		
		if (clazz != null) {
			return resultJsons.stream()
					.map(json -> JacksonUtils.toObject(json, clazz))
					.collect(toList());
		}
		
		return (List<T>) resultJsons;
	}
}
