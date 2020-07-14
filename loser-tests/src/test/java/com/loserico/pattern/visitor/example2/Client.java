package com.loserico.pattern.visitor.example2;

import java.util.ArrayList;
import java.util.List;

public class Client {

	/** Prints all figures on each of the printers. */
	public void printAllEverywhere(Shape[] shapes, Printer[] printers) {
		for (int i = 0; i < shapes.length; i++) {
			Shape figure = shapes[i];
			for (int j = 0; j < printers.length; j++) {
				Printer printer = printers[j];
				figure.printOn(printer);
			}
		}
	}

	public static void main(String[] args) {
		List<Shape> shapeList = new ArrayList<Shape>();
		shapeList.add(new Rectangle());
		shapeList.add(new Circle());
		Shape[] shapes = new Shape[2];
		shapeList.toArray(shapes);

		List<Printer> printerList = new ArrayList<Printer>();
		printerList.add(new InkjetPrinter());
		printerList.add(new PostscriptPrinter());
		Printer[] printers = printerList.toArray(new Printer[printerList.size()]);

		Client client = new Client();
		client.printAllEverywhere(shapes, printers);
	}

}