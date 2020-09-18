package com.loserico.mongo.support;

import com.loserico.tokenparser.utils.ParserUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Update;

/**
 * <p>
 * Copyright: (C), 2020-09-17 15:31
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ScriptUpdate extends Update {
	
	public static Update toUpdate(String update) {
		return Update.fromDocument(Document.parse(update));
	}
	
	public static Update toUpdate(String update, Object param) {
		String updatejson = ParserUtils.parse(update, param);
		return Update.fromDocument(Document.parse(updatejson));
	}
}
