package com.loserico.web.converter;

import com.loserico.common.lang.utils.EnumUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class ObjectToEnumConverterFactory implements ConverterFactory<Object, Enum> {
	
	private Set<String> properties = new HashSet<>();
	
	@SuppressWarnings({"unchecked"})
	@Override
	public <T extends Enum> Converter<Object, T> getConverter(Class<T> targetType) {
		return new ObjectToEnumConverter(targetType, getProperties());
	}
	
	public Set<String> getProperties() {
		return properties;
	}
	
	public void setProperties(Set<String> properties) {
		this.properties = properties;
	}
	
	public final class ObjectToEnumConverter<T extends Enum> implements Converter<Object, Enum> {
		
		private Class<T> enumType;
		
		private Set<String> properties = new HashSet<>();
		
		public ObjectToEnumConverter(Class<T> enumType, Set<String> properties) {
			this.enumType = enumType;
			this.properties = properties;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public T convert(Object source) {
			for (String property : properties) {
				Object resultEnum = EnumUtils.lookupEnum(enumType, source, property);
				if (resultEnum == null) {
					continue;
				}
				return (T) resultEnum;
			}
			return null;
		}
		
	}
}
