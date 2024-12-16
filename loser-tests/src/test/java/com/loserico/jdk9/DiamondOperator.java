package com.loserico.jdk9;

import java.util.Comparator;

public class DiamondOperator {

    public static void main(String[] args) {
        Comparator<String> comparator = new Comparator<>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
            
        };

        Comparator<String> comparator2 = (o1, o2) -> 0;

        System.out.println(comparator.compare("a", "b"));
    }
}
