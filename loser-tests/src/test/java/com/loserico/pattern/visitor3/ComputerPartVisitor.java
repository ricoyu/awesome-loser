package com.loserico.pattern.visitor3;

/**
 * 表示访问者的接口
 * <p>
 * Copyright: Copyright (c) 2019-06-25 11:45
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public interface ComputerPartVisitor {

	/**
	 * 专门访问Keyboard对象, 注意这里违反了依赖倒置原则, 依赖了具体类, 没有依赖抽象。
	 * 
	 * @param keyboard
	 */
	public void visit(Keyboard keyboard);

	public void visit(Mouse mouse);

	public void visit(Monitor monitor);
	
	public void visit(Computer computer);
}
