package com.loserico.jvm;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StringConstTest {

    @Test
    public void test() {
    	String a = "abc";
        String b = "abc";
         assertTrue(a == b);
    }

    @Test
    public void test2() {
    	String i = "i";
        String j = new String("i");
        System.out.println( i == j);
    }
}
