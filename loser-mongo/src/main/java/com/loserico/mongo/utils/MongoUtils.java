package com.loserico.mongo.utils;

import com.loserico.common.lang.transformer.Transformers;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.mongo.annotation.MongoField;
import com.loserico.mongo.annotation.MongoId;
import com.loserico.mongo.annotation.MongoTransient;
import com.loserico.mongo.exception.InstanceCreationException;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2020-12-04 16:42
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class MongoUtils {
	
	/**
	 * 转成指定的对象列表
	 *
	 * @param cursor
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> toList(MongoCursor<Document> cursor, Class<T> clazz) {
		if (cursor == null || !cursor.hasNext()) {
			return Collections.emptyList();
		}
		
		List<T> results = new ArrayList<>();
		while (cursor.hasNext()) {
			Document document = cursor.next();
			T t = toObject(document, clazz);
			results.add(t);
		}
		cursor.close();
		
		return results;
	}
	
	/**
	 * 将Document对象转成POJO
	 *
	 * @param document
	 * @param clazz
	 * @param <T>
	 * @return T
	 */
	public static <T> T toObject(Document document, Class<T> clazz) {
		if (document == null) {
			return null;
		}
		
		Objects.requireNonNull(clazz, "Class cannot be null!");
		
		/*
		 * 先通过反射创建对象实例
		 */
		T target = null;
		try {
			target = clazz.newInstance();
		} catch (Exception e) {
			log.error("创建对象实例异常[" + clazz.getName() + "]", e);
			throw new InstanceCreationException(e);
		}
		
		/*
		 * 拿到这个class的所有字段, 包括自己定义的和父类中定义的字段, 但是不包括Object对象中的字段
		 */
		Field[] fields = ReflectionUtils.getFields(clazz);
		for (Field field : fields) {
			//如果字段加了@MongoTransient注解的话, 这个字段就不处理了
			if (field.getAnnotation(MongoTransient.class) != null) {
				continue;
			}
			
			/*
			 * 处理主键_id
			 */
			MongoId mongoId = field.getAnnotation(MongoId.class);
			if (mongoId != null) {
				String value = document.get("_id").toString();
				ReflectionUtils.setField(field, target, Transformers.convert(value, field.getType()));
				continue;
			}
			
			Class fieldType = field.getType();
			
			//TODO 字段为Map类型的暂时先不处理?
			if (Map.class.isAssignableFrom(fieldType)) {
				continue;
			}
			
			/*
			 * 接下来确定MongoDB文档的字段名称
			 */
			String fieldName = null;
			MongoField mongoField = field.getAnnotation(MongoField.class);
			//取@MongoField的value属性作为字段名
			if (mongoField != null) {
				fieldName = mongoField.value();
			}
			//如果没有加@MongoField, 或者加了@MongoField, 但是其value属性为"", 那么取这个字段本身的名字
			if (isBlank(fieldName)) {
				fieldName = field.getName();
			}
			
			//从文档中根据字段名取出对应的值
			Object docFieldValue = document.get(fieldName);
			
			//表示这个document对象内嵌了一个子对象
			if (docFieldValue instanceof Document) {
				Document nestedDocument = (Document) docFieldValue;
				//递归转换Document对象到POJO
				Object value = toObject(nestedDocument, field.getType());
				//给字段赋值
				ReflectionUtils.setField(field, target, value);
				continue;
			}
			
			/*
			 * 处理list类型的字段
			 */
			if (docFieldValue instanceof List) {
				List docFieldValues = (List) docFieldValue;
				List results = new ArrayList();
				
				/*
				 * 从文档出取出的值是一个List类型, 那么认为POJO中对应的field也是一个List类型的, 所以先拿到这个List里面存放的element类型
				 */
				ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
				Class fieldElementType = (Class) parameterizedType.getActualTypeArguments()[0];
				
				for (Object docElementValue : docFieldValues) {
					/*
					 * 这又是一个内嵌的文档类型
					 */
					if (docElementValue instanceof Document) {
						Document nestedDocument = (Document) docElementValue;
						
						//递归转换Document对象到POJO
						Object value = toObject(nestedDocument, fieldElementType);
						results.add(value);
					} else {
						results.add(Transformers.convert(docElementValue, fieldElementType));
					}
				}
				
				ReflectionUtils.setField(field, target, results);
				continue;
			}
			
			/*
			 * 如果字段是原子类型, 但是Mongo中取出的值是null, 或者说某个document不存在这个字段, 此时取出的值也是null
			 * 那么就不要给原子类型字段赋值了
			 */
			if (fieldType.isPrimitive() && docFieldValue == null) {
				continue;
			}
			
			//从Document对象取出的值不是Map, 也不是List, 那么认为它是一个简单类型, 直接给POJO字段赋值
			ReflectionUtils.setField(field, target, Transformers.convert(docFieldValue, fieldType));
		}
		
		return target;
	}

}
