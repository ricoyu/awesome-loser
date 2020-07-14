package com.loserico.pattern.visitor3;

/**
 * 使用 ComputerPartDisplayVisitor 来显示 Computer 的组成部分
 * <p>
 * Copyright: Copyright (c) 2019-06-25 11:57
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class VisitorPatternDemo {

	public static void main(String[] args) {
		ComputerPart computerPart = new Computer();
		computerPart.accept(new ComputerPartDisplayVisitor());
	}
}
