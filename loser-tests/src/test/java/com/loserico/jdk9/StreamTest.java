package com.loserico.jdk9;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void testTakeWhile() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //从头开始取所有奇数,直到遇见一个偶数为止
        list.stream().takeWhile(i -> i % 2 == 1).forEach(System.out::println);
    }

    @Test
    public void testDropWhile() {
        //测试Stream新增dropWhile方法
        //从头开始删除满足条件的数据, 直到遇见第一个不满足的位置, 并保留剩余元素
        List<Integer> list = List.of(6, 8, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //删除流开头所有的偶数,直到遇见奇数为止
        list.stream().dropWhile(i -> i % 2 == 0).forEach(System.out::println);
    }

    @Test
    public void testOfNullable() {
        //of方法获取流, 允许元素中有多个null值
        Stream<Integer> stream1 = Stream.of(10, 20, 30, null);
        //如果元素中只有一个null, 是不允许的
        Stream<Integer> stream2 = Stream.of(null); //java.lang.NullPointerException
        //JAVA9中, 如果元素为null, 返回的是一个空Stream, 如果不为null, 返回一个只有一个元素的Stream
        Stream<Integer> stream3 = Stream.ofNullable(null);
    }

    @Test
    public void testIterate() {
        //JAVA8通过 generate方法获取一个Stream
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
        //JAVA8 通过iterate获取一个Stream
        Stream.iterate(0, t -> t + 2).limit(10).forEach(System.out::println);
        //JAVA9通过重载iterate获取Stream
        Stream.iterate(0, t -> t < 10, t -> t + 1).forEach(System.out::println);
    }

    @Test
    public void testOptionalStream() {
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 10, 5, 45, 95, 36, 85, 47);
        Optional<List<Integer>> optional = Optional.ofNullable(list);

        // 通过optional的Stream方法获取一个Stream
        Stream<List<Integer>> stream = optional.stream();
        // 以为内部的每个元素也是一个List,通过flatMap方法,将内部的List转换为Stream后再放入一个大Stream
        stream.flatMap(x -> x.stream()).forEach(System.out::println);
    }

    @Test
    public void testStreamTransferTo() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("D:\\Dropbox\\doc\\idea  技巧 设置 配置.txt");
        OutputStream outputStream = new FileOutputStream("d:/bbb.txt");
        try (inputStream; outputStream) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadOnlyList() {
        List<String> list= List.of("TOM","Jerry","Mark","Ben");
        System.out.println(list);
    }
}
