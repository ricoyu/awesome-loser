package com.loserico.orm.predicate;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Predicates {
	
	/**
	 * 等于查询
	 * @param propertyName
	 * @param propertyValue
	 * @return Predicate
	 */
	public static Predicate eq(String propertyName, Object propertyValue) {
		if (propertyValue == null) {
			return stringPredicate(propertyName, null);
		}
		
		if (propertyValue instanceof String) {
			return stringPredicate(propertyName, (String) propertyValue);
		}
		
		if (propertyValue instanceof Integer) {
			return integerPredicate(propertyName, (Integer) propertyValue);
		}
		
		if (propertyValue instanceof Long) {
			return longPredicate(propertyName, (Long) propertyValue);
		}
		
		if (propertyValue instanceof Boolean) {
			return booleanPredicate(propertyName, (Boolean) propertyValue);
		}
		
		if (propertyValue instanceof LocalDate) {
			return localDatePredicate(propertyName, (LocalDate) propertyValue);
		}
		
		return basicPredicate(propertyName, propertyValue);
	}
	
	/**
	 * 不等于查询
	 * @param propertyName
	 * @param propertyValue
	 * @return Predicate
	 */
	public static Predicate ne(String propertyName, Object propertyValue) {
		if (propertyValue == null) {
			return new StringPredicate(propertyName, null, CompareMode.NOTEQ);
		}
		
		if (propertyValue instanceof String) {
			return stringPredicate(propertyName, (String) propertyValue, CompareMode.NOTEQ);
		}
		
		if (propertyValue instanceof Integer) {
			return integerPredicate(propertyName, (Integer) propertyValue, CompareMode.NOTEQ);
		}
		
		if (propertyValue instanceof Long) {
			return longPredicate(propertyName, (Long) propertyValue, CompareMode.NOTEQ);
		}
		
		if (propertyValue instanceof Boolean) {
			return booleanPredicate(propertyName, (Boolean) propertyValue, CompareMode.NOTEQ);
		}
		
		if (propertyValue instanceof LocalDate) {
			throw new IllegalArgumentException("LocalDate类型不支持不等于查询");
		}
		
		return new BasicPredicate(propertyName, propertyValue, BasicMatchMode.NE);
	}
	
	public static Predicate between(String propertyName, LocalDateTime begin, LocalDateTime end) {
		return new LocalDateTimePredicate(propertyName, begin, end);
	}

	public static Predicate stringPredicate(String propertyName, String propertyValue) {
		return new StringPredicate(propertyName, propertyValue);
	}

	public static Predicate stringPredicate(String propertyName, String propertyValue, CompareMode compareMode) {
		return new StringPredicate(propertyName, propertyValue, compareMode);
	}

	public static Predicate integerPredicate(String propertyName, Integer propertyValue) {
		return new IntegerPredicate(propertyName, propertyValue);
	}

	public static Predicate integerPredicate(String propertyName, Integer propertyValue, CompareMode compareMode) {
		return new IntegerPredicate(propertyName, propertyValue, compareMode);
	}

	public static Predicate longPredicate(String propertyName, long propertyValue) {
		return new LongPredicate(propertyName, propertyValue);
	}

	public static Predicate longPredicate(String propertyName, long propertyValue, CompareMode compareMode) {
		return new LongPredicate(propertyName, propertyValue, compareMode);
	}

	public static Predicate booleanPredicate(String propertyName, boolean propertyValue) {
		return new BooleanPredicate(propertyName, propertyValue);
	}

	public static Predicate booleanPredicate(String propertyName, boolean propertyValue, CompareMode compareMode) {
		return new BooleanPredicate(propertyName, propertyValue, compareMode);
	}

	public static Predicate localDatePredicate(String propertyName, LocalDate propertyValue) {
		return new LocalDatePredicate(propertyName, propertyValue);
	}
	
	public static Predicate localDatePredicate(String propertyName, LocalDate propertyValue, DateMatchMode dateMatchMode) {
		return new LocalDatePredicate(propertyName, propertyValue, dateMatchMode);
	}

	public static Predicate inPredicate(String propertyName, Object propertyValue) {
		return new InPredicate(propertyName, propertyValue);
	}
	
	public static Predicate notInPredicate(String propertyName, Object propertyValue) {
		return new InPredicate(propertyName, propertyValue, CompareMode.NOTIN);
	}
	
	public static Predicate basicPredicate(String propertyName, Object propertyValue) {
		return new BasicPredicate(propertyName, propertyValue);
	}
}
