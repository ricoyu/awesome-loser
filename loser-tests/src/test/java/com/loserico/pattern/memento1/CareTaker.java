package com.loserico.pattern.memento1;

import java.util.ArrayList;
import java.util.List;

/**
 * 守护者类
 * <p>
 * Copyright: Copyright (c) 2024-04-02 11:49
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CareTaker {
	private List<Memento> mementoList = new ArrayList<Memento>();

	public void add(Memento state) {
		mementoList.add(state);
	}

	public Memento get(int index) {
		return mementoList.get(index);
	}
}