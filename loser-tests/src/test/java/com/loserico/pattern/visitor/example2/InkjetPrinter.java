package com.loserico.pattern.visitor.example2;

public class InkjetPrinter implements Printer {
	public void printCircle(Circle circle) {
		// ... rasterizing logic for inkjet printing of circles here ...
		System.out.println("Inkjet printer prints a cirlce.");
	}

	public void printRectangle(Rectangle rectangle) {
		// ... rasterizing logic for inkjet printing of rectangles here ...
		System.out.println("Inkjet printer prints a rectangle.");
	}
}