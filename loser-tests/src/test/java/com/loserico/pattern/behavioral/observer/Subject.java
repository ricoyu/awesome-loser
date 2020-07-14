package com.loserico.pattern.behavioral.observer;

/**
 * <p>
 * Copyright: (C), 2020/2/2 10:09
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface Subject {
	
	/**
	 * 注册观察者对象
	 *
	 * @param observer
	 */
	public void registerObserver(Observer observer);
	
	/**
	 * 删除观察者对象
	 *
	 * @param observer
	 */
	public void removeObserver(Observer observer);
	
	/**
	 * 通知观察者
	 */
	public void notifyObservers();
}
