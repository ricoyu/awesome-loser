package com.loserico.orm.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BasicPredicate extends AbstractPredicate {

	private Object propertyValue;
	private BasicMatchMode basicMatchMode = BasicMatchMode.EQ;

	public BasicPredicate(String propertyName, Object propertyValue) {
		setPropertyName(propertyName);
		this.propertyValue = propertyValue;
	}

	public BasicPredicate(String propertyName, Object propertyValue, BasicMatchMode basicMatchMode) {
		setPropertyName(propertyName);
		this.propertyValue = propertyValue;
		this.basicMatchMode = basicMatchMode;
	}

	@SuppressWarnings("rawtypes")
	public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
		Predicate predicate = null;
		switch (basicMatchMode) {
		case EQ:
			if (propertyValue != null) {
				predicate = criteriaBuilder.equal(root.get(getPropertyName()), propertyValue);
			} else {
				predicate = criteriaBuilder.isNull(root.get(getPropertyName()));
			}
			break;
		case NE:
			if (propertyValue != null) {
				predicate = criteriaBuilder.notEqual(root.get(getPropertyName()), propertyValue);
			} else {
				predicate = criteriaBuilder.isNotNull(root.get(getPropertyName()));
			}
			break;
		default:
			break;
		}
		return predicate;
	}

}
