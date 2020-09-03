package com.loserico.websocket;

import com.loserico.common.lang.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

/**
 * <p>
 * Copyright: (C), 2020-08-20 16:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class CommandlineWebSocketClient extends WebSocketClient {
	
	public CommandlineWebSocketClient(String url) throws URISyntaxException {
		super(new URI(url));
	}
	
	@Override
	public void onOpen(ServerHandshake shake) {
		System.out.println("握手...");
		for (Iterator<String> it = shake.iterateHttpFields(); it.hasNext(); ) {
			String key = it.next();
			System.out.println(key + ":" + shake.getFieldValue(key));
		}
	}
	
	@Override
	public void onMessage(String paramString) {
		System.out.println("客户端接收到消息：" + paramString);
	}
	
	@Override
	public void onClose(int paramInt, String paramString, boolean paramBoolean) {
		System.out.println("关闭...");
	}
	
	@Override
	public void onError(Exception e) {
		System.out.println("异常" + e);
		
	}
	
	public static void main(String[] args) {
		try {
			CommandlineWebSocketClient client = new CommandlineWebSocketClient("ws://127.0.0.1:8081/ws/agent");
			client.connect();
			//while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
				log.info("Connecting...");
			//}
			log.info("Connected.");
			
			IOUtils.readCommandLine(message -> client.send(message));
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}