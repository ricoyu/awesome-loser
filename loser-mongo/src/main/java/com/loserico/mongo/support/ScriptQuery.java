package com.loserico.mongo.support;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.Query;

/**
 * <p>
 * Copyright: (C), 2020-09-14 16:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ScriptQuery extends Query {
	
	private String jsonOperation;
	
	public ScriptQuery(String jsonOperation) {
		this.jsonOperation = jsonOperation;
	}
	
	@Override
	public Document getQueryObject() {
		return Document.parse(jsonOperation);
	}
	
	
	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			Document sort = Document.parse("{\"salary\": -1, \"dep\": 1}");
			sort.forEach((key, value) -> {
				System.out.println("key: " + key + ", value: " + value);
			});
			System.out.println("------------");
		}
	}
	
}
