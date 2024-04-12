package com.loserico.pattern.structural.bridge;

public class BridgePatternDemo {

    public static void main(String[] args) {
        Shape redCircle = new Circle(new Red());
        Shape blueSquare = new Square(new Blue());
        Shape greenTriangle = new Triangle(new Green());

        System.out.println(redCircle.draw());
        System.out.println(blueSquare.draw());
        System.out.println(greenTriangle.draw());
    }
}