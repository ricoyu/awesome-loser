package com.loserico.pattern.creational.prototype.register;

public class ConcretePrototype2 implements Prototype {
	private String name;

	public Prototype clone() {
		ConcretePrototype2 prototype = new ConcretePrototype2();
		prototype.setName(this.name);
		return prototype;
	}

	public String toString() {
		return "Now in Prototype2 , name = " + this.name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}