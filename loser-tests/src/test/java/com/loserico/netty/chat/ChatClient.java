package com.loserico.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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

/**
 * <p>
 * Copyright: (C), 2020-09-09 8:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ChatClient {
	
	@SneakyThrows
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap()
					.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new StringEncoder());
							pipeline.addLast(new StringDecoder());
							pipeline.addLast(new ChatClientHandler());
						}
					});
			
			ChannelFuture channelFuture = bootstrap.connect("localhost", 9000).sync();
			//得到channel
			Channel channel = channelFuture.channel();
			log.info("========" + channel.localAddress() + "========");
			
			//客户端需要输入信息， 创建一个扫描器
			//IOUtils.readCommandLine(msg -> channel.writeAndFlush(msg));
			for (int i = 0; i < 200; i++) {
				channel.writeAndFlush("Hello 三少爷");
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}
