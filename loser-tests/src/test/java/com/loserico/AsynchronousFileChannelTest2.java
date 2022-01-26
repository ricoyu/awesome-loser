package com.loserico;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.util.concurrent.Future;

/**
 * <p>
 * Copyright: (C), 2022-01-25 15:19
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AsynchronousFileChannelTest2 {
	
	public static void main(String[] args) throws IOException {
		AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("C:\\Users\\ricoy\\Documents\\a.txt"));
		
		//一旦实例化完成，我们就可以着手准备将数据读入到 Buffer 中
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		//异步文件通道的读操作和写操作都需要提供一个文件的开始位置，文件开始位置为 0
		Future<Integer> future = channel.read(byteBuffer, 0);
	}
}
