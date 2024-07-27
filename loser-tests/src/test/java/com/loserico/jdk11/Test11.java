package com.loserico.jdk11;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Test11 {

    @Test
    public void testTypeInference() {
        Consumer<String> consumer = (@Deprecated var s) -> System.out.println(s);
        consumer.accept("Hello, World!");
    }

    @Test
    public void testString() {
        System.out.println("   ".isBlank());
        //去除字符串首尾空白
        System.out.println("  Hello,  World!  ".strip()); // "Hello,  World!"
        //去除字符串尾部空格
        System.out.println("  Hello, World!  ".stripTrailing()); // "  Hello, World!"
        //去除字符串首部空格
        System.out.println("  Hello, World!  ".stripLeading()); // "Hello, World!"
        //复制字符串
        System.out.println("Java".repeat(3)); // "JavaJavaJava"
        //行数统计
        System.out.println("A\nB\nC".lines().count()); // 3
    }

    @Test
    public void testOptional() {
        //Stream 处理结果要通过Optional 来封装
        var list = List.of(1, 89, 63, 45, 72, 65, 41, 65, 82, 35, 95, 100);
        //获取最大值, 使用Optional尽量避免使用空值
        Optional<Integer> optional = list.stream().max(Integer::compareTo);
        optional.ifPresentOrElse((i) -> {
            System.out.println(i);
        }, () -> {
            System.out.println("没有最大值");
        });

        System.out.println(optional.orElse(200));

        Optional<Object> optinoal2 = Optional.ofNullable(null);
        optinoal2.orElseThrow();
        System.out.println(optinoal2.or(() -> Optional.of(100)));
    }

}
