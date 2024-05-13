package com.loserico.proxy.demo2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class FooProxy implements InvocationHandler {

    private Foo target;

    public FooProxy(Foo target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("这是代理类说的: '哥牛逼~~'");
        return method.invoke(target, args);
    }
}
