https://github.com/xetorthio/jedis/wiki 

# 通用配置项

* **redis.default.enabled true**

  默认会找localhost:6379的单实例Redis

* **redis.host 默认 localhost**
* **redis.port 6379**
* **redis.password**
* **redis.db 0**

* **redis.maxTotal 默认 400**
  最大连接数，如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
* **redis.maxIdle 默认 100**
  最大空闲数，控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值是8。
* **redis.minIdle 10**
  最小空闲数
* **redis.testOnBorrow false**
  是否在从池中取出连接前进行检验，如果检验失败，则从池中去除连接并尝试取出另一个
* **redis.testOnReturn false**
  在return给pool时，是否提前进行validate操作
* **redis.testWhileIdle false**
  在空闲时检查有效性，默认false
* **redis.timeBetweenEvictionRunsMillis 60000**
  表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；或者说表示idle object evitor两次扫描之间要sleep的毫秒数
* **redis.minEvictableIdleTimeMillis 30000**
  这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
* **redis.numTestsPerEvictionRun 1000**
  表示idle object evitor每次扫描的最多的对象数
* **redis.numTestsPerEvictionRun 1000**
  表示idle object evitor每次扫描的最多的对象数
* **redis.maxWaitMillis 3000**
  等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException

# 单节点配置

src/main/resources/redis.properties

```properties
#单节点用法
redis.host=localhost
redis.port=6379
redis.password=deepdata$
redis.db=12
```

# Sentinel配置

src/main/resources/redis.properties

```properties
#sentinel用法
redis.sentinels=192.168.10.101:26379,192.168.10.102:26379,192.168.10.103:26379
redis.maserName=mymaster
redis.password=deepdata$
redis.timeout=2000
redis.db=0
```

# Cluster 配置

src/main/resources/redis.properties

* redis.clusters=192.168.10.201:6379,192.168.10.201:6380,192.168.10.202:6379,192.168.10.201:6380,192.168.10.203:6379,192.168.10.201:6380
  后面跟的是集群中所有Master, Slave的IP:PORT
* redis.cluster.connectionTimeout 默认5000
  表示连接超时时间
* redis.cluster.soTimeout 默认2000
  表示读取数据超时时间
* redis.cluster.maxAttempts 默认 3
  出现异常最大重试次数

```properties
redis.clusters=192.168.2.201:6379,192.168.2.201:6380,192.168.2.202:6379,192.168.2.201:6380,192.168.2.203:6379,192.168.2.201:6380
redis.password=deepdata$
redis.timeout=2000
redis.db=0
```

# 初始化流程

1. 先看是否配置了`redis.sentinels`, 如果配置了则创建基于Sentinel的JedisPool
2. 否则看是否配置了`redis.clusters`, 如果配置了则创建JedisCluster
3. 如果上述两项都没有配置, 则看是否配置了`redis.default.enabled`, 如果为true则创建一个单实例的JedisPool
4. 如果是单实例或者哨兵模式, 可以对JedisPool预热, 通过参数redis.warmUp=true开启, 默认true

# 用法

常用的Redis API都支持了, 也有一些比较有特色的API

1. string操作

   ```
   JedisUtils.set("key1", "v1")
   JedisUtils.get("key1")
   ```

2. HASH操作, 支持HASH的field过期

   ```java
   JedisUtils.HASH.hset("k1", "field1", "v1");     //无过期时间的版本 
   JedisUtils.HASH.hset("k1", "field1", "v1", 12); //field1 12秒后过期, k1不过期
   ```

3. 分布式锁

   ```java
   Lock lock = JedisUtils.lock(LOCK_KEY + msgId, 5000);
   if (!lock.locked()) {
     log.warn("请不要重复消费消息{}", msgTxtVO);
     channel.basicReject(deliveryTag, false);
     return;
   }
   ```

4. 一些泛型方法

   * key是字符串, value是一个ArrayList, 通过Jackson序列化反序列化

     ```java
     public static <T> List<T> getList(String key, Class<T> clazz)
     ```

   * value不是字符串的情况, 使用Jackson反序列化

     ```java
     public static boolean set(String key, Object value)
     public static <T> T get(String key, Class<T> clazz)
     ```

     