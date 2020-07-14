package com.loserico.zookeeper;

import org.apache.zookeeper.ZooKeeper;

/**
 * Zookeeper客户端连接成功后回调函数
 * 
 * <p>
 * Copyright: Copyright (c) 2019-04-08 11:02
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public interface ConnectCallback {

	public void process(ZooKeeper zooKeeper);
}
