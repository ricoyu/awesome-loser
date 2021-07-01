package com.loserico.search.support;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.annotation.Index;
import com.loserico.search.builder.admin.ElasticPutMappingBuilder;
import com.loserico.search.builder.admin.FieldDefBuilder;
import com.loserico.search.enums.Analyzer;
import com.loserico.search.enums.Dynamic;
import com.loserico.search.enums.FieldType;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * <p>
 * Copyright: (C), 2021-05-06 14:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class MappingSupport {
	
	public static ElasticPutMappingBuilder extractIndexMapping(Class entity) {
		if (entity == null) {
			return null;
		}
		
		Index index = (Index)entity.getAnnotation(Index.class);
		if (index == null) {
			return null;
		}
		
		String indexName = index.value();
		
		
		/*
		 * 这两个是索引级别Mapping设置
		 */
		Dynamic dynamic = index.dynamic();
		boolean sourceEnabled = index.sourceEnabled();
		
		ElasticPutMappingBuilder builder = new ElasticPutMappingBuilder(indexName, dynamic);
		builder.sourceEnabled(sourceEnabled);
		
		//获取字段级别的Mapping设置
		Set<Field> fields = ReflectionUtils.filterFieldByAnnotation(entity, com.loserico.search.annotation.Field.class);
		for (Field field : fields) {
			com.loserico.search.annotation.Field annotation = field.getAnnotation(com.loserico.search.annotation.Field.class);
			//ES 中的字段名
			String fieldName = annotation.value();
			//如果没有指定ES字段名, 那么默认取Entity的字段名
			if (isBlank(fieldName)) {
				fieldName = field.getName();
			}
			FieldType fieldType = determineFieldType(annotation.type(), field);
			
			//只有TEXT类型的字段才需要分词
			Analyzer analyzer = fieldType == FieldType.TEXT ? annotation.analyzer() : null;
			Analyzer searchAnalyzer = fieldType == FieldType.TEXT ? annotation.searchAnalyzer() : null;
			
			String copyTo = annotation.copyTo();
			boolean eagerGlobalOrdinals = annotation.eagerGlobalOrdinals();
			boolean enabled = annotation.enabled();
			boolean enableIndex = annotation.index();
			String nullValue = annotation.nullValue();
			boolean store = annotation.store();
			String format = annotation.format();
			
			FieldDefBuilder fieldDefBuilder = FieldDefBuilder.builder(fieldName, fieldType)
					.analyzer(analyzer)
					.searchAnalyzer(searchAnalyzer)
					.copyTo(copyTo)
					.enabled(enabled)
					.index(enableIndex)
					.eagerGlobalOrdinals(eagerGlobalOrdinals)
					.store(store)
					.format(format);
			builder.field(fieldDefBuilder);
		}
		
		return builder;
	}
	
	private static FieldType determineFieldType(FieldType fieldType, Field field) {
		//因为FieldType.TEXT是默认值, 如果拿到的fieldType不是TEXT类型, 说明显式设置过, 不需要动态判断
		if (fieldType != FieldType.TEXT) {
			return fieldType;
		}
		
		//如果fieldType是TEXT类型, 并且字段是String类型, 那么不需要动态判断
		if (field.getType() == String.class) {
			return fieldType;
		}
		
		Class<?> type = field.getType();
		if (type == Integer.class) {
			return FieldType.INTEGER;
		}
		
		if (type == Long.class) {
			return FieldType.LONG;
		}
		
		if (type == LocalDateTime.class || type == Date.class) {
			return FieldType.DATE;
		}
		
		if (type == Double.class) {
			return FieldType.DOUBLE;
		}
		
		return FieldType.KEYWORD;
	}
}
