package com.loserico.concurrent;

public class ThreadLocalExample {

    public static void main(String[] args) {

        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        // 设置值
        threadLocal.set(1);
        // 获取值
        System.out.println(threadLocal.get()); // 输出当前线程中threadLocal的值
        // 移除值
        threadLocal.remove();
        // 再次获取值，由于之前已经移除，所以这里将返回null
        System.out.println(threadLocal.get()); // 输出null
    }
}