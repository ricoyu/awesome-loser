package com.loserico.pattern.visitor.example2;

public class VisitorPatternExample {

	public static void main(String[] args) {
		Shape[] figures = new Shape[] { new Circle(), new Rectangle() };
		Printer[] printers = new Printer[] { new PostscriptPrinter(), new InkjetPrinter() };
		new Client().printAllEverywhere(figures, printers);
	}
}