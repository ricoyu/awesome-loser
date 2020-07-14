package com.loserico.pattern.bridge.example1;

/**
 * 扩充抽象类或细化抽象类（RefinedAbstraction）
 * 
 * @author Loser
 * @since Jul 6, 2016
 * @version
 *
 */
public class RefinedAbstraction extends Abstraction {

	public void operation() {
		//业务代码  
		impl.operationImpl(); //调用实现类的方法  
		//业务代码  
	}
}