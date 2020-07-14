package com.loserico.cache.config;

import lombok.Data;

/**
 * Jedis Pool 配置属性
 * <p>
 * Copyright: Copyright (c) 2019-10-17 13:47
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class RedisProperties {

	/**
	 * IP
	 */
	private String host;

	/**
	 * 端口号
	 */
	private int port;

	/**
	 * 数据库ID
	 */
	private int database;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 建立连接的超时时间, 如果超过这个时间还连不上Redis就抛异常
	 */
	private int timeout = 5000;

	/**
	 * 资源池中最大连接数
	 */
	private int maxTotal = 8;

	/**
	 * 资源池允许最大空闲的连接数
	 */
	private int maxIdle = 8;

	private int minIdle = 8;

	/**
	 * 向资源池借用连接时是否做连接有效性检测(ping), 无效连接会被移除
	 */
	private boolean testOnBorrow = false;

	/**
	 * 向资源池归还连接时是否做连接有效性检测(ping), 无效连接会被移除
	 */
	private boolean testOnReturn = false;

	/**
	 * 是否开启jmx监控，可用于监控
	 */
	private boolean jmxEnabled = true;

	/**
	 * 当资源池用尽后，调用者是否要等待。有当为true时，下面的maxWaitMillis才会生效
	 */
	private boolean blockWhenExhausted = true;

	/**
	 * 当资源池连接用尽后，调用者的最大等待时间(单位为毫秒)
	 */
	private int maxWaitMillis = -1;

	/**
	 * 是否开启空闲资源监测
	 */
	private boolean testWhileIdle = false;

	/**
	 * 空闲资源的检测周期(单位为毫秒)
	 */
	private int timeBetweenEvictionRunsMillis = -1;

	/**
	 * 资源池中资源最小空闲时间(单位为毫秒), 达到此值后空闲资源将被移除
	 */
	private int minEvictableIdleTimeMillis = 1800000;

	/**
	 * 做空闲资源检测时，每次的采样数
	 */
	private int numTestsPerEvictionRun = 3;

}
