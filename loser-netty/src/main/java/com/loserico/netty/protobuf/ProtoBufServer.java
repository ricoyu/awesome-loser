package com.loserico.netty.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * <p>
 * Copyright: (C), 2023-11-07 17:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProtoBufServer {
	
	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//去除消息长度部分, 同时根据这个消息的长度读取实际的数据
							ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
							ch.pipeline().addLast(new ProtobufDecoder(PersonProto.Person.getDefaultInstance()));
							ch.pipeline().addLast(new ProtoBufServerHandler());
						}
					});
			
			ChannelFuture channelFuture = bootstrap.bind(port).sync();
			channelFuture.channel().closeFuture().sync();
		}finally {
			bossGroup.shutdownGracefully().sync();
			workerGroup.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new ProtoBufServer().bind(8080);
	}
}
