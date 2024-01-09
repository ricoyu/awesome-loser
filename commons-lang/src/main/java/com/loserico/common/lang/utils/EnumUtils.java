package com.loserico.common.lang.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public final class EnumUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(EnumUtils.class);
	
	/**
	 * 根据value的类型自动解析成对应的enum, 如果value是String类型, 则根据enum的name去匹配, 否则根据ordinal去匹配
	 *
	 * @param clazz
	 * @param value
	 * @return
	 */
	@SuppressWarnings({"rawtypes"})
	public static <T extends Enum> T lookupEnum(Class clazz, Object value) {
		if (value == null) {
			return null;
		}
		Enum result = null;
		if (value instanceof String) {
			result = lookup(clazz, value.toString());
			if (result == null) {
				try {
					Integer propertyValue = Integer.parseInt((String) value);
					return lookup(clazz, propertyValue);
				} catch (NumberFormatException e) {
					logger.trace("msg", e);
					return null;
				}
			} else {
				return (T)result;
			}
		}
		if (value instanceof Long) {
			return lookup(clazz, (Long) value);
		}
		if (value instanceof Integer) {
			return lookup(clazz, (Integer) value);
		}
		if (value instanceof BigInteger) {
			return lookup(clazz, (BigInteger) value);
		}
		return null;
	}
	
	/**
	 * 根据value的类型,并根据指定的enum的某个属性去自动解析成对应的enum
	 *
	 * @param clazz
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Enum> T lookupEnum(Class clazz, Object value, String property) {
		if (value == null) {
			return null;
		}
		
		Enum result = null;
		if (value instanceof String) {
			result = lookup(clazz, value.toString(), property);
			if (result == null) {
				try {
					Integer propertyValue = Integer.parseInt((String) value);
					return lookup(clazz, propertyValue, property);
				} catch (NumberFormatException e) {
					logger.trace("msg", e);
					return null;
				}
			} else {
				return (T)result;
			}
		}
		if (value instanceof Long) {
			return lookup(clazz, (Long) value, property);
		}
		if (value instanceof Integer) {
			return lookup(clazz, (Integer) value, property);
		}
		if (value instanceof BigInteger) {
			return lookup(clazz, (BigInteger) value, property);
		}
		return null;
	}
	
	/**
	 * 根据value的类型,并根据指定的enum的某个属性去自动解析成对应的enum
	 * 如果根据property匹配不到, 回退到downgradeProperty去匹配
	 * <p>
	 * 这是为了应对如下情况的enum:
	 * TO_AUDIT(101, "待审核", "未审核")
	 * Enum的某个值有描述, 还有别名, 解析的时候希望优先根据描述匹配, 匹配不到根据别名匹配
	 *
	 * @param clazz
	 * @param value
	 * @return Enum
	 * @on
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Enum> T lookupEnum(Class clazz, Object value, String property, String downgradeProperty) {
		if (value == null) {
			return null;
		}
		
		Enum result = null;
		if (value instanceof String) {
			result = lookup(clazz, value.toString(), property);
			if (result == null) {
				result = lookup(clazz, value.toString(), downgradeProperty);
				if (result != null) {
					return (T)result;
				}
				try {
					Integer propertyValue = Integer.parseInt((String) value);
					return lookup(clazz, propertyValue, property);
				} catch (NumberFormatException e) {
					logger.trace("msg", e);
					return null;
				}
			} else {
				return (T)result;
			}
		}
		if (value instanceof Long) {
			return lookup(clazz, (Long) value, property);
		}
		if (value instanceof Integer) {
			return lookup(clazz, (Integer) value, property);
		}
		if (value instanceof BigInteger) {
			return lookup(clazz, (BigInteger) value, property);
		}
		return null;
	}
	
	/**
	 * 根据value的类型自动解析成对应的enum
	 *
	 * @param clazz
	 * @param value
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <T extends Enum> T toEnum(Class clazz, Object value) {
		return (T) lookupEnum(clazz, value);
	}
	
	/**
	 * 泛型化的版本
	 *
	 * @param clazz
	 * @param value
	 * @param property
	 * @return T
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T extends Enum> T toEnum(Class<T> clazz, Object value, String property) {
		return (T) lookupEnum(clazz, value, property);
	}
	
	/**
	 * 泛型化的版本
	 *
	 * @param clazz
	 * @param value
	 * @param property
	 * @return T
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T extends Enum> T toEnum(Class<T> clazz, Object value, String property, String downgradeProperty) {
		return (T) lookupEnum(clazz, value, property, downgradeProperty);
	}
	
	/**
	 * 泛型化的版本,支持根据多个property按顺序匹配
	 *
	 * @param clazz
	 * @param value
	 * @param properties
	 * @return T
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T extends Enum> T toEnum(Class<T> clazz, Object value, List<String> properties) {
		if (value == null) {
			return null;
		}
		
		for (String property : properties) {
			Object enumObj = lookupEnum(clazz, value, property);
			if (enumObj != null) {
				return (T) enumObj;
			}
		}
		return null;
	}
	
	/**
	 * 根据ordinal获取enum实例
	 *
	 * @param clazz
	 * @param ordinal
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static <T extends Enum> T lookup(Class clazz, Integer ordinal) {
		if (ordinal == null) {
			return null;
		}
		
		EnumSet enumSet = EnumSet.allOf(clazz);
		if (ordinal < enumSet.size()) {
			Iterator<Enum> iterator = enumSet.iterator();
			for (int i = 0; i < ordinal; i++) {
				iterator.next();
			}
			Enum rval = iterator.next();
			if (rval.ordinal() == ordinal.intValue()) {
				return (T) rval;
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private static <T extends Enum> T lookup(Class clazz, Integer value, String property) {
		if (value == null) {
			return null;
		}
		
		Enum[] consts = (Enum[]) clazz.getEnumConstants();
		for (int i = 0; i < consts.length; i++) {
			Enum enumObj = consts[i];
			Integer propertyValue = null;
			try {
				Object objValue = ReflectionUtils.getField(enumObj, clazz, property);
				if (objValue instanceof Integer) {
					propertyValue = (Integer) objValue;
				}
			} catch (ClassCastException e) {
				logger.warn(e.getMessage());
				break;
			}
			if (propertyValue != null && value != null && value.intValue() == propertyValue.intValue()) {
				return (T) enumObj;
			}
		}
		return null;
	}
	
	/**
	 * 根据ordinal获取enum实例
	 *
	 * @param clazz
	 * @param ordinal
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static <T extends Enum> T lookup(Class clazz, Long ordinal) {
		if (ordinal == null) {
			return null;
		}
		
		EnumSet enumSet = EnumSet.allOf(clazz);
		if (ordinal < enumSet.size()) {
			Iterator<Enum> iterator = enumSet.iterator();
			for (int i = 0; i < ordinal; i++) {
				iterator.next();
			}
			Enum rval = iterator.next();
			assert (rval.ordinal() == ordinal.intValue());
			return (T) rval;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private static <T extends Enum> T lookup(Class clazz, Long value, String property) {
		if (value == null) {
			return null;
		}
		
		Enum[] consts = (Enum[]) clazz.getEnumConstants();
		for (int i = 0; i < consts.length; i++) {
			Enum enumObj = consts[i];
			Object objValue = ReflectionUtils.getField(enumObj, clazz, property);
			if (objValue instanceof Long) {
				Long propertyValue = (Long) objValue;
				if (value != null && propertyValue != null && value.intValue() == propertyValue.intValue()) {
					return (T) enumObj;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据ordinal获取enum实例
	 *
	 * @param clazz
	 * @param ordinal
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static <T extends Enum> T lookup(Class clazz, BigInteger ordinal) {
		if (ordinal == null) {
			return null;
		}
		
		EnumSet enumSet = EnumSet.allOf(clazz);
		if (ordinal.intValue() < enumSet.size()) {
			Iterator<Enum> iterator = enumSet.iterator();
			for (int i = 0; i < ordinal.intValue(); i++) {
				iterator.next();
			}
			Enum rval = iterator.next();
			assert (rval.ordinal() == ordinal.intValue());
			return (T) rval;
		}
		throw new IllegalArgumentException(
				"Invalid value " + ordinal + " for " + clazz.getName() + ", must be < " + enumSet.size());
	}
	
	@SuppressWarnings("rawtypes")
	private static <T extends Enum> T lookup(Class clazz, BigInteger value, String property) {
		if (value == null) {
			return null;
		}
		
		Enum[] consts = (Enum[]) clazz.getEnumConstants();
		for (int i = 0; i < consts.length; i++) {
			Enum enumObj = consts[i];
			Object objValue = ReflectionUtils.getField(enumObj, clazz, property);
			if (objValue instanceof BigInteger) {
				BigInteger propertyValue = (BigInteger) objValue;
				if (value != null && propertyValue != null && value.intValue() == propertyValue.intValue()) {
					return (T) enumObj;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据name获取enum实例
	 *
	 * @param clazz
	 * @param name
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static <T extends Enum> T lookup(Class clazz, String name) {
		if (isBlank(name)) {
			throw new IllegalArgumentException("Invalid value " + name + " for " + clazz.getName() + ", must be" + EnumSet.allOf(clazz));
		}
		try {
			return (T)Enum.valueOf(clazz, name);
		} catch (IllegalArgumentException e) {
			logger.trace("msg", e);
		}
		try {
			return (T)Enum.valueOf(clazz, name.toUpperCase());
		} catch (IllegalArgumentException e) {
			logger.trace("msg", e);
			return null;
		}
	}
	
	/**
	 * 根据指定的enum类中的属性去获取enum实例
	 *
	 * @param clazz
	 * @param value
	 * @param property
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <T extends Enum> T lookup(Class clazz, String value, String property) {
		if (isBlank(value)) {
			return null;
		}
		Enum[] consts = (Enum[]) clazz.getEnumConstants();
		for (int i = 0; i < consts.length; i++) {
			Enum enumObj = consts[i];
			String propertyValue = null;
			try {
				Object objValue = ReflectionUtils.getField(enumObj, clazz, property);
				if (objValue instanceof String) {
					propertyValue = (String) objValue;
				}
			} catch (ClassCastException e) {
				//logger.warn("属性 {} 不是字符串类型", property);
				break; //不再继续尝试
			}
			if (StringUtils.equalsIgCase(propertyValue, value)) {
				return (T) enumObj;
			}
		}
		return null;
	}
	
}
