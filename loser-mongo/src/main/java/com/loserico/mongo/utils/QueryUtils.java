package com.loserico.mongo.utils;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * <p>
 * Copyright: (C), 2020-10-23 17:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class QueryUtils {
	
	private MongoTemplate mongoTemplate;
	
	/**
	 * 将MongoDB Entity转成Document对象
	 * 
	 * @param entity
	 * @return
	 */
	/*public static <T> Document toDocument(T entity) {
		AdaptibleEntity<T> entity = operations.forEntity(objectToSave, mongoConverter.getConversionService());
		entity.assertUpdateableIdIfNotSet();
		
		MappedDocument mapped = entity.toMappedDocument(writer);
		Document dbDoc = mapped.getDocument();
	}*/
}
