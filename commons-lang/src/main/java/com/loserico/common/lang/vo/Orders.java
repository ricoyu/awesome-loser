package com.loserico.common.lang.vo;

public final class Orders {

	private Orders() {

	}

	public static OrderBean asc(String propertyName) {
		return new OrderBean(propertyName, OrderBean.ORDER_BY.ASC);
	}

	public static OrderBean desc(String propertyName) {
		return new OrderBean(propertyName, OrderBean.ORDER_BY.DESC);
	}

}
