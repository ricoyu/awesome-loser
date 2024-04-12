package com.loserico.pattern.creational.builder;

public class BuilderPatternDemo {

    public static void main(String[] args) {
        Car car = Car.builder()
                     .engine("V8")
                     .wheels(4)
                     .color("Red")
                     .brand("Ferrari")
                     .sunroof(true)
                     .build();
        System.out.println(car);
    }
}
