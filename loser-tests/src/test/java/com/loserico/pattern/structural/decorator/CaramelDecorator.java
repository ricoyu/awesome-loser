package com.loserico.pattern.structural.decorator;

class CaramelDecorator extends CoffeeDecorator {
    public CaramelDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", 加焦糖";
    }

    @Override
    public double cost() {
        return coffee.cost() + 0.5; // 假设加焦糖需要额外支付0.5元
    }
}