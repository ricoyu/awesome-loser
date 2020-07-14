package com.loserico.common.lang.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * 分页支持
 * 
 * @author xuehyu
 * @since 2014-10-13
 * @version 1.0
 * 
 */
public class Page implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 当前第几页
	 */
	private int currentPage = 1;

	/**
	 * 每页多少条记录
	 */
	private int pageSize = 10;

	/**
	 * 总共有多少页，通过查询结果返回给你显示用
	 */
	private int totalPages = 0;

	/**
	 * 总共有多少条记录，通过查询结果返回给你显示用"
	 */
	private int totalCount = 0;
	
	private boolean autoCount = true;

	/**
	 * 还有没有下一页
	 */
	private boolean hasNextPage = false;

	/**
	 * 还有没有前一页
	 */
	private boolean hasPreviousPage = false;

	/**
	 * 对于某些接口，有些调用需要分页，有些不需要分页，如果不需要分页，传true就可以了
	 */
	private boolean pagingIgnore = false;
	
	/**
	 * 第一级排序, 如果只按一个字段排序，那么就用这个
	 */
	private OrderBean order;

	/**
	 * 如果提供了order，那么orders是第二第三...级排序
	 * <p>
	 * 如果需要先按A排序，相同的再按B排序，就可以用这个orders指定多个排序规则
	 */
	private List<OrderBean> orders = new ArrayList<>();

	/**
	 * firstRowIndex begins from 0
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getFirstResult() {
		return (currentPage - 1) * pageSize;
	}

	/**
	 * Record limits to maxResults per time
	 * 
	 * @return
	 */
	public int getMaxResults() {
		return this.pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		updatePagingStatus();
	}

	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}
	
	public void addOrder(OrderBean... orderBeans) {
		requireNonNull(orders, "order不能为null");
		for (int i = 0; i < orderBeans.length; i++) {
			OrderBean orderBean = orderBeans[i];
			requireNonNull(orderBean);
			orders.add(orderBean);
		}
	}
	
	public Page addOrder(String orderBy, OrderBean.ORDER_BY direction) {
		orders.add(new OrderBean(orderBy, direction));
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(currentPage, pageSize, totalPages, totalCount, hasNextPage, hasPreviousPage, order, orders);
	}

	/**
	 * Based on record return from sql query, determine if there is another page
	 * 
	 */
	private void updatePagingStatus() {
		this.totalPages = (int) Math.ceil(this.totalCount / (double) this.pageSize);

		// not record found or there is only one page
		if (this.totalPages == 0 || this.totalPages == 1) {
			this.hasNextPage = false;
			this.hasPreviousPage = false;
		} else {// more than one page
			if (this.currentPage < this.totalPages) {
				this.hasNextPage = true;
			} else {
				this.hasNextPage = false;
			}

			if (this.currentPage == 1) {
				this.hasPreviousPage = false;
			} else {
				this.hasPreviousPage = true;
			}
		}

	}

	public boolean isPagingIgnore() {
		return pagingIgnore;
	}

	public void setPagingIgnore(boolean pagingIgnore) {
		this.pagingIgnore = pagingIgnore;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}
	
	public OrderBean getOrder() {
		return order;
	}
	
	public void setOrder(OrderBean order) {
		this.order = order;
	}
	
	public List<OrderBean> getOrders() {
		return orders;
	}
}