package com.loserico.mongo.support;

import com.loserico.tokenparser.utils.ParserUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

/**
 * 聚合查询
 * <p>
 * Copyright: (C), 2020-09-14 16:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AggregationQuery implements AggregationOperation {
	
	private String json;
	
	public AggregationQuery(String json) {
		this.json = json;
	}
	
	public AggregationQuery(String json, Object param) {
		this.json = ParserUtils.parse(json, param);;
	}
	
	@Override
	public Document toDocument(AggregationOperationContext context) {
		return context.getMappedObject(Document.parse(json));
	}
}
