package com.loserico.orm.transformer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 
 * @author Loser
 * @since May 29, 2016
 * @version
 *
 */
public class ValueHandlerFactory {
	
	private ValueHandlerFactory() {
	}

	public static interface ValueHandler<T> {
		public T convert(Object value);

		public String render(T value);
	}

	public static abstract class BaseValueHandler<T> implements ValueHandler<T>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public String render(T value) {
			return value.toString();
		}
	}

	public static class NoOpValueHandler<T> extends BaseValueHandler<T> {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings({ "unchecked" })
		public T convert(Object value) {
			return (T) value;
		}
	}

	public static class IntegerValueHandler extends BaseValueHandler<Integer> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final IntegerValueHandler INSTANCE = new IntegerValueHandler();

		@Override
		public Integer convert(Object value) {
			if (value == null) {
				return null;
			}
			if (BigDecimal.class.isInstance(value)) {
				return ((BigDecimal) value).intValue();
			} else if (Long.class.isInstance(value)) {
				return ((Long) value).intValue();
			} else if (BigInteger.class.isInstance(value)) {
				return ((BigInteger) value).intValue();
			} else if (String.class.isInstance(value)) {
				return Integer.parseInt((String) value);
			}
			throw unknownConversion(value, Integer.class);
		}
	}

	public static class LongValueHandler extends BaseValueHandler<Long> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final LongValueHandler INSTANCE = new LongValueHandler();

		@Override
		public Long convert(Object value) {
			if (value == null) {
				return null;
			}
			if (BigDecimal.class.isInstance(value)) {
				return Long.valueOf(((BigDecimal) value).longValue());
			} else if (BigInteger.class.isInstance(value)) {
				return ((BigInteger) value).longValue();
			} else if (Integer.class.isInstance(value)) {
				return ((Integer) value).longValue();
			} else if (String.class.isInstance(value)) {
				return Long.parseLong((String) value);
			} else if (Short.class.isInstance(value)) {
				return ((Short)value).longValue();
			}
			throw unknownConversion(value, Long.class);
		}

		@Override
		public String render(Long value) {
			return value.toString() + 'L';
		}
	}

	public static class FloatValueHandler extends BaseValueHandler<Float> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final FloatValueHandler INSTANCE = new FloatValueHandler();

		@Override
		public Float convert(Object value) {
			if (value == null) {
				return null;
			}
			if (BigDecimal.class.isInstance(value)) {
				return ((BigDecimal) value).floatValue();
			}
			throw unknownConversion(value, Float.class);
		}

		@Override
		public String render(Float value) {
			return value.toString() + 'F';
		}
	}

	public static class BigDecimalValueHandler extends BaseValueHandler<BigDecimal> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final BigDecimalValueHandler INSTANCE = new BigDecimalValueHandler();

		@Override
		public BigDecimal convert(Object value) {
			if (value == null) {
				return null;
			}
			if (Float.class.isInstance(value)) {
				return new BigDecimal(((Float) value).toString());
			}
			if (Double.class.isInstance(value)) {
				return new BigDecimal(((Double) value).toString());
			}
			if (Integer.class.isInstance(value)) {
				return BigDecimal.valueOf(((Integer) value).doubleValue());
			}
			throw unknownConversion(value, Float.class);
		}

		@Override
		public String render(BigDecimal value) {
			return value.toString();
		}
	}

	public static class DateValueHandler extends BaseValueHandler<Date> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final DateValueHandler INSTANCE = new DateValueHandler();

		@Override
		public Date convert(Object value) {
			if (value == null) {
				return null;
			}
			if (Timestamp.class.isInstance(value)) {
				return new Date(((Timestamp) value).getTime());
			} else if (java.sql.Date.class.isInstance(value)) {
				return new Date(((java.sql.Date) value).getTime());
			}
			throw unknownConversion(value, Date.class);
		}

		@Override
		public String render(Date value) {
			return value.toString() + 'F';
		}
	}

	public static class StringValueHandler extends BaseValueHandler<String> implements Serializable {
		private static final long serialVersionUID = -3809324482933886451L;
		public static final StringValueHandler INSTANCE = new StringValueHandler();

		@Override
		public String convert(Object value) {
			if (value == null) {
				return null;
			}
			if (Character.class.isInstance(value) || Integer.class.isInstance(value)
					|| Long.class.isInstance(value) || Double.class.isInstance(value)
					|| BigDecimal.class.isInstance(value) || BigInteger.class.isInstance(value)
					|| Character.TYPE.isInstance(value) || Integer.TYPE.isInstance(value)
					|| Long.TYPE.isInstance(value) || Double.TYPE.isInstance(value)) {
				return value.toString();
			}
			throw unknownConversion(value, String.class);
		}

		@Override
		public String render(String value) {
			return value.toString() + 'F';
		}
	}

	public static class LocalDateTimeValueHandler extends BaseValueHandler<LocalDateTime> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final LocalDateTimeValueHandler INSTANCE = new LocalDateTimeValueHandler();

		@Override
		public LocalDateTime convert(Object value) {
			if (value == null) {
				return null;
			}
			if (Timestamp.class.isInstance(value)) {
				return ((Timestamp) value).toLocalDateTime();
			}
			return null;
		}

	}
	
	public static class LocalTimeValueHandler extends BaseValueHandler<LocalTime> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final LocalTimeValueHandler INSTANCE = new LocalTimeValueHandler();

		@Override
		public LocalTime convert(Object value) {
			if (value == null) {
				return null;
			}
			if (Time.class.isInstance(value)) {
				return ((Time) value).toLocalTime();
			}
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static class LocalDateValueHandler extends BaseValueHandler<LocalDate> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final LocalDateValueHandler INSTANCE = new LocalDateValueHandler();
		private static final Class DATE_TYPE = Date.class;
		private static final Class TIMESTAMP_TYPE = Timestamp.class;

		@Override
		public LocalDate convert(Object value) {
			if (value == null) {
				return null;
			}
			if (DATE_TYPE.isInstance(value)) {
				return ((Date) value).toLocalDate();
			} else if(TIMESTAMP_TYPE.isInstance(value)) {
				return ((Timestamp)value).toLocalDateTime().toLocalDate();
			}
			return null;
		}

	}

	public static class BooleanValueHandler extends BaseValueHandler<Boolean> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final BooleanValueHandler INSTANCE = new BooleanValueHandler();

		@Override
		public Boolean convert(Object value) {
			if (value == null) {
				return false;
			}
			if (Boolean.class.isInstance(value)) {
				return (Boolean) value;
			}
			if (String.class.isInstance(value)) {
				return Boolean.getBoolean((String) value);
			}
			if(Integer.class.isInstance(value)) {
				Integer integerValue = (Integer)value;
				if(integerValue.intValue() == 0) {
					return false;
				}
				return true;
			}
			if(Long.class.isInstance(value)) {
				Long longValue = (Long)value;
				if(longValue.intValue() == 0) {
					return false;
				}
				return true;
			}
			if(BigInteger.class.isInstance(value)) {
				BigInteger bigInteger = (BigInteger)value;
				if(bigInteger.intValue() == 0 ) {
					return false;
				}
				return true;
			}
			if(Byte.class.isInstance(value)) {
				Byte byteValue = (Byte)value;
				if(byteValue.byteValue() == (byte)1) {
					return true;
				} else {
					return false;
				}
			}
			return (Boolean) value;
		}

	}

	public static class ShortValueHandler extends BaseValueHandler<Short> implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final ShortValueHandler INSTANCE = new ShortValueHandler();

		@Override
		public Short convert(Object value) {
			if (value == null) {
				return 0;
			}
			if (Short.class.isInstance(value)) {
				return (Short) value;
			}
			if (String.class.isInstance(value)) {
				return Short.valueOf((String) value);
			}
			return (Short) value;
		}

	}

	@SuppressWarnings("rawtypes")
	private static IllegalArgumentException unknownConversion(Object value, Class type) {
		return new IllegalArgumentException("Unaware how to convert value [" + value + " : " + typeName(value)
				+ "] to requested type [" + type.getName() + "]");
	}

	private static String typeName(Object value) {
		return value == null ? "???" : value.getClass().getName();
	}

	/**
	 * Convert the given value into the specified target type.
	 *
	 * @param value The value to convert
	 * @param targetType The type to which it should be converted
	 *
	 * @return The converted value.
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T convert(Object value, Class<T> targetType) {
		if (value == null) {
			return null;
		}
		//		if (targetType.equals(value.getClass())) {
		//			return (T) value;
		//		}
		if (targetType.isAssignableFrom(value.getClass())) {
			return (T) value;
		}

		ValueHandler<T> valueHandler = determineAppropriateHandler(targetType);
		if (valueHandler == null) {
			throw unknownConversion(value, targetType);
		}
		return valueHandler.convert(value);
	}

	/**
	 * Determine the appropriate {@link ValueHandlerFactory.ValueHandler} strategy for
	 * converting a value to the given target type
	 *
	 * @param targetType The target type (to which we want to convert values).
	 * @param <T> parameterized type for the target type.
	 * @return The conversion
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> ValueHandler<T> determineAppropriateHandler(Class<T> targetType) {
		if (Integer.class.equals(targetType) || Integer.TYPE.equals(targetType)) {
			return (ValueHandler<T>) IntegerValueHandler.INSTANCE;
		}
		if (Long.class.equals(targetType) || Long.TYPE.equals(targetType)) {
			return (ValueHandler<T>) LongValueHandler.INSTANCE;
		}
		if (Float.class.equals(targetType) || Float.TYPE.equals(targetType)) {
			return (ValueHandler<T>) FloatValueHandler.INSTANCE;
		}
		if (BigDecimal.class.equals(targetType)) {
			return (ValueHandler<T>) BigDecimalValueHandler.INSTANCE;
		}
		if (Date.class.equals(targetType)) {
			return (ValueHandler<T>) DateValueHandler.INSTANCE;
		}
		if (String.class.equals(targetType)) {
			return (ValueHandler<T>) StringValueHandler.INSTANCE;
		}
		if (LocalDateTime.class.equals(targetType)) {
			return (ValueHandler<T>) LocalDateTimeValueHandler.INSTANCE;
		}
		if (LocalDate.class.equals(targetType)) {
			return (ValueHandler<T>) LocalDateValueHandler.INSTANCE;
		}
		if (Boolean.class.equals(targetType) || Boolean.TYPE.equals(targetType)) {
			return (ValueHandler<T>) BooleanValueHandler.INSTANCE;
		}
		if (Short.class.equals(targetType) || Short.TYPE.equals(targetType)) {
			return (ValueHandler<T>) ShortValueHandler.INSTANCE;
		}
		if (LocalTime.class.equals(targetType)) {
			return (ValueHandler<T>) LocalTimeValueHandler.INSTANCE;
		}
		 
		return null;
	}

}