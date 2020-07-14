package com.loserico.pattern.singleton;

/**
 * 输出：
 * load outer class...
 * ===========分割线===========
 * load static inner class...
 * static inner method
 * 
 * 结论: 
 * 加载一个类时, 其内部类不会同时被加载。
 * 一个类被加载, 当且仅当其某个静态成员(静态域, 构造器, 静态方法等) 被调用时发生。
 * 
 * <p>
 * Copyright: Copyright (c) 2018-05-05 19:46
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class StaticInnerClassTest {
	
    static {
        System.out.println("load outer class...");
    }
    // 静态内部类
    static class StaticInnerTest {
        static {
            System.out.println("load static inner class...");
        }
        static void staticInnerMethod() {
            System.out.println("static inner method...");
        }
    }
    public static void main(String[] args) {
        StaticInnerClassTest outerTest = new StaticInnerClassTest(); // 此刻其内部类是否也会被加载？
        System.out.println("===========分割线===========");
        StaticInnerTest.staticInnerMethod(); // 调用内部类的静态方法
    }
}