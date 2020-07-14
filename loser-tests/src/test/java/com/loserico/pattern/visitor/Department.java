package com.loserico.pattern.visitor;

/** 
 * 公司部门（访问者）抽象类 
 * 
 */
public abstract class Department {

	// 声明一组重载的访问方法，用于访问不同类型的具体元素（这里指的是不同的员工）    

	/** 
	 * 抽象方法访问公司管理者对象<br/> 
	 * 具体访问对象的什么  就由具体的访问者子类（这里指的是不同的具体部门）去实现 
	 * @param me 
	 */
	public abstract void visit(ManagerEmployee me);

	/** 
	 * 抽象方法 访问公司普通员工对象<br/> 
	 * 具体访问对象的什么  就由具体的访问者子类（这里指的是不同的具体部门）去实现 
	 * @param ge 
	 */
	public abstract void visit(GeneralEmployee ge);

}