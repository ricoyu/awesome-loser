package com.loserico.web.context.support;

import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.HashSet;
import java.util.Set;

public class CustomConversionServiceFactoryBean extends ConversionServiceFactoryBean {

	private Set<String> properties = new HashSet<>();
	
	@Override
	protected GenericConversionService createConversionService() {
		CustomConversionService conversionService = new CustomConversionService();
		if(!properties.isEmpty()) {
			conversionService.setProperties(properties);
		}
		conversionService.afterPropertiesSet();
		return conversionService;
	}

	public Set<String> getProperties() {
		return properties;
	}

	public void setProperties(Set<String> properties) {
		this.properties = properties;
	}

}
