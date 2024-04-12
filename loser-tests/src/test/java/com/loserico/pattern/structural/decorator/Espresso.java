package com.loserico.pattern.structural.decorator;

class Espresso implements Coffee {
    @Override
    public String getDescription() {
        return "意式浓缩";
    }

    @Override
    public double cost() {
        return 1.99;
    }
}