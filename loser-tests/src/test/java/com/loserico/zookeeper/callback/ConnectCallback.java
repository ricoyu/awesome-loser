package com.loserico.zookeeper.callback;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;

import com.loserico.zookeeper.ZookeeperClient;

/**
 * Zookeeper客户端连接成功后回调函数
 * 
 * WatcherEvent.type == KeeperState.SyncConnected
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

	public void process(ZookeeperClient zookeeperClient, WatchedEvent event);
}
