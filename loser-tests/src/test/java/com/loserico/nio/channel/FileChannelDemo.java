package com.loserico.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {

    public static void main(String[] args) throws IOException {
        try(RandomAccessFile file = new RandomAccessFile("D:\\Learning\\awesome-loser\\loser-tests\\outagain.txt", "r")){
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(64);

            while (channel.read(buffer) != -1) {
                buffer.flip(); //切换到读模式
                while (buffer.hasRemaining()) {
                    System.out.print((char)buffer.get());
                }
                buffer.clear(); //清空缓冲区
            }
        }
    }
}
