package com.loserico.pattern.structural.composite3;

/**
 * 平房
 * 叶子节点
 * <p>
 * Copyright: Copyright (c) 2018-10-09 15:36
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class Bungalow implements Property {
	float price;
	String address;
	String builder;

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