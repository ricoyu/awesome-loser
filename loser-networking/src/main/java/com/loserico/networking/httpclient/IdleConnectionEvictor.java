package com.loserico.networking.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * 清除连接池中过期的连接
 * <p>
 * Copyright: Copyright (c) 2019-12-25 11:59
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class IdleConnectionEvictor extends Thread {
	
	private final HttpClientConnectionManager connectionManager;
	private volatile boolean shutdown;
	
	public IdleConnectionEvictor(HttpClientConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		this.start();
	}
	
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(5000);
					// 关闭失效的连接
					connectionManager.closeExpiredConnections();
				}
			}
		} catch (InterruptedException ex) {
			// 结束
		}
	}
	
	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}