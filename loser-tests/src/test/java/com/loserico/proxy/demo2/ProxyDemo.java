package com.loserico.proxy.demo2;

import java.lang.reflect.Proxy;

public class ProxyDemo {

    public static void main(String[] args) {
        Foo proxy = (Foo)Proxy.newProxyInstance(Foo.class.getClassLoader(),
                new Class[]{Foo.class},
                new FooProxy(new FooImpl()));
        proxy.sayHello("I am Sexy Uncle!");
    }
}
