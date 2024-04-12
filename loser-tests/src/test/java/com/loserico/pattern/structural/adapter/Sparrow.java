package com.loserico.pattern.structural.adapter;

class Sparrow implements Bird {
    public void fly() {
        System.out.println("麻雀飞翔");
    }
    
    public void makeSound() {
        System.out.println("麻雀叫声: 啾啾");
    }
}