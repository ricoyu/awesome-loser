package com.loserico.pattern.creational.builder;

public interface Builder {
	public void buildPart1();

	public void buildPart2();

	public Product retrieveResult();
}