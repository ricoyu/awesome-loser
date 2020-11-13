package com.loserico.netty.barrage.channel;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * <p>
 * Copyright: (C), 2020-11-07 9:23
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class WebsocketBarrageServerInitializer extends ChannelInitializer<SocketChannel> {
	
	/**
	 * HTTP协议分为两部分
	 * 一部分是请求头
	 * 另外一部分是请求体(大小不确定)
	 * 
	 * 一般是分两步处理, 先处理请求头, 然后根据请求头的Content-Length来确定请求体的大小, 然后再进行处理
	 * 如果请求体是一个上传文件, 那么这个文件可能会被分成很多包, 然后再把它们合在一起
	 * @param ch
	 * @throws Exception
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("http-decodec", new HttpRequestDecoder()); //HTTP协议解码
		/*
		 * 把请求头和请求体合并在一起, 并限制最大65536 bytes
		 */
		pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536)); //64KB
		pipeline.addLast("http-encodec", new HttpResponseEncoder()); //HTTP编码
		pipeline.addLast("http-chunked", new ChunkedWriteHandler());
		
		/*
			pipeline.addLast(new HttpServerCodec());
			pipeline.addLast(new HttpObjectAggregator(64*1024));
			pipeline.addLast(new ChunkedWriteHandler());
		*/
		
		/*
		 * 经过HttpObjectAggregator处理之后, HttpRequestHandler只需要处理一次
		 */
		pipeline.addLast("http-request", new HttpRequestHandler("/ws")); //业务处理
		/*
		 * Netty自带的实现Websocket协议的编码与解码器
		 * 只需要提供给他一个url就可以了, 访问这个url即走Websocket协议
		 * 作为Netty来讲, 只要注册一个Websocket协议的解码器, 就能够实现Websocket协议
		 */
		pipeline.addLast("WebSocket-protocol", new WebSocketServerProtocolHandler("/ws"));
		//实现弹幕发送业务
		pipeline.addLast("WebSocket-request", new TextWebSocketFrameHandler());
	}
}
