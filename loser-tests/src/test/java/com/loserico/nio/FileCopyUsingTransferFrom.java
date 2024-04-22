package com.loserico.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCopyUsingTransferFrom {

    public static void main(String[] args) throws IOException {
        String srcFile = "d:/01 课程介绍.mp4";
        String dstFile = "d:/课程介绍.mp4";

        try (FileInputStream fis = new FileInputStream(srcFile);
             FileOutputStream fos = new FileOutputStream(dstFile);
             FileChannel sourceChannel = fis.getChannel();
             FileChannel dstChannel = fos.getChannel()) {
            long position = 0;
            long size = sourceChannel.size();
            dstChannel.transferFrom(sourceChannel, position, size);
        }
    }
}
