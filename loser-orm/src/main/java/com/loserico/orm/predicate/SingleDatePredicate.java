package com.loserico.orm.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

import static com.loserico.orm.predicate.DateMatchMode.EARLIER_THAN;
import static com.loserico.orm.predicate.DateMatchMode.EARLIER_THAN_OR_SAME;
import static com.loserico.orm.predicate.DateMatchMode.EXACT;
import static com.loserico.orm.predicate.DateMatchMode.LATER_THAN;
import static com.loserico.orm.predicate.DateMatchMode.LATER_THAN_OR_SAME;

public class SingleDatePredicate extends AbstractDatePredicate {

	private Date specifiedDate;

	public SingleDatePredicate(String propertyName, Date specifiedDate) {
		setPropertyName(propertyName);
		this.specifiedDate = specifiedDate;
		addCandidateMatchMode(EARLIER_THAN, EARLIER_THAN_OR_SAME, EXACT, LATER_THAN, LATER_THAN_OR_SAME);
	}

	public SingleDatePredicate(String propertyName, Date specifiedDate, DateMatchMode matchMode) {
		this(propertyName, specifiedDate);
		checkDateMatchMode(matchMode);
		setMatchMode(matchMode);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
		Path path = root.get(getPropertyName());
		DateMatchMode matchMode = getMatchMode();
		Predicate predicate = null;

		switch (matchMode) {
		case EARLIER_THAN:
			predicate = criteriaBuilder.lessThan(path, specifiedDate);
			break;
		case EARLIER_THAN_OR_SAME:
			predicate = criteriaBuilder.lessThanOrEqualTo(path, specifiedDate);
			break;
		case EXACT:
			if (specifiedDate != null) {
				predicate = criteriaBuilder.equal(path, specifiedDate);
			} else {
				predicate = criteriaBuilder.isNull(path);
			}
			break;
		case LATER_THAN:
			predicate = criteriaBuilder.greaterThan(path, specifiedDate);
			break;
		case LATER_THAN_OR_SAME:
			predicate = criteriaBuilder.greaterThanOrEqualTo(path, specifiedDate);
		default:
			break;
		}
		return predicate;
	}

	public Date getSpecifiedDate() {
		return specifiedDate;
	}

	public void setSpecifiedDate(Date specifiedDate) {
		this.specifiedDate = specifiedDate;
	}

}
