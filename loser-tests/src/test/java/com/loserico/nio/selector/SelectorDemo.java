package com.loserico.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.charset.StandardCharsets.UTF_8;

public class SelectorDemo {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);
        ssc.register(selector, OP_ACCEPT);

        System.out.println("完成在8080端口监听");
        while (true) {
            selector.select(); //阻塞，直到有就绪事件
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (key.isAcceptable()) {
                    //处理连接就绪事件
                    SocketChannel sc = ssc.accept();
                    System.out.println("有客户端连接上来了");
                    sc.configureBlocking(false);
                    sc.register(selector, OP_READ);
                }
                if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(64);
                    if (sc.read(buffer) != -1) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        String msg = new String(bytes, UTF_8);
                        System.out.println("收到客户端消息: " + msg);
                        buffer.clear();
                        sc.write(ByteBuffer.wrap("这是哥的回复".getBytes(UTF_8)));
                        System.out.println();
                    }

                }
                it.remove();
            }
        }
    }


}
