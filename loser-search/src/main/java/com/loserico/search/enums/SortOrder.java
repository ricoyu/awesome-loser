package com.loserico.search.enums;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.sort.SortBuilders;

/**
 * <p>
 * Copyright: (C), 2021-01-11 20:17
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class SortOrder {
	
	private SortType sortType;
	
	private String field;
	
	/**
	 * 默认升序
	 */
	private Direction direction = Direction.ASC;
	
	
	private SortOrder() {
	}
	
	private SortOrder(SortType sortType) {
		this.sortType = sortType;
	}
	
	/**
	 * 为 SearchRequestBuilder 添加排序规则
	 *
	 * @param builder
	 */
	public void addTo(SearchRequestBuilder builder) {
		if (sortType == SortType.SCORE) {
			builder.addSort(SortBuilders.scoreSort().order(direction.toSortOrder()));
		} else if (sortType == SortType.FIELD) {
			builder.addSort(SortBuilders.fieldSort(field).order(direction.toSortOrder()));
		}
	}
	
	public BucketOrder toBucketOrder() {
		if ("key".equalsIgnoreCase(field)) {
			return BucketOrder.key(direction == Direction.ASC);
		}
		
		return BucketOrder.count(direction == Direction.ASC);
	}
	
	/**
	 * 根据权重排序
	 */
	public static SortOrderBuilder scoreSort() {
		return new SortOrderBuilder(SortType.SCORE);
	}
	
	/**
	 * 根据字段值排序
	 */
	public static SortOrderBuilder fieldSort(String field) {
		return new SortOrderBuilder(field);
	}
	
	private enum SortType {
		
		/**
		 * 根据权重排序
		 */
		SCORE,
		
		/**
		 * 根据字段值排序
		 */
		FIELD;
	}
	
	
	public static class SortOrderBuilder {
		
		private SortType sortType;
		
		private String field;
		
		private SortOrderBuilder(SortType sortType) {
			this.sortType = sortType;
		}
		
		private SortOrderBuilder(String field) {
			this.sortType = SortType.FIELD;
			this.field = field;
		}
		
		/**
		 * 升序排列
		 *
		 * @return
		 */
		public SortOrder asc() {
			SortOrder sortOrder = new SortOrder(sortType);
			sortOrder.direction = Direction.ASC;
			sortOrder.field = field;
			return sortOrder;
		}
		
		/**
		 * 降序排列
		 *
		 * @return
		 */
		public SortOrder desc() {
			SortOrder sortOrder = new SortOrder(sortType);
			sortOrder.direction = Direction.ASC;
			sortOrder.field = field;
			return sortOrder;
		}
		
		/**
		 * 指定字符串排序规则
		 * @param direction
		 * @return SortOrder
		 */
		public SortOrder direction(String direction) {
			SortOrder sortOrder = new SortOrder(sortType);
			sortOrder.field = field;
			if (Direction.ASC.name().equalsIgnoreCase(direction)) {
				sortOrder.direction = Direction.ASC;
			} else if (Direction.DESC.name().equalsIgnoreCase(direction)) {
				sortOrder.direction = Direction.DESC;
			} else {
				log.error("Sort direction can only be asc, desc, your's {}", direction);
				throw new IllegalArgumentException("Sort direction can only be asc, desc, your's " + direction);
			}
			
			return sortOrder;
		}
	}
}
