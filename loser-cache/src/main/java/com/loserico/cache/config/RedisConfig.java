package com.loserico.cache.config;

/**
 * redis.properties中各种可配置项
 * <p>
 * Copyright: (C), 2021-11-10 9:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class RedisConfig {
	
	/**
	 * 是否启用默认配置, 默认Redis为localhost:6379
	 */
	public static final String DEFAULT_ENABLED = "redis.default.enabled";
	
	/**
	 * 如果是哨兵模式, 这里填哨兵ip:port
	 */
	public static final String SENTINELS = "redis.sentinels";
	
	/**
	 * 哨兵模式下master名字(默认 mymaster), 这个要跟Redis服务器配置的一致
	 */
	public static final String SENTINEL_MASTER_NAME = "redis.maserName";
	
	/**
	 * Redis host, 默认localhost
	 */
	public static final String REDIS_HOST = "redis.host";
	
	/**
	 * Redis port, 默认6379
	 */
	public static final String REDIS_PORT = "redis.port";
	
	/**
	 * Redis 密码, 默认没有密码
	 */
	public static final String REDIS_PASSWORD = "redis.password";
	
	/**
	 * Redis默认数据, 默认0
	 */
	public static final String REDIS_DB = "redis.db";
	
	/**
	 * 默认50000 (50秒)连接超时
	 */
	public static final String CONNECTION_TIMEOUT = "redis.connectionTimeout";
	
	/**
	 * 执行redis命令超时时间, 默认1000 (1秒) <br/>
	 * 在高并发, 大数据量情况下, Redis执行命令可能会超时, 可以调大一点这个参数
	 */
	public static final String SOCKER_TIMEOUT = "redis.socketTimeout";
	
	/**
	 * 是否开启debug模式, 会打印读取到的配置信息, 方便线上排查, 默认false
	 */
	public static final String REDIS_DEBUG = "redis.debug";
	
	/**
	 * Redis连接池里面保留的最少连接数, 默认8
	 */
	public static final String REDIS_POOL_MIN_IDLE = "redis.minIdle";
	
	/**
	 * 启动时是否要预热Redis? 即提前在连接池里面建立好一些连接, 默认 true <p/>
	 * 由于一些原因(例如超时时间设置较小原因), 有的项目在启动成功后会出现超时 <p/>
	 * 第一次使用时, 池子没有资源使用, 会new Jedis, 使用后放到池子里, 可能会有一定的时间开销, <p/>
	 * 所以也可以考虑在JedisPool定义后, 为JedisPool提前进行预热, 例如以最小空闲数量为预热数量. <p/>
	 */
	public static final String REDIS_WARM_UP = "redis.warmUp";
}
