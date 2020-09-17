package com.loserico.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Copyright: (C), 2020-09-17 9:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class HeartBeatClient {
	
	@SneakyThrows
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new StringEncoder());
							pipeline.addLast(new StringDecoder());
							pipeline.addLast(new HeartBeatClientHandler());
						}
					});
			
			log.info("Netty client starting ...");
			
			Channel channel = bootstrap.connect("localhost", 9000).sync().channel();
			String text = "Heartbeat Packet";
			
			while (channel.isActive()) {
				int num = ThreadLocalRandom.current().nextInt(10);
				TimeUnit.MILLISECONDS.sleep(num * 1000);
				channel.writeAndFlush(text);
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}
