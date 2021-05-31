package com.loserico.search.enums;

import org.elasticsearch.search.sort.SortOrder;

/**
 * 排序
 */
public enum Direction {
	
	ASC,
	
	DESC;
	
	/**
	 * Direction 转Elasticsearch API SortOrder
	 * @return
	 */
	public SortOrder toSortOrder() {
		if (this == ASC) {
			return SortOrder.ASC;
		}
		
		return SortOrder.DESC;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
