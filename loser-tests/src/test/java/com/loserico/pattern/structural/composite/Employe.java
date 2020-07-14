package com.loserico.pattern.structural.composite;

/**
 * 普通员工类
 * <p>
 * Copyright: Copyright (c) 2018-10-09 15:14
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 *
 */
public class Employe implements Worker {

	private String name;

	public Employe(String name) {
		this.name = name;
	}

	@Override
	public void doSomething() {
		System.out.println(toString());
	}

	@Override
	public String toString() {
		return "我叫" + getName() + ", 就一普通员工!";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}