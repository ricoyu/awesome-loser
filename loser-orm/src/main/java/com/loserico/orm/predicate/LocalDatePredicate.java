package com.loserico.orm.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;

public class LocalDatePredicate extends AbstractDatePredicate {

	private LocalDate begin;
	private LocalDate end;

	private DateMatchMode matchMode = DateMatchMode.EXACT;

	public LocalDatePredicate(String propertyName, LocalDate localDate) {
		setPropertyName(propertyName);
		this.begin = localDate;
		addCandidateMatchMode(DateMatchMode.EXACT, DateMatchMode.BETWEEN, DateMatchMode.LATER_THAN, DateMatchMode.LATER_THAN_OR_SAME, DateMatchMode.EARLIER_THAN, DateMatchMode.EARLIER_THAN_OR_SAME);
		checkDateMatchMode(matchMode);
	}
	
	public LocalDatePredicate(String propertyName, LocalDate localDate, DateMatchMode matchMode) {
		setPropertyName(propertyName);
		this.begin = localDate;
		this.matchMode = matchMode;
		addCandidateMatchMode(DateMatchMode.EXACT, DateMatchMode.BETWEEN, DateMatchMode.LATER_THAN, DateMatchMode.LATER_THAN_OR_SAME, DateMatchMode.EARLIER_THAN, DateMatchMode.EARLIER_THAN_OR_SAME);
		checkDateMatchMode(matchMode);
	}

	public LocalDatePredicate(String propertyName, LocalDate begin, LocalDate end) {
		setPropertyName(propertyName);
		this.begin = begin;
		this.end = end;
		addCandidateMatchMode(DateMatchMode.EXACT, DateMatchMode.BETWEEN, DateMatchMode.LATER_THAN, DateMatchMode.LATER_THAN_OR_SAME, DateMatchMode.EARLIER_THAN, DateMatchMode.EARLIER_THAN_OR_SAME);
		checkDateMatchMode(matchMode);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
		Predicate predicate = null;
		switch (matchMode) {
		case BETWEEN:
			predicate = criteriaBuilder.between(root.get(getPropertyName()), begin, end);
			break;
		case EXACT:
			if (begin != null) {
				predicate = criteriaBuilder.equal(root.get(getPropertyName()), begin);
			} else {
				predicate = criteriaBuilder.isNull(root.get(getPropertyName()));
			}
			break;
		case EARLIER_THAN:
			predicate = criteriaBuilder.lessThan(root.get(getPropertyName()), begin);
			break;
		case LATER_THAN:
			predicate = criteriaBuilder.greaterThan(root.get(getPropertyName()), begin);
		case EARLIER_THAN_OR_SAME:
			predicate = criteriaBuilder.lessThanOrEqualTo(root.get(getPropertyName()), begin);
			break;
		case LATER_THAN_OR_SAME:
			predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(getPropertyName()), begin);
			break;
		default:
			predicate = criteriaBuilder.equal(root.get(getPropertyName()), begin);
			break;
		}
		return predicate;
	}

}