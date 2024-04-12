package com.loserico.nio.buffer;

import java.nio.ByteBuffer;

public class BufferOperationDemo {

    public static void main(String[] args) {
        // 分配一个容量为10的新字节缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        printBufferState("分配后", buffer);

        // 写模式：向缓冲区写入数据
        buffer.put((byte) 'H')
                .put((byte) 'e')
                .put((byte) 'l')
                .put((byte) 'l')
                .put((byte) 'o');
        printBufferState("写入数据后", buffer);

        // 调用flip()切换到读模式
        buffer.flip();
        printBufferState("flip()后切换到读模式", buffer);

        // 读模式：从缓冲区读取数据
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        System.out.println();
        printBufferState("读取数据后", buffer);

        // 调用clear()清空缓冲区，准备再次写入数据
        buffer.clear();
        printBufferState("clear()后准备重新写入", buffer);

        // 再次写入数据
        buffer.put((byte) 'W')
                .put((byte) 'o')
                .put((byte) 'r')
                .put((byte) 'l')
                .put((byte) 'd');
        printBufferState("再次写入数据后", buffer);

        // 再次切换到读模式并读取数据
        buffer.flip();
        printBufferState("第二次flip()后切换到读模式", buffer);
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        System.out.println();
        printBufferState("读取新数据后", buffer);
    }

    // 辅助方法，打印缓冲区的当前状态
    private static void printBufferState(String message, ByteBuffer buffer) {
        System.out.println(message + ":");
        System.out.printf("Position：%d, Limit：%d, Capacity：%d%n", buffer.position(), buffer.limit(), buffer.capacity());
        System.out.println();
    }
}
