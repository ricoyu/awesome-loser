package com.loserico.concurrent.executor;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * Copyright: (C), 2021-01-28 16:28
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NetworkService implements Runnable {
	
	private final ServerSocket serverSocket;
	
	private final ExecutorService pool;
	
	@SneakyThrows
	public NetworkService(int port, int poolSize) {
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(poolSize);
	}
	
	@Override
	public void run() {
		try {
			for (; ; ) {
				pool.execute(new Handler(serverSocket.accept()));
			}
		} catch (IOException e) {
			pool.shutdown();
		}
	}
}

class Handler implements Runnable {
	private final Socket socket;
	
	public Handler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// read and service request on socket
	}
}
