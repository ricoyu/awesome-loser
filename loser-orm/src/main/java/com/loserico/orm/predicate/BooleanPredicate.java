package com.loserico.orm.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BooleanPredicate extends AbstractPredicate {

	private Boolean propertyValue;
	private CompareMode compareMode = CompareMode.EQ;

	public BooleanPredicate(String propertyName, Boolean propertyValue) {
		setPropertyName(propertyName);
		this.propertyValue = propertyValue;
	}

	public BooleanPredicate(String propertyName, Boolean propertyValue, CompareMode compareMode) {
		this(propertyName, propertyValue);
		if(compareMode != CompareMode.EQ && compareMode != CompareMode.NOTEQ) {
			throw new IllegalArgumentException("BooleanPredicate only accept compareMode EQ or NOTEQ!");
		}
		this.setCompareMode(compareMode);
	}

	public CompareMode getCompareMode() {
		return compareMode;
	}

	public void setCompareMode(CompareMode compareMode) {
		this.compareMode = compareMode;
	}

	@SuppressWarnings({ "rawtypes"})
	public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
		Predicate predicate = null;
		Path path = root.get(getPropertyName());
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
		}
		return predicate;
	}

}