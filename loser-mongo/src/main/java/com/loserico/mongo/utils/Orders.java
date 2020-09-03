package com.loserico.mongo.utils;

import com.loserico.common.lang.vo.OrderBean;
import com.loserico.common.lang.vo.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

import static com.loserico.common.lang.utils.Assert.notNull;
import static java.util.stream.Collectors.toList;

/**
 * <p>
 * Copyright: (C), 2020-08-20 11:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class Orders {
	
	/**
	 * OrderBean转Sort
	 *
	 * @param orderBean
	 * @return Sort
	 */
	public static Sort toSort(OrderBean orderBean) {
		notNull(orderBean, "orderBean 不能为null");
		
		Direction direction = orderBean.getDirection() == OrderBean.ORDER_BY.ASC ? Direction.ASC : Direction.DESC;
		return new Sort(direction, orderBean.getOrderBy());
	}
	
	/**
	 * OrderBean[] 转Sort
	 * @param orderBeans
	 * @return
	 */
	public static Sort toSort(OrderBean... orderBeans) {
		notNull(orderBeans, "orders 不能为null");
		Order[] orders = new Order[orderBeans.length];
		for (int i = 0; i < orderBeans.length; i++) {
			OrderBean order = orderBeans[i];
			orders[i] = new Order(direction(order), order.getOrderBy());
		}
		
		return Sort.by(orders);
	}
	
	/**
	 * OrderBean转Sort
	 *
	 * @param orderBeans
	 * @return Sort
	 */
	public static Sort toSort(List<OrderBean> orderBeans) {
		notNull(orderBeans, "orderBean 不能为null");
		
		List<Order> orders = orderBeans.stream().map((orderBean) -> {
			return new Order(direction(orderBean), orderBean.getOrderBy());
		}).collect(toList());
		return Sort.by(orders);
	}
	
	/**
	 * 先从page对象中取order, 去过不为null, 表示按单个属性排序<br/>
	 * 如果为null, 取orders, 如果不为null并且包含元素, 表示按多属性排序<br/>
	 * 如果两者都为null, 表示不需要排序, 返回null
	 *
	 * @param page
	 * @return Sort
	 */
	public static Sort toSort(Page page) {
		notNull(page, "page 不能为null");
		if (page.getOrder() != null) {
			return toSort(page.getOrder());
		}
		
		if (page.getOrders() != null && page.getOrders().size() > 0) {
			return toSort(page.getOrders());
		}
		
		return null;
	}
	
	/**
	 * 获取升降序规则
	 *
	 * @param orderBean
	 * @return Direction
	 */
	private static Direction direction(OrderBean orderBean) {
		return orderBean.getDirection() == OrderBean.ORDER_BY.ASC ? Direction.ASC : Direction.DESC;
	}
}
