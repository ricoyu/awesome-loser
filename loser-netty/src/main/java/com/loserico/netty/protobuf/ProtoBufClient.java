package com.loserico.netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * <p>
 * Copyright: (C), 2023-11-07 16:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProtoBufClient {
	
	public void connect(String host, int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//加一个消息长度解决粘包拆包问题
							ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
							//负责对想对端写的数据进行编码
							ch.pipeline().addLast(new ProtobufEncoder());
							ch.pipeline().addLast(new ProtoBufClientHandler());
						}
					});
			
			ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new ProtoBufClient().connect("localhost", 8080);
	}
}
