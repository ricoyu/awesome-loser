package com.loserico.orm.predicate;

import com.loserico.common.lang.utils.ArrayTypes;
import com.loserico.common.lang.utils.Types;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;

public class InPredicate extends AbstractPredicate {

	private Object propertyValue;
	private CompareMode compareMode = CompareMode.IN;

	public InPredicate(String propertyName, Object propertyValue) {
		setPropertyName(propertyName);
		this.propertyValue = propertyValue;
	}

	public InPredicate(String propertyName, Object propertyValue, CompareMode compareMode) {
		setPropertyName(propertyName);
		this.propertyValue = propertyValue;
		this.compareMode = compareMode;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
		if (propertyValue == null) {
			return null;
		}

		Path path = root.get(getPropertyName());
		In inPredicate = null;
		
		if (compareMode == CompareMode.IN) {
			inPredicate = criteriaBuilder.in(path);
		} else {
			inPredicate = (In)criteriaBuilder.in(path).not();
		}
		if (propertyValue.getClass().isAssignableFrom(Collection.class)) {
			Collection values = (Collection) propertyValue;
			for (Object value : values) {
				inPredicate.value(value);
			}
		} else {
			ArrayTypes arrayTypes = Types.arrayTypes(propertyValue);
			if (arrayTypes != null) {
				switch (arrayTypes) {
				case LONG_WRAPPER:
					Long[] arr1 = (Long[]) propertyValue;
					for (int i = 0; i < arr1.length; i++) {
						Long value = arr1[i];
						inPredicate.value(value);
					}
					break;
				case LONG:
					long[] arr2 = (long[]) propertyValue;
					for (int i = 0; i < arr2.length; i++) {
						long value = arr2[i];
						inPredicate.value(value);
					}
					break;
				case INTEGER:
					int[] arr3 = (int[]) propertyValue;
					for (int i = 0; i < arr3.length; i++) {
						int value = arr3[i];
						inPredicate.value(value);
					}
					break;
				case INTEGER_WRAPPER:
					Integer[] arr4 = (Integer[]) propertyValue;
					for (int i = 0; i < arr4.length; i++) {
						Integer value = arr4[i];
						inPredicate.value(value);
					}
					break;
				case STRING:
					String[] arr5 = (String[]) propertyValue;
					for (int i = 0; i < arr5.length; i++) {
						String value = arr5[i];
						inPredicate.value(value);
					}
					break;
				case DOUBLE:
					double[] arr6 = (double[]) propertyValue;
					for (int i = 0; i < arr6.length; i++) {
						double value = arr6[i];
						inPredicate.value(value);
					}
					break;
				case DOUBLE_WRAPPER:
					Double[] arr7 = (Double[]) propertyValue;
					for (int i = 0; i < arr7.length; i++) {
						Double value = arr7[i];
						inPredicate.value(value);
					}
					break;
				case FLOAT:
					float[] arr8 = (float[]) propertyValue;
					for (int i = 0; i < arr8.length; i++) {
						float value = arr8[i];
						inPredicate.value(value);
					}
					break;
				case FLOAT_WRAPPER:
					Float[] arr9 = (Float[]) propertyValue;
					for (int i = 0; i < arr9.length; i++) {
						float value = arr9[i];
						inPredicate.value(value);
					}
					break;

				default:
					break;
				}
			}
		}

		return inPredicate;
	}

}
