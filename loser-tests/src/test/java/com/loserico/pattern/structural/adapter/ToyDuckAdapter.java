package com.loserico.pattern.structural.adapter;

class ToyDuckAdapter implements Bird {
    ToyDuck toyDuck;
    
    public ToyDuckAdapter(ToyDuck toyDuck) {
        this.toyDuck = toyDuck;
    }
    
    public void fly() {
        // 玩具鸭不会飞，所以这里可以为空实现，或者打印一条信息表示玩具鸭不会飞
        System.out.println("玩具鸭不会飞");
    }
    
    public void makeSound() {
        // 调用玩具鸭的叫声
        toyDuck.squeak();
    }
}
