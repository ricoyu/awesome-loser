package com.loserico.pattern.structural.composite;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 领导类
 * <p>
 * Copyright: Copyright (c) 2018-10-09 15:06
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 *
 */
public class Leader implements Worker {
	private List<Worker> workers = new CopyOnWriteArrayList<Worker>();
	private String name;

	public Leader(String name) {
		this.name = name;
	}

	public void add(Worker worker) {
		workers.add(worker);
	}

	public void remove(Worker worker) {
		workers.remove(worker);
	}

	public Worker getChild(int i) {
		return workers.get(i);
	}

	@Override
	public void doSomething() {
		System.out.println(toString());
		Iterator<Worker> it = workers.iterator();
		while (it.hasNext()) {
			it.next().doSomething();
		}

	}

	@Override
	public String toString() {
		return "我叫" + getName() + ", 我是一个领导,有 " + workers.size() + "下属。";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}