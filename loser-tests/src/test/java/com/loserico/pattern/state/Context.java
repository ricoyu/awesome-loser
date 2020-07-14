package com.loserico.pattern.state;

/** 
 * 环境/上下文类<br/> 
 * 拥有状态对象，且可以完成状态间的转换 [状态的改变/切换 在环境类中实现] 
 * @author  lvzb.software@qq.com 
 * 
 */
public class Context {
	// 维护一个抽象状态对象的引用  
	private State state;

	/**
	 * 模拟手机的话费属性<br/> 
	 * 环境状态如下： 
	 * 1>、当  bill >= 0.00$ : 状态正常   还能拨打电话  
	 * 2>、当  bill < 0.00$ : 手机欠费   不能拨打电话 
	 */
	private double bill;

	/** 
	 * 环境处理函数，调用状态实例行为完成业务逻辑<br/> 
	 * 根据不同的状态实例引用  在不同状态下处理不同的行为 
	 */
	public void Handle() {
		checkState();
		state.behavior();
	}

	/** 
	 * 检查环境状态:状态的改变/切换 在环境类中实现 
	 */
	private void checkState() {
		if (bill >= 0.00) {
			setState(new ConcreteStateA());
		} else {
			setState(new ConcreteStateB());
		}
	}

	/** 
	 * 设置环境状态<br/> 
	 * 私有方法，目的是让环境的状态由系统环境自身来控制/切换,外部使用者无需关心环境内部的状态 
	 * @param state 
	 */
	private void setState(State state) {
		this.state = state;
	}

	public double getBill() {
		return bill;
	}

	public void setBill(double bill) {
		this.bill = bill;
	}
}