package com.loserico.orm.predicate;

import java.time.LocalDate;

public class Predicates {

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
