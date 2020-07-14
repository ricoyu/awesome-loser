package com.loserico.zookeeper;

import com.loserico.zookeeper.callback.ConnectCallback;
import com.loserico.zookeeper.exception.ZookeeperException;
import com.loserico.zookeeper.utils.PathUtils;
import com.loserico.zookeeper.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CountDownLatch;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;

/**
 * 
 * <p>
 * Copyright: Copyright (c) 2019-04-08 15:17
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
@Slf4j
public class ZookeeperClient {

	public static final String UNIX_PATH_SEPARATOR = "/";

	private static final byte[] EMPTY_BYTE = new byte[0];;
//	private static final byte[] EMPTY_BYTE = new byte[0];

	/** It is recommended to use quite large sessions timeouts for ZooKeeper. */
	public static final int DEFAULT_SESSION_TIMEOUT = 30000;

	private ZooKeeper zk;

	private ZookeeperClient() {
	}

	/**
	 * 初始化Zookeeper客户端, sessionTimeout值介于ticketTime的2倍到20倍之间, ticketTime默认值是2000
	 * 
	 * 127.0.0.1:4545/app/a
	 * 127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002/app/a
	 * 如果url是带chroot格式的, 必须先初始化/app/a, 否则后续在这个后缀下的所有操作都会报NoNodeException
	 * 
	 * @param url
	 * @param sessionTimeout
	 * @on
	 */
	public static ZookeeperClient initialize(String url, int sessionTimeout) {
		ZookeeperClient zookeeperClient = new ZookeeperClient();
		try {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			zookeeperClient.zk = new ZooKeeper(url, sessionTimeout, (event) -> {
				if (event.getState() == KeeperState.SyncConnected) {
					countDownLatch.countDown();
				}
			});
			initializeChroot(url);
			countDownLatch.await();
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
		return zookeeperClient;
	}

	/**
	 * 初始化Zookeeper客户端, 并提供回调函数入口
	 * 
	 * @param url
	 * @param sessionTimeout
	 * @param connectCallback
	 */
	public static ZookeeperClient initialize(String url, int sessionTimeout, ConnectCallback connectCallback) {
		ZookeeperClient zookeeperClient = new ZookeeperClient();
		try {
			CountDownLatch countDownLatch = new CountDownLatch(1);
			zookeeperClient.zk = new ZooKeeper(url, sessionTimeout, (event) -> {
				if (event.getState() == KeeperState.SyncConnected) {
					countDownLatch.countDown();
					connectCallback.process(zookeeperClient, event);
				}
			});
			initializeChroot(url);
			countDownLatch.await();
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
		return zookeeperClient;
	}

	/**
	 * 返回Zookeeper客户端
	 * 
	 * @return
	 */
	public ZooKeeper zk() {
		return this.zk;
	}

	/**
	 * 创建持久节点, 父节点不存在的话自动创建(父节点都是PERSISTENT)
	 * @param nodePath
	 * @param nodeValue
	 * @return String 创建的节点的完整路径名
	 * @on
	 */
	public String createPersistent(String nodePath, String nodeValue) {
		return create(nodePath, nodeValue, CreateMode.PERSISTENT);
	}

	/**
	 * 创建持久节点, 父节点不存在的话自动创建(父节点都是PERSISTENT)
	 * @param nodePath
	 * @return String 创建的节点的完整路径名
	 * @on
	 */
	public String createPersistent(String nodePath) {
		return create(nodePath, null, CreateMode.PERSISTENT);
	}

	/**
	 * 创建临时节点, 父节点不存在的话自动创建(父节点都是PERSISTENT)
	 * 
	 * @param nodePath
	 * @return String 创建的节点的完整路径名
	 * @on
	 */
	public String createEphemeral(String nodePath, String nodeValue) {
		return create(nodePath, nodeValue, CreateMode.EPHEMERAL);
	}

	/**
	 * 创建临时节点, 父节点不存在的话自动创建(父节点都是PERSISTENT)
	 * 
	 * @param nodePath
	 * @return String 创建的节点的完整路径名
	 * @on
	 */
	public String createEphemeral(String nodePath) {
		return create(nodePath, null, CreateMode.EPHEMERAL);
	}

	/**
	 * 创建节点并返回值, 如果节点已经存在, 返回null
	 * @param nodePath
	 * @param nodeValue
	 * @param mode
	 * @return String 创建的节点的完整路径名
	 * @on
	 */
	public String create(String nodePath, String nodeValue, CreateMode mode) {
		if (StringUtils.isBlank(nodePath)) {
			throw new RuntimeException("Missing value for nodePath");
		}

		byte[] data = EMPTY_BYTE;
		if (StringUtils.isNotBlank(nodeValue)) {
			data = nodeValue.getBytes(UTF_8);
		}

		String[] paths = PathUtils.pathSplit(nodePath);
		try {
			StringJoiner stringJoiner = new StringJoiner("/");
			stringJoiner.add("");
			for (int i = 0; i < paths.length; i++) {
				String path = stringJoiner.add(paths[i]).toString();
				if (zk.exists(path, false) == null) {
					log.info("Create node {}", path);
					if (i < paths.length - 1) {
						// 父节点必须是持久型节点
						try {
							zk.create(path, EMPTY_BYTE, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
						} catch (Exception e) {
							if (e instanceof NodeExistsException) {
								log.info("父节点{}已经存在, 不用创建", path);
							} else {
								throw e;
							}
						}
						continue;
					}

					// 最后一个节点才可能是临时节点
					try {
						return zk.create(path, data, Ids.OPEN_ACL_UNSAFE, mode);
					} catch (Exception e) {
						if (e instanceof NodeExistsException) {
							log.info("尾节点{}已经存在, 不用创建", path);
						} else {
							throw e;
						}
					}
				}
			}
		} catch (KeeperException | InterruptedException e) {
			log.error("", e);
			throw new ZookeeperException(e);
		}

		return null;
	}

	/**
	 * 节点数据是字符串的情况
	 * 
	 * @param path
	 * @param watch
	 * @return String 节点的值
	 */
	public String getStr(String path, boolean watch) {
		try {
			byte[] data = zk.getData(path, watch, new Stat());
			//return com.loserico.cache.utils.UnMarshaller.toString(data);
			return "";
		} catch (KeeperException | InterruptedException e) {
			if (e instanceof NoNodeException) {
				log.info("不存在节点{}", path);
				return null;
			}
			log.error("", e);
			throw new ZookeeperException(e);
		}
	}

	/**
	 * 节点数据是字符串的情况, 并设置Watcher
	 * 值得注意的是这里的Watcher会反复注册, 就是说节点有数据改变会不断通知到Watcher
	 * 
	 * @param path
	 * @param watcher
	 * @return String 节点的值
	 * @on
	 */
	public String getStr(String path, Watcher watcher) {
		try {
			byte[] data = zk.getData(path, (event) -> {
				log.info("Set watcher for node[{}] again", path);
				getData(path, watcher);
				watcher.process(event);
			}, new Stat());
			//return Unmarshaller.toString(data);
			return "";
		} catch (KeeperException | InterruptedException e) {
			if (e instanceof NoNodeException) {
				log.info("不存在节点{}", path);
				return null;
			}
			log.error("", e);
			throw new ZookeeperException(e);
		}
	}

	/**
	 * 节点数据是Integer的情况
	 * 
	 * @param path
	 * @param watch
	 * @return
	 */
	public Integer getInteger(String path, boolean watch) {
		try {
			byte[] data = zk.getData(path, watch, new Stat());
			//return Unmarshaller.toInteger(data);
			return 0;
		} catch (KeeperException | InterruptedException e) {
			if (e instanceof NoNodeException) {
				log.info("不存在节点{}", path);
				return null;
			}
			log.error("", e);
			throw new ZookeeperException(e);
		}
	}

	/**
	 * 节点数据是Long的情况
	 * 
	 * @param path
	 * @param watch
	 * @return
	 */
	public Long getLong(String path, boolean watch) {
		try {
			byte[] data = zk.getData(path, watch, new Stat());
			//return Unmarshaller.toLong(data);
			return 1L;
		} catch (KeeperException | InterruptedException e) {
			if (e instanceof NoNodeException) {
				log.info("不存在节点{}", path);
				return null;
			}
			log.error("", e);
			throw new ZookeeperException(e);
		}
	}

	/**
	 * 获取节点数据
	 * 
	 * @param <T>
	 * @param path
	 * @param watch
	 * @param clazz
	 * @return T
	 */
	public <T> T getData(String path, boolean watch, Class<T> clazz) {
		try {
			byte[] data = zk.getData(path, watch, new Stat());
			//return Unmarshaller.toObject(data, clazz);
			return null;
		} catch (KeeperException | InterruptedException e) {
			if (e instanceof NoNodeException) {
				log.info("不存在节点{}", path);
				return null;
			}
			log.error("", e);
			throw new ZookeeperException(e);
		}
	}
	
	/**
	 * 获取节点byte[]数据, Watcher只watch一次, 重复watch
	 * @param path
	 * @param watcher
	 * @return byte[]
	 */
	public byte[] getData(String path, Watcher watcher) {
		try {
			return zk.getData(path, (event) -> {
				getData(path, watcher);
				watcher.process(event);
			}, new Stat());
		} catch (KeeperException | InterruptedException e) {
			if (e instanceof NoNodeException) {
				log.info("不存在节点{}", path);
				return null;
			}
			log.error("", e);
			throw new ZookeeperException(e);
		}
	}

	/**
	 * 获取子节点列表, 如果这个节点没有子节点, 返回empty list
	 * 
	 * @param path
	 * @param watcher
	 * @return List<String>
	 */
	public List<String> getChildren(String path, boolean watcher) {
		try {
			return zk.getChildren(path, watcher);
		} catch (KeeperException | InterruptedException e) {
			if (e instanceof NoNodeException) {
				log.info("No children under {}", path);
				return emptyList();
			}
			log.error("", e);
			throw new ZookeeperException(e);
		}
	}

	/**
	 * 删除掉子节点的节点
	 * 
	 * @param path
	 */
	public void rmr(String path) {
		List<String> children = getChildren(path, false);
		if (children.isEmpty()) {
			delete(path);
			return;
		}

		for (String subPath : children) {
			rmr(path + "/" + subPath);
		}

		delete(path);
	}

	/**
	 * 删除节点, 如果节点不存在, 不会抛异常
	 * 
	 * @param path
	 */
	public void delete(String path) {
		try {
			zk.delete(path, -1);
		} catch (InterruptedException | KeeperException e) {
			log.error("", e);
			if (e instanceof NoNodeException) {
				return;
			}
			throw new ZookeeperException(e);
		}
	}
	
	private static void initializeChroot(String url) {
		int slashIndex = url.indexOf("/");
		if (slashIndex == -1) {
			return;
		}
		
		String chroot = url.substring(slashIndex);
		if (chroot.length() != 1) {
			url = url.substring(0, slashIndex);
			CountDownLatch countDownLatch = new CountDownLatch(1);
			try {
				ZooKeeper zooKeeper = new ZooKeeper(url, DEFAULT_SESSION_TIMEOUT, (event) -> {
					if (event.getState() == KeeperState.SyncConnected) {
						countDownLatch.countDown();
					}
				});
				countDownLatch.await();
				ZookeeperClient client = new ZookeeperClient();
				client.zk = zooKeeper;
				client.createPersistent(chroot);
				zooKeeper.close();
			} catch (Exception e) {
				log.error("", e);
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 解析url中的chroot后缀, 没有的话返回null
	 * Added in 3.2.0: An optional "chroot" suffix may also be appended to the connection string. 
	 * This will run the client commands while interpreting all paths relative to this root
	 * (similar to the unix chroot command). 
	 * 
	 * If used the example would look like:
	 * "127.0.0.1:4545/app/a" or "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002/app/a" 
	 * 
	 * where the client would be rooted at "/app/a" and all paths would be relative to this root 
	 * - ie getting/setting/etc... "/foo/bar" would result in operations being run on "/app/a/foo/bar"
	 * (from the server perspective). 
	 * 
	 * This feature is particularly useful in multi-tenant environments where each user of a particular 
	 * ZooKeeper service could be rooted differently.
	 * This makes re-use much simpler as each user can code his/her application as if it were rooted at "/", 
	 * while actual location (say /app/a) could be determined at deployment time.
	 * 
	 * 所有操作的路径都相对于/app/a而言, 比如create /foo/bar "", 创建的节点是/app/a/foo/bar
	 * 
	 * @param url
	 * @return
	 * @on
	 */
	private static String urlSuffix(String url) {
		int slashIndex = url.indexOf("/");
		if (slashIndex == -1) {
			return null;
		}
		
		return url.substring(slashIndex);
	}

}
