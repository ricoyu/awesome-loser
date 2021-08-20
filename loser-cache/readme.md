https://github.com/xetorthio/jedis/wiki 

https://blog.didispace.com/JedisPool%E8%B5%84%E6%BA%90%E6%B1%A0%E4%BC%98%E5%8C%96/

https://gist.github.com/JonCole/925630df72be1351b21440625ff2671f

# 摘要

1. redis.host=localhost
2. redis.port=6379
3. redis.password
4. redis.connectionTimeout=50000
5. redis.socketTimeout=1000
6. redis.db=0
7. redis.warmUp=true
8. redis.maxTotal=50
9. redis.maxIdle=50
10. redis.minIdle=8
11. redis.testOnBorrow=true
12. redis.testOnReturn=false
13. redis.blockWhenExhausted=true
14. redis.maxWaitMillis=60000
15. redis.testWhileIdle=true
16. redis.timeBetweenEvictionRunsMillis=30000
17. redis.minEvictableIdleTimeMillis=60000
18. redis.numTestsPerEvictionRun=-1

# Tips

1. maxTotal, maxIdle 设置一样大 50
2. 一个连接的QPS大约是1000(即每次执行一个Jedis操作耗费1ms), 业务期望的QPS是50000, 那么理论上需要的资源池大小是50000 / 1000 = 50个 
3. 通常来讲maxTotal可以比理论值大一些
4. 空闲Jedis对象检测由4个参数组合控制
   * testWhileIdle 
     是否开启空闲资源监测 默认false, 建议true
   * timeBetweenEvictionRunsMillis
     空闲资源的检测周期(单位为毫秒) 默认-1, 参考JedisPoolConfig设置30000
   * minEvictableIdleTimeMillis
     资源池中资源最小空闲时间(单位为毫秒), 达到此值后空闲资源将被移除 默认1000 * 60 * 30 = 30分钟, 参考JedisPoolConfig设置60000
   * numTestsPerEvictionRun
     做空闲资源检测时, 每次的采样数 默认3, 参考JedisPoolConfig设置-1, 表示对所有连接做空闲监测
5. blockWhenExhausted
   当资源池用尽后, 调用者是否要等待。只有当为true时, 下面的maxWaitMillis才会生效 默认 true, 建议 true
6. maxWaitMillis
   用尽后, 调用者的最大等待时间(单位为毫秒) 默认 -1 永不超时, 建议5000
7. testOnBorrow
   向资源池借用连接时是否做连接有效性检测(ping)，无效连接会被移除 默认false, 业务量很大时候建议设置为false(多一次ping的开销)
8. testOnReturn
   向资源池归还连接时是否做连接有效性检测(ping)，无效连接会被移除 默认false, 业务量很大时候建议设置为false(多一次ping的开销)

# 通用配置项

* redis.default.enabled true

  默认会找localhost:6379的单实例Redis

* redis.host 默认 localhost

* redis.port 6379

* redis.sentinels

  ```properties
  redis.sentinels=192.168.100.101:26379,192.168.100.102:26379,192.168.100.103:26379
  ```

* redis.maserName=mymaster
  Sentinel中Master名字

* redis.clusters

  ```properties
  redis.clusters=192.168.100.101:6379,192.168.100.101:6380,192.168.2.102:6379,192.168.2.102:6380,192.168.2.103:6379,192.168.2.103:6380
  ```

* redis.password

* redis.db 0

* redis.timeout

  既表示连接超时, 又表示读写超时

  从Jedis 2.8开始有区分connectionTimeout和soTimeout的构造函数如下

  ```java
    public JedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
        final int connectionTimeout, final int soTimeout, 
        final String password, final int database,
        final String clientName, 
        final boolean ssl, final SSLSocketFactory sslSocketFactory,
        final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
      super(poolConfig, new JedisFactory(host, port, 
      		connectionTimeout, soTimeout, 
      		password, database, clientName, 
      		ssl, sslSocketFactory, sslParameters, hostnameVerifier)
     	);
    }
  ```

# 连接超时配置

* redis.connectionTimeout
  连接超时时间 默认50000
* redis.socketTimeout
  socket timeout时间, 就是一次命令的执行不能超过这个时间 默认1000



# 最大最小资源数

* redis.maxTotal 默认 50
  最大连接数, 如果赋值为-1, 则表示不限制; 如果pool已经分配了maxActive个jedis实例, 则此时pool的状态为exhausted(耗尽)。
* redis.maxIdle 默认 50
  最大空闲数, 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例, 默认值是8。
* redis.minIdle 8
  最小空闲数

# 测试连接可用性

* redis.testOnBorrow true
  向资源池借用连接时是否做连接有效性检测(ping), 无效连接会被移除, 如果检验失败, 则从池中去除连接并尝试取出另一个
  
  业务量很大时候建议设置为false(多一次ping的开销)。
  
* redis.testOnReturn false
  向资源池归还连接时是否做连接有效性检测(ping)，无效连接会被移除. 
  
  业务量很大时候建议设置为false(多一次ping的开销)。

# 资源用尽时处理

* redis.blockWhenExhausted=true
* redis.maxWaitMillis 5000
  等待可用连接的最大时间, 单位毫秒, 默认值为-1, 表示永不超时。如果超过等待时间, 则直接抛出JedisConnectionException

# 空闲资源监测

1. redis.testWhileIdle true
   是否开启空闲资源监测 默认false, 建议true

2. redis.timeBetweenEvictionRunsMillis 30000
   空闲资源的检测周期(单位为毫秒) 默认-1, 建议 30000

3. redis.minEvictableIdleTimeMillis 60000

   资源池中资源最小空闲时间(单位为毫秒), 达到此值后空闲资源将被移除 默认 1000 * 60 * 30 = 30分钟, 建议60000

4. numTestsPerEvictionRun -1
   做空闲资源检测时, 每次的采样数, 默认3, 建议 -1
   可根据自身应用连接数进行微调, 如果设置为-1, 就是对所有连接做空闲监测

```java
package redis.clients.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class JedisPoolConfig extends GenericObjectPoolConfig {
  public JedisPoolConfig() {
    // defaults to make your life with connection pool easier :)
    setTestWhileIdle(true);
    setMinEvictableIdleTimeMillis(60000);
    setTimeBetweenEvictionRunsMillis(30000);
    setNumTestsPerEvictionRun(-1);
  }
}
```



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

     

# JedisPool配置说明

JedisPool保证资源在一个可控范围内, 并且提供了线程安全, 但是一个合理的GenericObjectPoolConfig 配置能为应用使用Redis保驾护航, 下面将对它的一些重要参数进行说明和建议: 

| 序号 | 参数名             | 含义                                                         | 默认值      | 使用建议                                          |
| :--- | :----------------- | :----------------------------------------------------------- | :---------- | :------------------------------------------------ |
| 1    | maxTotal           | 资源池中最大连接数                                           | 8           | 设置建议见下节                                    |
| 2    | maxIdle            | 资源池允许最大空闲的连接数                                   | 8           | 设置建议见下节                                    |
| 3    | minIdle            | 资源池确保最少空闲的连接数                                   | 0           | 设置建议见下节                                    |
| 4    | blockWhenExhausted | 当资源池用尽后，调用者是否要等待。只有当为true时，下面的maxWaitMillis才会生效 | true        | 建议使用默认值                                    |
| 5    | maxWaitMillis      | 当资源池连接用尽后，调用者的最大等待时间(单位为毫秒)         | -1 永不超时 | 不建议使用默认值                                  |
| 6    | testOnBorrow       | 向资源池借用连接时是否做连接有效性检测(ping)，无效连接会被移除 | false       | 业务量很大时候建议设置为false(多一次ping的开销)。 |
| 7    | testOnReturn       | 向资源池归还连接时是否做连接有效性检测(ping)，无效连接会被移除 | false       | 业务量很大时候建议设置为false(多一次ping的开销)。 |
| 8    | jmxEnabled         | 是否开启jmx监控，可用于监控                                  | true        | 建议开启，但应用本身也要开启                      |

空闲Jedis对象检测，下面四个参数组合来完成，testWhileIdle是该功能的开关。

| 序号 | 参数名                        | 含义                                                         | 默认值                    | 使用建议                                                     |
| :--- | :---------------------------- | :----------------------------------------------------------- | :------------------------ | :----------------------------------------------------------- |
| 1    | testWhileIdle                 | 是否开启空闲资源监测                                         | false                     | true                                                         |
| 2    | timeBetweenEvictionRunsMillis | 空闲资源的检测周期(单位为毫秒)                               | -1：不检测                | 建议设置, 周期自行选择, 也可以默认也可以使用下面JedisPoolConfig中的配置 |
| 3    | minEvictableIdleTimeMillis    | 资源池中资源最小空闲时间(单位为毫秒)，达到此值后空闲资源将被移除 | `1000 * 60 * 30` = 30分钟 | 可根据自身业务决定, 大部分默认值即可, 也可以考虑使用下面JeidsPoolConfig中的配置 |
| 4    | numTestsPerEvictionRun        | 做空闲资源检测时, 每次的采样数                               | 3                         | 可根据自身应用连接数进行微调, 如果设置为-1, 就是对所有连接做空闲监测 |

**资源池大小(maxTotal)、空闲(maxIdle minIdle)设置建议**

1. maxTotal: 最大连接数

   实际上这个是一个很难回答的问题, 考虑的因素比较多:

   * 业务希望Redis并发量
   * 客户端执行命令时间
   * Redis资源
     例如 nodes(例如应用个数) * maxTotal 是不能超过redis的最大连接数。Redis最大连接数配置参数是 maxclients , 默认 10000
   * 资源开销
     例如虽然希望控制空闲连接, 但是不希望因为连接池的频繁释放创建连接造成不必要开销。

   **举个例子:**

   * 一次命令时间 (borrow|return resource + Jedis执行命令(含网络) ) 的平均耗时约为1ms, 一个连接的QPS大约是1000
   * 业务期望的QPS是50000

   那么理论上需要的资源池大小是 50000 / 1000 = 50个。但事实上这是个理论值, 还要考虑到要比理论值预留一些资源, 通常来讲maxTotal可以比理论值大一些。

   但这个值不是越大越好, 一方面连接太多占用客户端和服务端资源, 另一方面对于Redis这种高QPS的服务器, 一个大命令的阻塞即使设置再大资源池仍然会无济于事。

2. maxIdle minIdle

   maxIdle实际上才是业务需要的最大连接数, maxTotal是为了给出余量, 所以maxIdle不要设置过小, 否则会有new Jedis(新连接)开销, 而minIdle是为了控制空闲资源监测。

   连接池的最佳性能是 ==maxTotal = maxIdle==, 这样就避免连接池伸缩带来的性能干扰。但是如果并发量不大或者maxTotal设置过高, 会导致不必要的连接资源浪费。
   可以根据实际总OPS和调用redis客户端的规模整体评估每个节点所使用的连接池。