package com.loserico.pattern.structural.decorator;

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", 加牛奶";
    }

    @Override
    public double cost() {
        return coffee.cost() + 0.3; // 假设加牛奶需要额外支付0.3元
    }
}