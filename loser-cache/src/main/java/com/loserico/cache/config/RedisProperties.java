package com.loserico.cache.config;

import lombok.Data;

/**
 * Jedis Pool 配置属性<p>
 * 这个配置文件里面已经对Redis做了最优配置以简化开发的配置量
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
	 * 既表示连接超时, 又表示读写超时, 单位毫秒
	 * 从Jedis 2.8开始有构造函数可以区分connectionTimeout和soTimeout了
	 */
	private int timeout = 5000;
	
	/**
	 * 连接超时时间
	 */
	private int connectionTimeout = 50000;
	
	/**
	 * socket timeout时间, 就是一次命令的执行不能超过这个时间
	 */
	private int socketTimeout = 1000;
	
	//---------------- 资源池大小、空闲设置 Begin ----------------
	/**
	 * 资源池中最大连接数<p>
	 * 实际上这个是一个很难回答的问题, 考虑的因素比较多
	 * <ul>
	 *     <li/>业务希望Redis并发量
	 *     <li/>客户端执行命令时间
	 *     <li/>Redis资源<p>
	 *          例如 nodes(例如应用个数) * maxTotal 是不能超过redis的最大连接数。Redis最大连接数配置参数是 maxclients , 默认 10000
	 *     <li/>资源开销<p>
	 *          例如虽然希望控制空闲连接, 但是不希望因为连接池的频繁释放创建连接造成不必要开销。
	 * </ul>
	 * 
	 * 举个例子
	 * <ul>
	 *     <li/>一次命令时间 (borrow|return resource + Jedis执行命令(含网络) ) 的平均耗时约为1ms, 一个连接的QPS大约是1000
	 *     <li/>业务期望的QPS是50000
	 * </ul>
	 * 那么理论上需要的资源池大小是 50000 / 1000 = 50个
	 */
	private int maxTotal = 50;
	
	/**
	 * 资源池允许最大空闲的连接数<p>
	 * 建议设置成跟maxTotal一样大<p>
	 * maxIdle实际上才是业务需要的最大连接数, maxTotal是为了给出余量, 所以maxIdle不要设置过小, 
	 * 否则会有new Jedis(新连接)开销, 而minIdle是为了控制空闲资源监测。
	 * <p>
	 * 连接池的最佳性能是设置 maxTotal = maxIdle, 这样就避免连接池伸缩带来的性能干扰。<p>
	 * 但是如果并发量不大或者maxTotal设置过高, 会导致不必要的连接资源浪费。<p>
	 * 可以根据实际总OPS和调用redis客户端的规模整体评估每个节点所使用的连接池。
	 */
	private int maxIdle = 50;
	
	/**
	 * 最小空闲数
	 */
	private int minIdle = 8;
	//---------------- 资源池大小、空闲设置 End ------------------
	
	
	//---------------- 空闲资源监测 Begin ----------------
	/**
	 * 是否开启空闲资源监测
	 */
	private boolean testWhileIdle = true;
	
	/**
	 * 空闲资源的检测周期(单位为毫秒)<p>
	 * 表示一个对象至少停留在idle状态的最短时间, 然后才能被idle object evitor扫描并驱逐; <p>
	 * 或者说表示idle object evitor两次扫描之间要sleep的毫秒数
	 */
	private int timeBetweenEvictionRunsMillis = 30000;
	
	/**
	 * 资源池中资源最小空闲时间(单位为毫秒), 达到此值后空闲资源将被移除
	 */
	private int minEvictableIdleTimeMillis = 60000;
	
	/**
	 * 做空闲资源检测时, 每次的采样数<p>
	 * 如果设置为-1, 就是对所有连接做空闲监测
	 */
	private int numTestsPerEvictionRun = -1;
	
	//---------------- 空闲资源监测 End ----------------
	
	
	
	//---------------- 资源用尽后处理方式 Begin --------------
	
	/**
	 * 当资源池用尽后, 调用者是否要等待。有当为true时, 下面的maxWaitMillis才会生效
	 */
	private boolean blockWhenExhausted = true;
	
	/**
	 * 当资源池连接用尽后, 调用者的最大等待时间(单位为毫秒)
	 * -1 表示永不超时(不建议使用默认值)
	 */
	private int maxWaitMillis = -1;
	
	//---------------- 资源用尽后处理方式 End ----------------
	
	
	
	/**
	 * 向资源池借用连接时是否做连接有效性检测(ping), 无效连接会被移除
	 * 业务量很大时候建议设置为false(多一次ping的开销)
	 */
	private boolean testOnBorrow = true;
	
	/**
	 * 向资源池归还连接时是否做连接有效性检测(ping), 无效连接会被移除
	 * 业务量很大时候建议设置为false(多一次ping的开销)
	 */
	private boolean testOnReturn = false;
	
	
	/**
	 * 是否开启jmx监控, 可用于监控
	 * 建议开启, 但应用本身也要开启
	 */
	private boolean jmxEnabled = true;

	
}
