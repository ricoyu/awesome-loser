package com.loserico.pattern.bridge.example1;

/**
 * 在抽象类 Abstraction 中定义了一个实现类接口类型的成员对象 impl，再通过注入的方式给该对象赋值，一般将该对象的可见性定义为
 * protected，以便在其子类中访问 Implementor 的方法，其子类一般称为扩充抽象类或细化抽象类（RefinedAbstraction）
 * 
 * @author Loser
 * @since Jul 6, 2016
 * @version
 *
 */
public abstract class Abstraction {
	protected Implementor impl; //定义实现类接口对象  

	public void setImpl(Implementor impl) {
		this.impl = impl;
	}

	public abstract void operation(); //声明抽象业务方法  
}