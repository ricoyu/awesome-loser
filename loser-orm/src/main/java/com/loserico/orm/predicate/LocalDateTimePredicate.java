package com.loserico.orm.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;

/**
 *  日期比较, begin, end都传则是between, 只传begin则是大于等于, 只传end则是小于等于 
 * <p>
 * Copyright: Copyright (c) 2024-01-30 14:41
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LocalDateTimePredicate extends AbstractDatePredicate {

	private LocalDateTime begin;
	private LocalDateTime end;

	private DateMatchMode matchMode = DateMatchMode.BETWEEN;

	public LocalDateTimePredicate(String propertyName, LocalDateTime begin, LocalDateTime end) {
		setPropertyName(propertyName);
		this.begin = begin;
		this.end = end; 
		if (begin == null && end == null) {
			throw new IllegalArgumentException("begin and end can't both be null");
		}
		if (begin == null) {
			matchMode = DateMatchMode.EARLIER_THAN_OR_SAME;
		}
		if (end == null) {
			matchMode = DateMatchMode.LATER_THAN_OR_SAME;
		}
		addCandidateMatchMode(matchMode);
		checkDateMatchMode(matchMode);
	}
	
	/**
	 * 上面构造函数决定了matchMode只有下面三种情况
	 * @param criteriaBuilder
	 * @param root
	 * @return Predicate
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
		switch (matchMode) {
		case BETWEEN:
			return criteriaBuilder.between(root.get(getPropertyName()), begin, end);
		case EARLIER_THAN_OR_SAME:
			return criteriaBuilder.lessThanOrEqualTo(root.get(getPropertyName()), end);
		case LATER_THAN_OR_SAME:
			return criteriaBuilder.greaterThanOrEqualTo(root.get(getPropertyName()), begin);
		default:
			return null;
		}
	}

}
