package com.loserico.pattern.visitor.example2;

public class PostscriptPrinter implements Printer {
	public void printCircle(Circle circle) {
		System.out.println("PostScript printer prints a cirlce.");
	}

	public void printRectangle(Rectangle rectangle) {
		System.out.println("PostScript printer prints a rectangle.");
	}
}