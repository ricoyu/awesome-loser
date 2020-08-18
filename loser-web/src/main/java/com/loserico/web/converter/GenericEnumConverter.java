package com.loserico.web.converter;

import com.loserico.common.lang.utils.EnumUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * 先按name匹配Enum
 * 其次如果是数字，按ordinal匹配Enum
 * 最后按制定的属性匹配
 * <p>
 * Copyright: Copyright (c) 2018-05-31 14:40
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 */
public class GenericEnumConverter implements GenericConverter {
	
	private Set<String> properties = new HashSet<>();
	
	//TODO 先按属性匹配，匹配不到，如果是数字，按ordinal匹配，如果是字符串，按name匹配
	private boolean propertyFirst = true;
	
	public GenericEnumConverter() {
		
	}
	
	public GenericEnumConverter(Set<String> properties) {
		this.properties = properties;
	}
	
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> convertiblePairs = new HashSet<>();
		convertiblePairs.add(new ConvertiblePair(String.class, Enum.class));
		convertiblePairs.add(new ConvertiblePair(int.class, Enum.class));
		convertiblePairs.add(new ConvertiblePair(long.class, Enum.class));
		return convertiblePairs;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		Assert.notNull(targetType, "Target type to convert to cannot be null");
		if (sourceType == null) {
			Assert.isTrue(source == null, "Source must be [null] if source type == [null]");
			return null;
		}
		if (source != null && !sourceType.getObjectType().isInstance(source)) {
			throw new IllegalArgumentException("Source to convert from must be an instance of [" +
					sourceType + "]; instead it was a [" + source.getClass().getName() + "]");
		}
		
		Enum result = null;
		if (propertyFirst) {
			for (String property : properties) {
				result = EnumUtils.lookupEnum(targetType.getObjectType(), source, property);
				if (result != null) {
					return result;
				}
			}
			if (result == null) {
				result = EnumUtils.lookupEnum(targetType.getObjectType(), source);
			}
		} else {
			result = EnumUtils.lookupEnum(targetType.getObjectType(), source);
			
			if (result == null) {
				for (String property : properties) {
					result = EnumUtils.lookupEnum(targetType.getObjectType(), source, property);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return result;
	}
	
	public Set<String> getProperties() {
		return properties;
	}
	
	public void setProperties(Set<String> properties) {
		this.properties = properties;
	}
	
	public boolean isPropertyFirst() {
		return propertyFirst;
	}
	
	public void setPropertyFirst(boolean propertyFirst) {
		this.propertyFirst = propertyFirst;
	}
	
}
