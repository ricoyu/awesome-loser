package com.loserico.pattern.creational.builder;

public class ConcreteBuilder implements Builder {

	private Product product = new Product();

	/**
	 * 产品零件建造方法1
	 */
	@Override
	public void buildPart1() {
		//构建产品的第一个零件
		product.setPart1("编号：9527");
	}

	/**
	 * 产品零件建造方法2
	 */
	@Override
	public void buildPart2() {
		//构建产品的第二个零件
		product.setPart2("名称：XXX");
	}

	/**
	 * 产品返还方法
	 */
	@Override
	public Product retrieveResult() {
		return product;
	}

}