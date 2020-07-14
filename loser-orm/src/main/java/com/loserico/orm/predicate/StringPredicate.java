package com.loserico.orm.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class StringPredicate extends AbstractPredicate {

	private String propertyValue;
	private CompareMode compareMode = CompareMode.EQ;

	public StringPredicate(String propertyName, String propertyValue) {
		setPropertyName(propertyName);
		this.propertyValue = propertyValue;
	}

	public StringPredicate(String propertyName, String propertyValue, CompareMode compareMode) {
		this(propertyName, propertyValue);
		this.setCompareMode(compareMode);
	}

	public CompareMode getCompareMode() {
		return compareMode;
	}

	public void setCompareMode(CompareMode compareMode) {
		this.compareMode = compareMode;
	}

	public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
		Predicate predicate = null;
		Path<String> path = root.get(getPropertyName());
		switch (compareMode) {
		case EQ:
			if (propertyValue != null) {
				predicate = criteriaBuilder.equal(path, propertyValue);
			} else {
				predicate = criteriaBuilder.isNull(path);
			}
			break;
		case NOTEQ:
			if (propertyValue != null) {
				predicate = criteriaBuilder.notEqual(path, propertyValue);
			} else {
				predicate = criteriaBuilder.isNotNull(path);
			}
			break;
		case ANYWHERE:
			predicate = criteriaBuilder.like(path, String.join("%", propertyValue, "%"));
		}
		return predicate;
	}
}
