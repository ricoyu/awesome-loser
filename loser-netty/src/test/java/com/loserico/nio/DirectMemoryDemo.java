package com.loserico.nio;

import java.nio.ByteBuffer;

public class DirectMemoryDemo {
    public static void main(String[] args) {
        // 分配1MB的直接内存
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024 * 1024);

        // 写入数据到直接内存
        for (int i = 0; i < 1024 * 1024; i++) {
            directBuffer.put((byte) i);
        }

        // 翻转buffer，准备读取数据
        directBuffer.flip();

        // 读取数据
        while (directBuffer.hasRemaining()) {
            byte b = directBuffer.get();
            // 可以在这里处理数据
            System.out.println(b);
        }

        // 清理直接内存（可选, 因为直接内存会在ByteBuffer对象被垃圾回收时自动释放）
        System.gc();
    }
}
