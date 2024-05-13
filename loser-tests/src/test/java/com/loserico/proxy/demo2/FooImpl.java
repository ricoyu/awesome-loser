package com.loserico.proxy.demo2;

public class FooImpl implements Foo{
    @Override
    public void sayHello(String msg) {
        System.out.println("FooImpl "+ msg);
    }
}
