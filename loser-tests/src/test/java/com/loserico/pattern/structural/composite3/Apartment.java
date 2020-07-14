package com.loserico.pattern.structural.composite3;

/**
 * 公寓
 * 叶子节点
 * <p>
 * Copyright: Copyright (c) 2018-10-09 15:39
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class Apartment implements Property {
	private float price;
	private String address;
	private String builder;

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBuilder() {
		return builder;
	}

	public void setBuilder(String builder) {
		this.builder = builder;
	}

	@Override
	public void purchase() {

	}

	@Override
	public void sell() {

	}

}
