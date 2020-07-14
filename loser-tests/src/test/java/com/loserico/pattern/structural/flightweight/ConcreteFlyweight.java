package com.loserico.pattern.structural.flightweight;

/**
 * 具体蝇量角色类ConcreteFlyweight有一个内部状态，它的值应当在蝇量对象被创建时赋予。所有的内部状态在对象创建之后，就不会再改变了。 
 * 如果一个蝇量对象有外部状态的话，所有的外部状态都必须存储在客户端，在使用蝇量对象时，再由客户端传入蝇量对象。这里只有一个外部状态，
 * operation()方法的参数externalState就是由外部传入的外部状态。
 * <p>
 * Copyright: (C), 2020/2/7 12:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ConcreteFlyweight implements Flyweight {
	
	private final String internalState;
	
	public ConcreteFlyweight(String internalState) {
		this.internalState = internalState;
	}
	
	@Override
	public void operation(String externalState) {
		System.out.println("内部状态: " + this.internalState);
		System.out.println("外部状态: " + externalState);
	}
}
