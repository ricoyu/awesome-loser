package com.loserico.mybatis.core;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loserico.common.lang.utils.BeanUtils;
import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.common.lang.vo.OrderBean.ORDER_BY;
import com.loserico.mybatis.page.PageProxy;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
@Slf4j
public final class Pages<T> {
	
	/**
	 * 构建Mybatis-Plus Page对象入口
	 *
	 * @param current
	 * @param size
	 * @return Pages
	 */
	public static <T> PageBuilder<T> builder(long current, long size) {
		return new PageBuilder(current, size);
	}
	
	/**
	 * 将Page对象转成Mybatis-Plus的Page对象
	 *
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> Page<T> convert(Object obj) {
		com.loserico.common.lang.vo.Page page = null;
		if (obj instanceof com.loserico.common.lang.vo.Page) {
			page = (com.loserico.common.lang.vo.Page) obj;
		} else {
			page = (com.loserico.common.lang.vo.Page) ReflectionUtils.getFieldValue("page", obj);
		}
		
		if (page == null) {
			log.warn("没有找到page对象!");
			return null;
		}
		
		PageBuilder builder = new PageBuilder(page.getCurrentPage(), page.getPageSize());
		List<OrderItem> orderItems = page.getOrders().stream()
				.map((order) -> new OrderItem(order.getOrderBy(), order.getDirection() == ORDER_BY.ASC ? true : false))
				.collect(toList());
		builder.orders = orderItems;
		
		Page targetPage = builder.build();
		PageProxy pageProxy = new PageProxy(page);
		
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Page.class);
		enhancer.setCallback(pageProxy);
		Page pageResult = (Page) enhancer.create();
		BeanUtils.copyProperties(targetPage, pageResult);
		return pageResult;
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
			if (current < 0) {
				this.current = 0;
			} else {
				this.current = current;
			}
			if (size < 0) {
				this.size = 10;
			} else {
				this.size = size;
			}
		}
		
		/**
		 * 自动优化 COUNT SQL
		 *
		 * @param optimizeCountSql
		 * @return Pages
		 */
		public PageBuilder optimizeCountSql(boolean optimizeCountSql) {
			this.optimizeCountSql = optimizeCountSql;
			return this;
		}
		
		/**
		 * 是否进行 count 查询
		 *
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
		 *
		 * @param columns
		 * @return Pages
		 */
		public PageBuilder asc(String... columns) {
			orders.addAll(OrderItem.ascs(columns));
			return this;
		}
		
		/**
		 * 添加排序规则, 降序字段
		 *
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
