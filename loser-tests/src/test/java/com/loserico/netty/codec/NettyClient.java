package com.loserico.netty.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2020-09-11 9:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class NettyClient {
	
	@SneakyThrows
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new StringEncoder());
							pipeline.addLast(new ObjectEncoder());
							pipeline.addLast(new LongToByteEncoder());
							pipeline.addLast(new ByteToLongDecoder());
							pipeline.addLast(new NettyClientHandler());
						}
					});
			log.info("netty client starting...");
			ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
}
