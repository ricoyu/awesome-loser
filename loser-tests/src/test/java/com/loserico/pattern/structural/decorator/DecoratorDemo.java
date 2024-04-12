package com.loserico.pattern.structural.decorator;

public class DecoratorDemo {

    public static void main(String[] args) {
        Coffee espresso = new Espresso();
        System.out.println(espresso.getDescription() + " 价格: " + espresso.cost());

        Coffee latte = new MilkDecorator(new Espresso());
        System.out.println(latte.getDescription() + " 价格: " + latte.cost());

        Coffee caramelMacchiato = new CaramelDecorator(new MilkDecorator(new Espresso()));
        System.out.println(caramelMacchiato.getDescription() + " 价格: " + caramelMacchiato.cost());
    }
}
