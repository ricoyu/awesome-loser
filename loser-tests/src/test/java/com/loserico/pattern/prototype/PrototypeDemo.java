package com.loserico.pattern.prototype;

public class PrototypeDemo {
    public static void main(String[] args) {
        // 创建原型对象
        ConcretePrototype prototype1 = new ConcretePrototype("001", "TypeA");
        System.out.println("Original Object: " + prototype1);

        // 克隆原型对象
        ConcretePrototype clonedPrototype = prototype1.clone();
        System.out.println("Cloned Object: " + clonedPrototype);
    }
}