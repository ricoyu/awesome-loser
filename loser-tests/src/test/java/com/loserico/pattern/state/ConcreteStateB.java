package com.loserico.pattern.state;

/** 
 * 具体的状态子类B 
 * @author  lvzb.software@qq.com 
 * 
 */
public class ConcreteStateB implements State {

	@Override
	public void behavior() {
		// 状态B 的业务行为, 及当为该状态下时，能干什么  
		// 如：手机在欠费停机状态下, 不 能拨打电话  
		System.out.println("手机在欠费停机状态下, 不能拨打电话");
	}

}