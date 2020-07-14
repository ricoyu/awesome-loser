package com.loserico.pattern.state;

/** 
 * 具体的状态子类A 
 * @author  lvzb.software@qq.com 
 */
public class ConcreteStateA implements State {

	@Override
	public void behavior() {
		// 状态A 的业务行为, 及当为该状态下时，能干什么   
		// 如：手机在未欠费停机状态下, 能正常拨打电话  
		System.out.println("手机在未欠费状态下, 能正常拨打电话");
	}

}