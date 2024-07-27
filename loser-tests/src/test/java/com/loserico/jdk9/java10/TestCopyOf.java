package com.loserico.jdk9.java10;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCopyOf {

    @Test
    public void test() {
        List<String> strings1 = List.of("MCA", "JAVA", "Golang");
        List<String> strings2 = List.copyOf(strings1);
        //判断两个结合在内存上是否是同一个
        System.out.println(strings2 == strings1); //true

        var strings3 = new ArrayList<String>();
        List<String> strings4 = List.copyOf(strings3);
        System.out.println(strings4 == strings3); //false
    }

}
