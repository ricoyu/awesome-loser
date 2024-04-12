package com.loserico.pattern.behavioral.memento1;

/**
 * 备忘录类
 * <p>
 * Copyright: Copyright (c) 2024-04-02 11:46
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class Memento {
	private String state;

	public Memento(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}
}