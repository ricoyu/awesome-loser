package com.loserico.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_CONNECT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.charset.StandardCharsets.UTF_8;

public class SelectorClientDemo {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress(8080));
        sc.register(selector, OP_CONNECT | OP_READ);
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                        System.out.println("连接成功");
                        String s = "你好啊老铁!";
                        ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(UTF_8));
                        channel.write(buffer);
                    }
                }

                if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(64);
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    if (socketChannel.read(buffer) != -1) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        String response = new String(bytes);
                        System.out.println("Server response: " + response);
                    }
                }
            }
        }
    }
}
