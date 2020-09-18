package com.loserico.mongo.support;

import com.loserico.tokenparser.utils.ParserUtils;
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
	
	public ScriptQuery(String jsonOperation, Object param) {
		this.jsonOperation = ParserUtils.parse(jsonOperation, param);
	}
	
	@Override
	public Document getQueryObject() {
		return Document.parse(jsonOperation);
	}
}
