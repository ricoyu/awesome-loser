package com.loserico.web.converter;

import com.google.common.base.Splitter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Spring 绑定逗号隔开的请求参数到数组类型
 * <p>
 * Copyright: Copyright (c) 2018-05-23 14:57
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class StringToArrayConverter implements GenericConverter {

	private static final Class<Integer[]> INT_ARR = Integer[].class;
	private static final Class<Long[]> LONG_ARR = Long[].class;
	private static final Class<Double[]> DOUBLE_ARR = Double[].class;
	private static final Class<Float[]> FLOAT_ARR = Float[].class;
	private static final Class<BigDecimal[]> BIGDECIMAL_ARR = BigDecimal[].class;
	private static final Class<String[]> STRING_ARR = String[].class;
	private static final Class<Character[]> CHAR_ARR = Character[].class;
	private static final Class<Boolean[]> BOOLEAN_ARR = Boolean[].class;

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> convertiblePairs = new HashSet<>();
		convertiblePairs.add(new ConvertiblePair(String.class, INT_ARR));
		convertiblePairs.add(new ConvertiblePair(String.class, LONG_ARR));
		convertiblePairs.add(new ConvertiblePair(String.class, DOUBLE_ARR));
		convertiblePairs.add(new ConvertiblePair(String.class, FLOAT_ARR));
		convertiblePairs.add(new ConvertiblePair(String.class, BIGDECIMAL_ARR));
		convertiblePairs.add(new ConvertiblePair(String.class, STRING_ARR));
		convertiblePairs.add(new ConvertiblePair(String.class, CHAR_ARR));
		convertiblePairs.add(new ConvertiblePair(String.class, BOOLEAN_ARR));
		return convertiblePairs;
	}

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

		String value = (String) source;
		if (isBlank(value)) {
			return null;
		}
		List<String> values = Splitter.on(",")
				.trimResults()
				.omitEmptyStrings()
				.splitToList(value);
		int len = values.size();
		Class<?> targetClazz = targetType.getObjectType();
		if (targetClazz == INT_ARR) {
			Integer[] intArr = new Integer[len];
			for (int i = 0; i < intArr.length; i++) {
				intArr[i] = Integer.parseInt(values.get(i));
			}
			return intArr;
		}
		if (targetClazz == LONG_ARR) {
			Long[] longArr = new Long[len];
			for (int i = 0; i < longArr.length; i++) {
				longArr[i] = Long.parseLong(values.get(i));
			}
			return longArr;
		}
		if (targetClazz == DOUBLE_ARR) {
			Double[] doubleArr = new Double[len];
			for (int i = 0; i < doubleArr.length; i++) {
				doubleArr[i] = Double.parseDouble(values.get(i));
			}
			return doubleArr;
		}
		if (targetClazz == FLOAT_ARR) {
			Float[] floatArr = new Float[len];
			for (int i = 0; i < floatArr.length; i++) {
				floatArr[i] = Float.parseFloat(values.get(i));
			}
			return floatArr;
		}
		if (targetClazz == BIGDECIMAL_ARR) {
			BigDecimal[] bigDecimalArr = new BigDecimal[len];
			for (int i = 0; i < bigDecimalArr.length; i++) {
				bigDecimalArr[i] = new BigDecimal(values.get(i));
			}
			return bigDecimalArr;
		}
		if (targetClazz == STRING_ARR) {
			String[] strArr = new String[len];
			for (int i = 0; i < strArr.length; i++) {
				strArr[i] = values.get(i);
			}
			return strArr;
		}
		if (targetClazz == CHAR_ARR) {
			Character[] charArr = new Character[len];
			for (int i = 0; i < charArr.length; i++) {
				charArr[i] = values.get(i).charAt(0);
			}
			return charArr;
		}
		if (targetClazz == BOOLEAN_ARR) {
			Boolean[] booleanArr = new Boolean[len];
			for (int i = 0; i < booleanArr.length; i++) {
				booleanArr[i] = Boolean.valueOf(values.get(i));
			}
			return booleanArr;
		}

		return null;
	}

}
