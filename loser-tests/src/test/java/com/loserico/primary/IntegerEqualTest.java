package com.loserico.primary;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class IntegerEqualTest {

    @Test
    public void test() {
        Integer a = 19000000;
        Integer b = 19000000;
        System.out.println(a == b);

        Map<Integer, String> map = new HashMap<>();
        map.put(a, "我是3");
        System.out.println(map.get(b));
    }
}
