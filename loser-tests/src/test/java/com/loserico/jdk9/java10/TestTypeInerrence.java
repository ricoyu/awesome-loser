package com.loserico.jdk9.java10;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Supplier;

public class TestTypeInerrence {

    @Test
    public void test() {
	    var person = new Person("Loser", 18);
        System.out.println(person.name);
        var now = new Date();
        System.out.println(now);
        var arrayList = new ArrayList<String>();
        arrayList.add(person.name);
        arrayList.forEach(System.out::println);

        for(var s : arrayList) {
            System.out.println(s);
        }

    }

    class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    public void testSupplier() {
//    	var supplier1 = () -> Math.random();
    	Supplier<Double> supplier2 = () -> Math.random();
        supplier2.get();
    }

    @Test
    public void testArrays() {
        var array1 = new int[3];
        var array = new int[]{1, 2, 3};

    }
}
