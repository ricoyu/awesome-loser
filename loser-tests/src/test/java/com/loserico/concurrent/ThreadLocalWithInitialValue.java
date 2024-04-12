package com.loserico.concurrent;

public class ThreadLocalWithInitialValue {

    public static void main(String[] args) {
        // 使用Lambda表达式提供初始值
        ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 5);

        System.out.println(threadLocal.get()); // 输出5

        threadLocal.set(10);
        System.out.println(threadLocal.get()); // 输出10

        threadLocal.remove();
        System.out.println(threadLocal.get()); // 输出5，因为移除后重新初始化
    }
}