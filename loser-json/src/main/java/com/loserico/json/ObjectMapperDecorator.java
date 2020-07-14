package com.loserico.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.loserico.json.jackson.deserializer.EnumDeserializer;
import com.loserico.json.jackson.deserializer.LocalDateDeserializer;
import com.loserico.json.jackson.deserializer.LocalDateTimeDeserializer;
import com.loserico.json.jackson.serializer.LocalDateTimeSerializer;
import com.loserico.json.resource.PropertyReader;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Set;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * ObjectMapper装饰器
 * 用法: 
 * ObjectMapperDecorator decorator = new ObjectMapperDecorator();
 * decorator.decorate(objectMapper);
 * 
 * <p>
 * Copyright: (C), 2020/4/30 10:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ObjectMapperDecorator {
	
	/**
	 * 这里可以自定义Jackson的一些行为, 默认读取的是classpath根目录下的jackson.properties文件
	 * <p>
	 * #序列化/反序列化优先采用毫秒数方式还是日期字符串形式, false表示采用日期字符串
	 * jackson.epoch.date=false
	 */
	private static final PropertyReader propertyReader = new PropertyReader("jackson");
	private static Set<String> enumProperties = propertyReader.getStringAsSet("loser.jackson.enum.propertes");
	private static boolean epochBased = propertyReader.getBoolean("loser.jackson.epochBased", false);
	private static boolean ignorePropertiesCase = propertyReader.getBoolean("loser.jackson.ignore_case", false);
	private static boolean failOnUnknownProperties = propertyReader.getBoolean("loser.jackson.fail.on.unknown.properties", false);
	
	public ObjectMapper decorate(ObjectMapper objectMapper) {
		if (epochBased) {
			epochMilisBasedObjectMapper(objectMapper);
		} else {
			stringBasedObjectMapper(objectMapper);
		}
		
		if (!enumProperties.isEmpty()) {
			SimpleModule customModule = new SimpleModule();
			customModule.setDeserializerModifier(new BeanDeserializerModifier() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public JsonDeserializer modifyEnumDeserializer(DeserializationConfig config,
				                                               final JavaType type,
				                                               BeanDescription beanDesc,
				                                               final JsonDeserializer<?> deserializer) {
					
					EnumDeserializer enumDeserializer = new EnumDeserializer((Class<Enum<?>>) type.getRawClass(), enumProperties);
					return enumDeserializer;
				}
				
			});
			
			objectMapper.registerModule(customModule);
		}
		
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, ignorePropertiesCase);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
		/*
		 * 用来处理没有默认构造函数的bean
		 * POJO的有参构造函数需要标注@JsonCreator
		 */
		objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
		return objectMapper;
	}
	
	private ObjectMapper stringBasedObjectMapper(ObjectMapper objectMapper) {
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class, new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer(ofPattern("yyyy-MM-dd HH:mm:ss")));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ofPattern("yyyy-MM-dd HH:mm:ss")));
		
		javaTimeModule.addSerializer(LocalDate.class, new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer(ofPattern("yyyy-MM-dd")));
		javaTimeModule.addDeserializer(LocalDate.class, new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer(ofPattern("yyyy-MM-dd")));
		
		javaTimeModule.addSerializer(LocalTime.class, new com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer(ofPattern("HH:mm:ss")));
		javaTimeModule.addDeserializer(LocalTime.class, new com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer(ofPattern("HH:mm:ss")));
		
		DateTimeFormatter epochMilisFormatter = epocMillisFormatter();
		
		/*
		 * 如果在Spring环境使用, 从Spring容器中拿到的objectMapper实例已经注册过javaTimeModule
		 * 默认是不支持重复注册的, 即我们这里注册的会被忽略, 
		 * 所以需要禁用IGNORE_DUPLICATE_MODULE_REGISTRATIONS, 该选项默认是开启的
		 */
		objectMapper.disable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
		objectMapper.registerModule(javaTimeModule);
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(epochMilisFormatter));
		
		/*
		 * java.util.Date 序列化格式
		 */
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(simpleDateFormat);
		
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
				.withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		
		return objectMapper;
	}
	
	private ObjectMapper epochMilisBasedObjectMapper(ObjectMapper objectMapper) {
		// formatter that accepts an epoch millis value
		DateTimeFormatter epochMilisFormatter = epocMillisFormatter();
		
		JavaTimeModule module = new JavaTimeModule();
		module.addSerializer(LocalDate.class, new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer(ofPattern("yyyy-MM-dd")));
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
		
		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
		module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(epochMilisFormatter));
		
		module.addSerializer(LocalTime.class, new LocalTimeSerializer(ofPattern("HH:mm:ss")));
		objectMapper.registerModule(module);
		
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
				.withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		
		return objectMapper;
	}
	
	private DateTimeFormatter epocMillisFormatter() {
		return new DateTimeFormatterBuilder()
					.appendValue(ChronoField.INSTANT_SECONDS, 1, 19, SignStyle.NEVER)
					.appendValue(ChronoField.MILLI_OF_SECOND, 3)
					.toFormatter().withZone(ZoneOffset.ofHours(8));
	}
	
	
	public Set<String> getEnumProperties() {
		return enumProperties;
	}
	
	public void setEnumProperties(Set<String> enumProperties) {
		this.enumProperties = enumProperties;
	}
	
	public boolean isEpochBased() {
		return epochBased;
	}
	
	public void setEpochBased(boolean epochBased) {
		this.epochBased = epochBased;
	}
	
	public boolean isIgnorePropertiesCase() {
		return ignorePropertiesCase;
	}
	
	public void setIgnorePropertiesCase(boolean ignorePropertiesCase) {
		this.ignorePropertiesCase = ignorePropertiesCase;
	}
	
	public boolean isFailOnUnknownProperties() {
		return failOnUnknownProperties;
	}
	
	public void setFailOnUnknownProperties(boolean failOnUnknownProperties) {
		this.failOnUnknownProperties = failOnUnknownProperties;
	}
}
