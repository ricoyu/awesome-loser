package com.loserico.orm.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalTime;

public class LocalTimePredicate extends AbstractDatePredicate {

	private LocalTime begin;
	private LocalTime end;

	private DateMatchMode matchMode = DateMatchMode.BETWEEN;

	public LocalTimePredicate(String propertyName, LocalTime begin, LocalTime end) {
		setPropertyName(propertyName);
		this.begin = begin;
		this.end = end;
		addCandidateMatchMode(DateMatchMode.BETWEEN);
		checkDateMatchMode(matchMode);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
		return criteriaBuilder.between(root.get(getPropertyName()), begin, end);
	}

}