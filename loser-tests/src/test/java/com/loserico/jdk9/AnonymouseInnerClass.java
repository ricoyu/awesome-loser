package com.loserico.jdk9;

public class AnonymouseInnerClass {

    public static void main(String[] args) {
        Person<String> person = new Person<>() {
            @Override
            public void eat(String s) {
                super.eat(s);
            }
        };
        person.eat("烧蹄子");
    }
}

class Person<T> {

    public void eat(T t) {
        System.out.println(t);
    }
}
