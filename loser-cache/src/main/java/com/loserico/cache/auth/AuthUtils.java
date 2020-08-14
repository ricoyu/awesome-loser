package com.loserico.cache.auth;

import com.loserico.cache.JedisUtils;
import com.loserico.common.lang.utils.StringUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.json.resource.PropertyReader;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.loserico.cache.JedisUtils.subscribe;
import static com.loserico.json.jackson.JacksonUtils.toJson;

/**
 * 基于Redis的登录/认证系统
 * 执行登录, 登出, token过期清理与过期token通知
 * 还有单点登录下线通知
 * <p>
 * Copyright: Copyright (c) 2018-07-27 16:53
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class AuthUtils {
	
	/**
	 * 默认读取classpath下redis.properties文件
	 */
	private static final PropertyReader propertyReader = new PropertyReader("redis");
	
	/**
	 * 用户缓存lua脚本的sha
	 */
	private static final ConcurrentHashMap<String, String> shaHashs = new ConcurrentHashMap<>();
	
	/**
	 * 根据用户名获取token
	 */
	public static final String AUTH_USERNAME_TOKEN_HASH = "auth:username:token";
	
	/**
	 * 根据token获取用户名
	 */
	public static final String AUTH_TOKEN_USERNAME_HASH = "auth:token:username";
	
	/**
	 * 根据token获取userdetails, Spring Security的UserDetails对象
	 */
	public static final String AUTH_TOKEN_USERDETAILS_HASH = "auth:token:userdetails";
	
	/**
	 * 根据token获取authorities, Spring Security的GrantedAuthority对象
	 */
	public static final String AUTH_TOKEN_AUTHORITIES_HASH = "auth:token:authorities";
	
	/**
	 * 根据token获取loginInfo, 这是调用login时传入的额外信息, 如设备ID, IP地址等等
	 */
	public static final String AUTH_TOKEN_LOGIN_INFO_HASH = "auth:token:login:info";
	
	/**
	 * 一个zset, value是token, score是token的过期时间
	 */
	public static final String AUTH_TOKEN_TTL_ZSET = "auth:token:ttl:zset";
	
	/**
	 * token过期后, 会publish一条消息到这个channel, 消息体是JSON格式的Map
	 * key是过期的token, value是LoginInfo对象
	 * 格式: {token:loginInfo, token:loginInfo, ...}
	 */
	public static final String AUTH_TOKEN_EXPIRE_CHANNEL = "auth:token:expired";
	
	/**
	 * 是否自动刷新token
	 * <p>
	 * 如果token快要过期了, 此时被刷新, 则表示过期时间被重置了.
	 */
	private static boolean autoRefresh = propertyReader.getBoolean("redis.auth.auto-refresh", true);
	
	private static final String sha1 = JedisUtils.scriptLoad("/lua-scripts/spring-security-auth.lua");
	
	/**
	 * 执行登录操作, 返回登录成功与否, 如果同一账号已经在别处登录, 先对其执行登出, 并将之前的登录信息返回
	 *
	 * @param username    用户名
	 * @param token       token
	 * @param expires     过期时间
	 * @param timeUnit    单位
	 * @param userdetails Spring Security的UserDetails对象
	 * @param authorities Spring Security的GrantedAuthority对象
	 * @param additionalInfo   额外的登录信息
	 * @return LoginResult<T>
	 */
	public static <T> LoginResult<T> login(String username,
	                                       String token,
	                                       long expires, TimeUnit timeUnit,
	                                       Object userdetails,
	                                       List<?> authorities,
	                                       T additionalInfo) {
		Objects.requireNonNull(timeUnit);
		
		byte[] result = JedisUtils.evalsha(sha1,
				0,
				"login",
				username,
				token,
				(expires == -1 ? expires : timeUnit.toMillis(expires)),
				toJson(userdetails),
				toJson(authorities),
				additionalInfo);
		String resultJson = StringUtils.toString(result);
		T lastLoginInfo = JsonPathUtils.readNode(resultJson, "$.lastLoginInfo");
		boolean success = JsonPathUtils.readNode(resultJson, "$.success");
		LoginResult<T> loginResult = new LoginResult<>(success, lastLoginInfo);
		return (LoginResult<T>) loginResult;
	}
	
	/**
	 * 执行登出操作 返回true表示登出成功 返回false表示该token不存在或者已经登出
	 *
	 * @param token
	 * @return boolean
	 */
	public static boolean logout(String token) {
		byte[] result = JedisUtils.evalsha(sha1,
				0,
				"logout",
				token);
		String str = StringUtils.toString(result);
		return Boolean.parseBoolean(str);
	}
	
	/**
	 * 清除过期的token
	 * <p>
	 * 没有token过期 返回emptyMap
	 * token过期, 返回map的key是token, value是LoginInfo
	 *
	 * @return
	 * @on
	 */
	public static <T> Map<String, T> clearExpired() {
		log.info("开始清理过期token");
		byte[] result = JedisUtils.evalsha(sha1, 0, "clearExpired");
		String resultJson = StringUtils.toString(result);
		log.info("清理结果, 过期Token有: {}", resultJson);
		return resultJson == null ? Collections.emptyMap() : JacksonUtils.toMap(resultJson);
	}
	
	/**
	 * <pre>
	 * 检查token是否存在并且未过期
	 * 如果通过, 返回token对应的用户名
	 * 否则返回null
	 * </pre>
	 *
	 * @param token
	 * @on
	 */
	public static String auth(String token) {
		Objects.requireNonNull(token, "token cannot be null");
		
		byte[] result = JedisUtils.evalsha(sha1,
				0,
				"auth",
				token,
				autoRefresh);
		return StringUtils.toString(result);
	}
	
	/**
	 * 返回0表示这个username没有登录
	 * 返回-1表示usernameTtl检查的时候发现这个用户登录已经过期, 同时会清理其登录信息
	 * 返回这个username对应的token剩余多少秒过期
	 *
	 * @param username
	 * @return
	 * @on
	 */
	public static Long usernameTtl(String username) {
		Objects.requireNonNull(username, "username cannot be null");
		
		byte[] bytes = JedisUtils.evalsha(sha1,
				0,
				"usernameTtl",
				username);
		String s = StringUtils.toString(bytes);
		Long ttlInSeconds = Long.parseLong(s);
		log.info("{} remain in {} seconds to expire", username, ttlInSeconds);
		return ttlInSeconds;
	}
	
	/**
	 * <pre>
	 * 检查指定用户是否已登录并且token未过期
	 * 如果通过, 返回对应的token
	 * 否则返回null
	 * </pre>
	 *
	 * @param username
	 * @on
	 */
	public static String isLogined(String username) {
		byte[] bytes = JedisUtils.evalsha(sha1, 0, "isLogined", username, autoRefresh);
		return StringUtils.toString(bytes);
	}
	
	/**
	 * 根据token获取UserDetails对象
	 *
	 * @param token
	 * @param clazz
	 * @return T
	 */
	public static <T> T userDetails(String token, Class<T> clazz) {
		String userdetails = JedisUtils.HASH.hget(AUTH_TOKEN_USERDETAILS_HASH, token);
		return JacksonUtils.toObject(userdetails, clazz);
	}
	
	/**
	 * 根据token获取登录时提供的authorities
	 *
	 * @param token
	 * @param clazz
	 * @return List<T>
	 */
	public static <T> List<T> authorities(String token, Class<T> clazz) {
		String authorities = JedisUtils.HASH.hget(AUTH_TOKEN_AUTHORITIES_HASH, token);
		return JacksonUtils.toList(authorities, clazz);
	}
	
	/**
	 * 返回登录时提供的loginInfo
	 *
	 * @param token
	 * @param clazz
	 * @return T
	 */
	public static <T> T loginInfo(String token, Class<T> clazz) {
		String loginInfo = JedisUtils.HASH.hget(AUTH_TOKEN_LOGIN_INFO_HASH, token);
		return JacksonUtils.toObject(loginInfo, clazz);
	}
	
	/**
	 * 根据accessToken获取用户名
	 *
	 * @param token
	 * @return
	 */
	public static String username(String token) {
		return JedisUtils.HASH.hget(AUTH_TOKEN_USERNAME_HASH, token);
	}
	
	/**
	 * Token过期后接受通知
	 * <p>
	 * 参数是一个Map<String, T>, T 即调用login()时传入的loginInfo
	 *
	 * @param consumer
	 */
	public static <T> void onTokenExpire(Consumer<Map<String, T>> consumer) {
		subscribe(AuthUtils.AUTH_TOKEN_EXPIRE_CHANNEL, (channel, message) -> {
			log.info("频道{} 收到消息 {}", channel, message);
			Map<String, T> result = JacksonUtils.toMap(message);
			consumer.accept(result);
		});
	}
	
	/**
	 * 定期自动清理token
	 */
	static {
		// 默认1分钟执行一次
		int period = propertyReader.getInt("redis.auth.clear-expired.period", 1);
		// -1表示不执行清理
		if (-1 != period) {
			ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
			scheduledExecutorService.scheduleAtFixedRate(() -> clearExpired(),
					1,
					period,
					TimeUnit.MINUTES);
		}
	}
}