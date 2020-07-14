package com.loserico.orm.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class LocalDateTimePredicate extends AbstractDatePredicate {

	private LocalDateTime begin;
	private LocalDateTime end;

	private DateMatchMode matchMode = DateMatchMode.BETWEEN;

	public LocalDateTimePredicate(String propertyName, LocalDateTime begin, LocalDateTime end) {
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