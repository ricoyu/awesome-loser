package com.loserico.pattern.structural.adapter;

class PlasticToyDuck implements ToyDuck {
    public void squeak() {
        System.out.println("塑料玩具鸭叫声: 嘎嘎");
    }
}