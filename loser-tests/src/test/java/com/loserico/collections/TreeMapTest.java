package com.loserico.collections;

import org.junit.Test;

import java.util.TreeMap;

public class TreeMapTest {

    @Test
    public void test() {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(1, "a");
        treeMap.put(3, "c");
        treeMap.put(2, "b");
        treeMap.put(4, "d");
        System.out.println(treeMap.firstKey());
        System.out.println(treeMap.lastKey());
        treeMap.remove(4);
        System.out.println(treeMap.floorKey(4)); //返回<=4的最大的一个key
        System.out.println(treeMap.ceilingKey(4)); //返回>=4的最小的一个key
    }
}
