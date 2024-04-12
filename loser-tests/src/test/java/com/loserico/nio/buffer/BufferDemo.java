package com.loserico.nio.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * 演示Buffer的四大属性
 * <p>
 * Copyright: Copyright (c) 2024-04-10 14:10
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BufferDemo {

    public static void main(String[] args) {
        // 创建一个容量为10的ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        print(buffer); // Position = 0, Limit = 10, Capacity = 10

        // 添加3个字节的数据到Buffer
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        print(buffer); // Position = 3, Limit = 10, Capacity = 10

        // 转换为读模式
        buffer.flip();
        print(buffer); // Position = 0, Limit = 3, Capacity = 10

        // 读取一个字节
        buffer.get();
        print(buffer); // Position = 1, Limit = 3, Capacity = 10

        // 标记当前位置
        buffer.mark();

        // 再读取两个字节
        buffer.get();
        buffer.get();
        print(buffer); // Position = 3, Limit = 3, Capacity = 10

        // 重置到标记位置
        buffer.reset();
        print(buffer); // Position = 1, Limit = 3, Capacity = 10
    }

    public static void  print(Buffer buffer) {
        System.out.println("Position = " + buffer.position() + ", Limit = " + buffer.limit() + ", Capacity = " + buffer.capacity());
    }
}
