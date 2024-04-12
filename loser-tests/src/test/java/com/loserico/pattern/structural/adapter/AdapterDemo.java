package com.loserico.pattern.structural.adapter;

public class AdapterDemo {

    public static void main(String[] args) {
        Bird sparrow = new Sparrow();
        ToyDuck toyDuck = new PlasticToyDuck();
        
        // 将玩具鸭适配成鸟
        Bird toyDuckAdapter = new ToyDuckAdapter(toyDuck);
        
        System.out.println("麻雀：");
        sparrow.fly();
        sparrow.makeSound();
        
        System.out.println("适配器转换后的玩具鸭：");
        toyDuckAdapter.fly(); // 实际上玩具鸭不会飞，适配器可以选择空实现或者输出提示
        toyDuckAdapter.makeSound(); // 调用原有玩具鸭的叫声方法
    }
}
