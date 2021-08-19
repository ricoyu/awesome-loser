package com.loserico.json.jackson.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.loserico.common.lang.utils.ReflectionUtils;
import lombok.SneakyThrows;

/**
 * <p>
 * Copyright: (C), 2021-08-13 13:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class LoserSerializerFactory {
	
	@SneakyThrows
	public static JsonSerializer<?> createEnumSerializer(SerializerProvider prov, BeanProperty beanProperty) {
		/*
		 * 如果Enum是像这个在某个属性上标注了@JsonValue的, 要优先找一下
		 * private enum AttackDirection {
		 * 
		 *   II("in_in", "横向攻击");
		 *   
		 *   @JsonValue
		 *   private String key;
		 *   
		 *   private String desc;
		 *
		 * }
		 */
		JsonSerializer<?> serializer = findSerializerByAnnotations(prov, beanProperty);
		if (serializer != null) {
			return serializer;
		}
		
		BeanSerializerFactory serializerFactory = (BeanSerializerFactory) ReflectionUtils.getFieldValue("_serializerFactory", prov);
		
		JavaType origType = beanProperty.getType();
		SerializationConfig config = prov.getConfig();
		BeanDescription beanDesc = config.introspect(origType);
		
		JavaType type;
		AnnotationIntrospector intr = config.getAnnotationIntrospector();
		if (intr == null) {
			type = origType;
		} else {
			try {
				type = intr.refineSerializationType(config, beanDesc.getClassInfo(), origType);
			} catch (JsonMappingException e) {
				return prov.reportBadTypeDefinition(beanDesc, e.getMessage());
			}
		}
		
		boolean staticTyping;
		if (type == origType) { // no changes, won't force static typing
			staticTyping = false;
		} else { // changes; assume static typing; plus, need to re-introspect if class differs
			staticTyping = true;
			if (!type.hasRawClass(origType.getRawClass())) {
				beanDesc = config.introspect(type);
			}
		}
		
		JsonFormat.Value format = beanDesc.findExpectedFormat(null);
		if (format != null && format.getShape() == JsonFormat.Shape.OBJECT) {
			// one special case: suppress serialization of "getDeclaringClass()"...
			((BasicBeanDescription) beanDesc).removeProperty("declaringClass");
			// returning null will mean that eventually BeanSerializer gets constructed
			return null;
		}
		
		SerializerFactoryConfig _factoryConfig = ReflectionUtils.getFieldValue("_factoryConfig", serializerFactory);
		@SuppressWarnings("unchecked")
		Class<Enum<?>> enumClass = (Class<Enum<?>>) origType.getRawClass();
		JsonSerializer<?> ser = EnumSerializer.construct(enumClass, config, beanDesc, format);
		// [databind#120]: Allow post-processing
		if (_factoryConfig.hasSerializerModifiers()) {
			for (BeanSerializerModifier mod : _factoryConfig.serializerModifiers()) {
				ser = mod.modifyEnumSerializer(config, origType, beanDesc, ser);
			}
		}
		if (ser != null && ser instanceof ContextualSerializer) {
			ser = ((ContextualSerializer) ser).createContextual(prov, beanProperty);
		}
		return ser;
	}
	
	private static JsonSerializer<?> findSerializerByAnnotations(SerializerProvider prov, BeanProperty beanProperty)
			throws JsonMappingException
	{
		JavaType type = beanProperty.getType();
		SerializationConfig config = prov.getConfig();
		BeanDescription beanDesc = config.introspect(type);
		Class<?> raw = type.getRawClass();
		// First: JsonSerializable?
		if (JsonSerializable.class.isAssignableFrom(raw)) {
			return SerializableSerializer.instance;
		}
		// Second: @JsonValue for any type
		AnnotatedMember valueAccessor = beanDesc.findJsonValueAccessor();
		if (valueAccessor != null) {
			if (prov.canOverrideAccessModifiers()) {
				ClassUtil.checkAndFixAccess(valueAccessor.getMember(), prov.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
			}
			JsonSerializer<Object> ser = findSerializerFromAnnotation(prov, valueAccessor);
			return new JsonValueSerializer(valueAccessor, ser);
		}
		// No well-known annotations...
		return null;
	}
	
	private static JsonSerializer<Object> findSerializerFromAnnotation(SerializerProvider prov, Annotated a) throws JsonMappingException {
		Object serDef = prov.getAnnotationIntrospector().findSerializer(a);
		if (serDef == null) {
			return null;
		}
		JsonSerializer<Object> ser = prov.serializerInstance(a, serDef);
		// One more thing however: may need to also apply a converter:
		return (JsonSerializer<Object>) findConvertingSerializer(prov, a, ser);
	}
	
	private static JsonSerializer<?> findConvertingSerializer(SerializerProvider prov, Annotated a, JsonSerializer<?> ser) throws JsonMappingException {
		Converter<Object,Object> conv = findConverter(prov, a);
		if (conv == null) {
			return ser;
		}
		JavaType delegateType = conv.getOutputType(prov.getTypeFactory());
		return new StdDelegatingSerializer(conv, delegateType, ser);
	}
	
	private static Converter<Object,Object> findConverter(SerializerProvider prov, Annotated a) throws JsonMappingException {
		Object convDef = prov.getAnnotationIntrospector().findSerializationConverter(a);
		if (convDef == null) {
			return null;
		}
		return prov.converterInstance(a, convDef);
	}
}
