package com.loserico.pattern.flyweight;

/** 
 *  
 *作者：alaric 
 *时间：2013-7-27下午3:34:57 
 *描述：数据类 
 */
public class Context {

	private int size;
	private char c;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public char getC() {
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}

	public Context(int size, char c) {
		super();
		this.size = size;
		this.c = c;
	}

}