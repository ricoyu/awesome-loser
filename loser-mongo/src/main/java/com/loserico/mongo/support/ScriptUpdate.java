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
	
	private static final String SET_BEGIN = "{'$set': ";
	private static final String SET_END = "}";
	
	public static Update toUpdate(String update) {
		return Update.fromDocument(Document.parse(update));
	}
	
	/**
	 * Mongo $set 用法
	 * 只更新指定的字段, 而不是整片文档替换
	 *
	 * @param update
	 * @param param
	 * @return
	 */
	public static Update toSetUpdate(String update, Object param) {
		String updatejson = ParserUtils.parse(update, param);
		String setJson = SET_BEGIN + updatejson + SET_END;
		return Update.fromDocument(Document.parse(setJson));
	}
	
	/**
	 * 更新整篇文档
	 * @param update
	 * @param param
	 * @return
	 */
	public static Update toUpdate(String update, Object param) {
		String updatejson = ParserUtils.parse(update, param);
		return Update.fromDocument(Document.parse(updatejson));
	}
}
