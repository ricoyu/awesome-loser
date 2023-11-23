package com.loserico.nio;

import java.nio.ByteBuffer;
import java.nio.InvalidMarkException;

public class ByteBufferMarkDemo {
    
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 写入一些数据
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);
        printStatus(buffer);
        // 设置 mark 在位置 3
        buffer.position(3);
        printStatus(buffer);
        buffer.mark();

        // 改变 position 的值，小于 mark 的值
        buffer.position(1);
        printStatus(buffer);

        try {
            // 尝试重置到 mark，将会抛出异常
            buffer.reset();
        } catch (InvalidMarkException e) {
            System.out.println("Caught InvalidMarkException as expected.");
        }
    }
    
    private static void printStatus(ByteBuffer buffer) {
        System.out.println("Position: " + buffer.position() +
                ", Limit: " + buffer.limit() +
                ", Capacity: " + buffer.capacity());
    }
}
