package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.loserico.common.lang.vo.Result;

import java.io.IOException;

/**
 * 序列化Result对象, 如果其中的page属性为null, 那么就不输出
 * <p>
 * Copyright: (C), 2020-11-03 17:54
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ResultSerializer extends StdSerializer<Result> {

	public ResultSerializer() {
		this(null);
	}

	public ResultSerializer(Class t) {
		super(t);
	}

	@Override
	public void serialize(Result value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		//0 成功 非0 失败
		gen.writeStringField("code", value.getCode());
		gen.writeStringField("status", value.getStatus());
		/*if ("0".equals(value.getCode())) {
			gen.writeStringField("status", "success");
		} else {
			gen.writeStringField("status", "error");
		}*/

		//非0状态才需要输出错误消息
		if (!"0".equals(value.getCode())) {
			gen.writeObjectField("message", value.getMessage());
		}

		//不是分页查询就不输出page字段
		if (value.getPage() != null) {
			gen.writeObjectField("page", value.getPage());
		}

		//成功状态才有数据返回, 否则data字段都不输出
		if ("0".equals(value.getCode())) {
			gen.writeObjectField("data", value.getData());
		}
		gen.writeEndObject();
	}
}
