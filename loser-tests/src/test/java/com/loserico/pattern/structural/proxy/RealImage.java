package com.loserico.pattern.structural.proxy;

/**
 * <p>
 * Copyright: (C), 2020/2/13 10:41
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RealImage implements Image {
	
	private String fileName;
	
	public RealImage(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public void display() {
		System.out.println("Displaying " + fileName);
	}
	
	private void loadFromDisk(String fileName) {
		System.out.println("Loading " + fileName);
	}
}
