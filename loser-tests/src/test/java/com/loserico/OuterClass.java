package com.loserico;

public class OuterClass {
    private String privateData = "I am private!";

    public void display() {
        // 创建匿名内部类，实现Runnable接口
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // 访问外部类的私有变量
                System.out.println(privateData);
            }
        };

        // 执行匿名内部类的方法
        r.run();
    }

    public static void main(String[] args) {
        OuterClass outer = new OuterClass();
        outer.display();
    }
}