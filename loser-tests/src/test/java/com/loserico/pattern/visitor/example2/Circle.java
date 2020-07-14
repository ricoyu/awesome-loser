package com.loserico.pattern.visitor.example2;

public class Circle implements Shape {
	
	public void printOn(Printer printer) {
		printer.printCircle(this); // <-- the "trick" !
	}
}