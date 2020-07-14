package com.loserico.web.context.support;

import com.loserico.web.converter.GenericEnumConverter;
import com.loserico.web.converter.StringToArrayConverter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.HashSet;
import java.util.Set;

public class CustomConversionService extends DefaultConversionService {
	
	private Set<String> properties = new HashSet<>();
	
	/**
	 * Hook method to lookup the converter for a given sourceType/targetType pair.
	 * First queries this ConversionService's converter cache.
	 * On a cache miss, then performs an exhaustive search for a matching converter.
	 * If no converter matches, returns the default converter.
	 * @param sourceType the source type to convert from
	 * @param targetType the target type to convert to
	 * @return the generic converter that will perform the conversion,
	 * or {@code null} if no suitable converter was found
	 * @see #getDefaultConverter(TypeDescriptor, TypeDescriptor)
	 */
	protected GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
		if(Enum.class.isAssignableFrom(targetType.getObjectType())) {
			return new GenericEnumConverter(properties);
		}
		return super.getConverter(sourceType, targetType);
	}

	public Set<String> getProperties() {
		return properties;
	}

	public void setProperties(Set<String> properties) {
		this.properties = properties;
	}
	
	public void afterPropertiesSet() {
		addConverter(new StringToArrayConverter());
	}
}
