package com.loserico.orm.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;

public interface Predicate {

	public jakarta.persistence.criteria.Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root);
}
