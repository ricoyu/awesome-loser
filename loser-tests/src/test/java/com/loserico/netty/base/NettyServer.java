package com.loserico.netty.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2020-09-02 10:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class NettyServer {
	
	@SneakyThrows
	public static void main(String[] args) {
		/*
		 * 创建两个线程组bossGroup和workerGroup, 含有的子线程NioEventLoop的个数默认为cpu核数的两倍
		 * bossGroup只是处理连接请求, 真正的和客户端业务处理, 会交给workerGroup完成
		 */
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			//创建服务器端的启动对象
			ServerBootstrap bootstrap = new ServerBootstrap();
			/*
			 * 使用链式编程来配置参数
			 * 设置两个线程组
			 */
			bootstrap.group(bossGroup, workerGroup)
					//使用NioServerSocketChannel作为服务器的通道实现
					.channel(NioServerSocketChannel.class)
					//初始化服务器连接队列大小, 服务端处理客户端连接请求是顺序处理的, 所以同一时间只能处理一个客户端连接。
					//多个客户端同时来的时候, 服务端将不能处理的客户端连接请求放在队列中等待处理
					.option(ChannelOption.SO_BACKLOG, 1024)
					//创建通道初始化对象，设置初始化参数
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//对workerGroup的SocketChannel设置处理器
							ch.pipeline().addLast(new NettyServerHandler());
						}
					});
			
			log.info("Netty server starting .....");
			
			/*
			 * 绑定一个端口并且同步生成了一个ChannelFuture异步对象, 通过isDone()等方法可以判断异步事件的执行情况
			 * 启动服务器(并绑定端口), bind是异步操作, sync方法是等待异步操作执行完毕
			 */
			ChannelFuture cf = bootstrap.bind(9000).sync();
			
			/*
			 * 对通道关闭进行监听, closeFuture是异步操作, 监听通道关闭
			 * 通过sync方法同步等待通道关闭处理完毕, 这里会阻塞等待通道关闭完成
			 */
			cf.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
