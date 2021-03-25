package com.loserico.mybatis.core;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-03-15 16:19
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class Pages<T> {
	
	/**
	 * 构建Mybatis-Plus Page对象入口
	 * @param current
	 * @param size
	 * @return Pages
	 */
	public static final <T> PageBuilder<T> builder(long current, long size) {
		return new PageBuilder(current, size);
	}
	
	public static class PageBuilder<T> {
		/**
		 * 每页显示条数，默认 10
		 */
		private long size = 10;
		
		/**
		 * 当前页
		 */
		private long current = 1;
		
		/**
		 * 排序字段信息
		 */
		private List<OrderItem> orders = new ArrayList<>();
		
		/**
		 * 自动优化 COUNT SQL
		 */
		private boolean optimizeCountSql = true;
		
		/**
		 * 是否进行 count 查询
		 */
		private boolean isSearchCount = true;
		
		private Long maxLimit;
		
		private PageBuilder(long current, long size) {
			this.current = current;
			this.size = size;
		}
		
		/**
		 * 自动优化 COUNT SQL
		 * @param optimizeCountSql
		 * @return Pages
		 */
		public PageBuilder optimizeCountSql(boolean optimizeCountSql) {
			this.optimizeCountSql = optimizeCountSql;
			return this;
		}
		
		/**
		 * 是否进行 count 查询
		 * @param isSearchCount
		 * @return Pages
		 */
		public PageBuilder isSearchCount(boolean isSearchCount) {
			this.isSearchCount = isSearchCount;
			return this;
		}
		
		public PageBuilder maxLimit(Long maxLimit) {
			this.maxLimit = maxLimit;
			return this;
		}
		
		/**
		 * 添加排序规则, 升序字段
		 * @param columns
		 * @return Pages
		 */
		public PageBuilder asc(String... columns) {
			orders.addAll(OrderItem.ascs(columns));
			return this;
		}
		
		/**
		 * 添加排序规则, 降序字段
		 * @param columns
		 * @return Pages
		 */
		public PageBuilder desc(String... columns) {
			orders.addAll(OrderItem.descs(columns));
			return this;
		}
		
		public Page build() {
			Page<T> page = new Page<>(current, size);
			page.addOrder(orders);
			page.setSearchCount(isSearchCount);
			page.setMaxLimit(maxLimit);
			page.setOptimizeCountSql(optimizeCountSql);
			return page;
		}
	}
	
}
