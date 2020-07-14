package com.loserico.pattern.bridge;

public class PencilDraw implements Drawing {

	@Override
	public void drawLine() {
		//This line is drawn by pencil  
		System.out.println("pencil is drawing a line...");
		//do drawing  
	}

	@Override
	public void drawCircle() {
		//This circle is drawn by pencil  
		System.out.println("pencil is drawing a circle...");
		//do drawing  
	}

}