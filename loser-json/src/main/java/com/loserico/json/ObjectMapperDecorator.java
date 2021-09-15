package com.loserico.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParser;
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
import com.loserico.common.lang.resource.PropertyReader;
import com.loserico.common.lang.vo.Page;
import com.loserico.common.lang.vo.Result;
import com.loserico.json.jackson.deserializer.DateStr2LongDeserializer;
import com.loserico.json.jackson.deserializer.EnumDeserializer;
import com.loserico.json.jackson.deserializer.LocalDateDeserializer;
import com.loserico.json.jackson.deserializer.LocalDateTimeDeserializer;
import com.loserico.json.jackson.deserializer.PageDeserializer;
import com.loserico.json.jackson.serializer.EnumI18NSerializer;
import com.loserico.json.jackson.serializer.LocalDateTimeSerializer;
import com.loserico.json.jackson.serializer.ResultSerializer;

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
		
		/**
		 * EnumDeserializer支持反序列化字符串到Enum类型, 并且是大小写不敏感的
		 * 正常的话字符串"ASC" 可以反序列化成 Direction.ASC, 而字符串"asc"是不可以反序列化为Direction.ASC的
		 * 加上EnumDeserializer后, 就可以支持字符串大小写不敏感匹配了
		 * 
		 * 如果指定了enumProperties, 那么可以根据指定enum的属性来反序列化为Enum类型, 默认根据ordinal 或者 name
		 */
		SimpleModule customModule = new SimpleModule();
		customModule.addSerializer(Result.class, new ResultSerializer());
		customModule.addSerializer(Enum.class, new EnumI18NSerializer());
		customModule.setDeserializerModifier(new BeanDeserializerModifier() {
			@SuppressWarnings({"unchecked", "rawtypes"})
			@Override
			public JsonDeserializer modifyEnumDeserializer(DeserializationConfig config,
			                                               final JavaType type,
			                                               BeanDescription beanDesc,
			                                               final JsonDeserializer<?> deserializer) {
				return new EnumDeserializer((Class<Enum<?>>) type.getRawClass(), enumProperties, deserializer);
			}
			
		});
		//Page对象在序列化的时候不希望把order输出到json, 但是反序列化的时候要可以接收order
		customModule.addDeserializer(Page.class, new PageDeserializer(Page.class));
		customModule.addDeserializer(Long.class, new DateStr2LongDeserializer());
		objectMapper.registerModule(customModule);
		
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, ignorePropertiesCase);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
		//JSON字符串field允许使用单引号括起来, 标准JSON是不允许的, 只可以使用双引号
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		//对POJO字段排序
		objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
		//对Map字段排序
		objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		/*
		 * 用来处理没有默认构造函数的bean
		 * POJO的有参构造函数需要标注@JsonCreator
		 */
		objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
		
		//注册xss解析器, 去掉注释的原因是request body如果有 < 这种, 都会被替换成&lt, 但是如果用户就是要提交<呢?
		/*SimpleModule xssModule = new SimpleModule("XssStringJsonModule");
		xssModule.addSerializer(String.class, new XssStringJsonSerializer());
		xssModule.addDeserializer(String.class, new XssStringJsonDeserializer());
		objectMapper.registerModule(xssModule);*/
		
		//系列化字符串时候, Jackson会把双引号转义, 如\", 这里配置不需要转义
		//objectMapper.getFactory().setCharacterEscapes(new CustomCharacterEscapes());
		return objectMapper;
	}
	
	private ObjectMapper stringBasedObjectMapper(ObjectMapper objectMapper) {
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class, new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer(ofPattern("yyyy-MM-dd HH:mm:ss")));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ofPattern("yyyy-MM-dd HH:mm:ss")));
		
		javaTimeModule.addSerializer(LocalDate.class, new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer(ofPattern("yyyy-MM-dd")));
		//javaTimeModule.addDeserializer(LocalDate.class, new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer(ofPattern("yyyy-MM-dd")));
		javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
		
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
				.withFieldVisibility(JsonAutoDetect.Visibility.NONE)
				.withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
				.withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY));
		
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
				.withFieldVisibility(JsonAutoDetect.Visibility.NONE)
				.withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
				.withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY));
		
		return objectMapper;
	}
	
	private DateTimeFormatter epocMillisFormatter() {
		return new DateTimeFormatterBuilder()
				.appendValue(ChronoField.INSTANT_SECONDS, 1, 19, SignStyle.NEVER)
				.appendValue(ChronoField.MILLI_OF_SECOND, 3)
				.toFormatter().withZone(ZoneOffset.ofHours(8));
	}
	
	
	/*public Set<String> getEnumProperties() {
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
	}*/
}
