package com.loserico.searchlegacy.support;

import com.loserico.searchlegacy.enums.ContextType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Elasticsearch 的 Context Suggester 需要在Mapping中加入Context信息
 * <p>
 * Copyright: (C), 2021-02-15 10:17
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class FieldContext {
	
	private ContextType type;
	
	private String name;
	
	public FieldContext(ContextType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>(2);
		map.put("type", type.toString());
		map.put("name", name);
		return map;
	}
}
