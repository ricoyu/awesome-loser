package com.loserico.pattern.visitor3;

/**
 * 表示元素的接口
 * <p>
 * Copyright: Copyright (c) 2019-06-25 11:46
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public interface ComputerPart {

	public void accept(ComputerPartVisitor computerPartVisitor);
}
