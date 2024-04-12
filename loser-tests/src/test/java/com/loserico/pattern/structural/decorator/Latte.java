package com.loserico.pattern.structural.decorator;

class Latte implements Coffee {
    @Override
    public String getDescription() {
        return "拿铁";
    }

    @Override
    public double cost() {
        return 2.99;
    }
}