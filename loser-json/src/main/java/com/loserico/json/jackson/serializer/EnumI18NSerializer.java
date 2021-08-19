package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.loserico.common.lang.i18n.I18N;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.json.jackson.serializer.annotation.EnumI18N;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>
 * Copyright: (C), 2021-08-13 10:17
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class EnumI18NSerializer extends JsonSerializer<Object> implements ContextualSerializer {
	
	private String property;
	
	private String fallbackTo;
	
	private JsonSerializer<Object> enumSerializer;
	
	public EnumI18NSerializer() {
		
	}
	
	public EnumI18NSerializer(String property, String fallbackTo) {
		this.property = property;
		this.fallbackTo = fallbackTo;
	}
	
	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (isBlank(property) && isBlank(fallbackTo)) {
			enumSerializer.serialize(value, gen, serializers);
			return;
		}
		
		String message = null;
		String template = ReflectionUtils.getFieldValue(property, value);
		if (isNotBlank(fallbackTo)) {
			String defaultMessage = ReflectionUtils.getFieldValue(fallbackTo, value);
			message = I18N.i18nMessage(template, defaultMessage);
			gen.writeString(message);
			return;
		}
		
		message = I18N.i18nMessage(template);
		gen.writeString(message);
	}
	
	/**
	 * 其实就是根据要序列化的POJO, 按需给他生成一个JsonSerializer, 因为可以在创建JsonSerializer之前,
	 * 访问POJO的属性, 所以属性的上下文信息可以注入进JsonSerializer
	 *
	 * @param serializerProvider
	 * @param beanProperty
	 * @return
	 * @throws JsonMappingException
	 */
	@Override
	public JsonSerializer<Object> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
		if (beanProperty == null) {
			return serializerProvider.findNullValueSerializer(null);
		}
		
		//只处理enum类型
		if (!Enum.class.isAssignableFrom(beanProperty.getType().getRawClass())) {
			return null;
		}
		//JsonSerializer<Object> enumSerializer = serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		//JsonSerializer<Object> enumSerializer = ReflectionUtils.invokeMethod(serializerProvider, "_createUntypedSerializer", beanProperty.getType());
		JsonSerializer<Object> enumSerializer = (JsonSerializer<Object>) LoserSerializerFactory.createEnumSerializer(serializerProvider, beanProperty);
		EnumI18N enumI18N = beanProperty.getAnnotation(EnumI18N.class);
		if (enumI18N == null) {
			return enumSerializer;
		}
		
		String property = enumI18N.property();
		String fallbackTo = enumI18N.fallbackTo();
		
		EnumI18NSerializer enumI18NSerializer = new EnumI18NSerializer(property, fallbackTo);
		enumI18NSerializer.setEnumSerializer(enumSerializer);
		return enumI18NSerializer;
	}
	
	public void setEnumSerializer(JsonSerializer<Object> enumSerializer) {
		this.enumSerializer = enumSerializer;
	}
}
