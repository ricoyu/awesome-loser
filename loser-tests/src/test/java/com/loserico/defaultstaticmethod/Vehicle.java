package com.loserico.defaultstaticmethod;

public interface Vehicle {
    // 定义一个抽象方法
    String getBrand();

    // 定义一个默认方法
    default void start() {
        System.out.println("Starting the " + getBrand());
    }
}