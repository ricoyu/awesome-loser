package com.loserico.cache.auth;

import com.loserico.cache.JedisUtils;
import com.loserico.common.lang.resource.PropertyReader;
import com.loserico.common.lang.utils.StringUtils;
import com.loserico.json.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.nio.charset.StandardCharsets.UTF_8;

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
public final class AuthUtils {

	private static final Logger log = LoggerFactory.getLogger(AuthUtils.class);
	
	/**
	 * 默认读取classpath下redis.properties文件
	 */
	private static final PropertyReader propertyReader = new PropertyReader("redis");
	
	/**
	 * 用户缓存lua脚本的sha
	 */
	private static final ConcurrentHashMap<String, String> shaHashs = new ConcurrentHashMap<>();
	
	/**
	 * 根据token获取用户名的 Redis Key
	 */
	public static final String AUTH_TOKEN_USERNAME_HASH = "auth:token:username";
	
	/**
	 * 根据token获取userdetails Redis Key, Spring Security的UserDetails对象
	 */
	public static final String AUTH_TOKEN_USERDETAILS_HASH = "auth:token:userdetails";
	
	/**
	 * 根据token获取authorities, Spring Security的GrantedAuthority对象
	 */
	public static final String AUTH_TOKEN_AUTHORITIES_HASH = "auth:token:authorities";
	
	/**
	 * 根据token获取loginInfo Redis Key, 这是调用login时传入的额外信息, 如设备ID, IP地址等等
	 */
	public static final String AUTH_TOKEN_LOGIN_INFO_HASH = "auth:token:login:info";
	
	/**
	 * 用户登录时通知的channel
	 */
	public static final String AUTH_LOGIN_CHANNEL = "auth:user:login:channel";
	
	/**
	 * 用户主动退出登录, 这个channel会发通知
	 */
	public static final String AUTH_LOGOUT_CHANNEL = "auth:logout:channel";
	
	/**
	 * token过期后, 会publish一条消息到这个channel, 消息体是username
	 */
	public static final String AUTH_TOKEN_EXPIRE_CHANNEL = "auth:token:expired:channel";
	
	/**
	 * 用户异地登录时踢掉前一个登录时发布的频道, 即单点登录下线通知
	 */
	public static final String AUTH_SINGLE_SIGNON_CHANNEL = "auth:single:signon:channel";
	
	/**
	 * 是否自动刷新token
	 * <p>
	 * 如果token快要过期了, 此时被刷新, 则表示过期时间被重置了.
	 */
	private static boolean autoRefresh = propertyReader.getBoolean("redis.auth.auto-refresh", true);
	
	//private static final String sha1 = JedisUtils.scriptLoad("/lua-scripts/spring-security-auth.lua");
	private static final String sha1 = JedisUtils.scriptLoad("/lua-scripts/spring-security-multi-auth.lua");
	
	/**
	 * 执行登录操作, 返回登录成功与否, 如果同一账号已经在别处登录, 先对其执行登出, 
	 * 然后PUBLIC一条消息到AUTH_SINGLE_SIGNON_CHANNEL, 消息体是
	 * <pre>
	 * {
	 *     "username": "rico",             被踢下线的用户名
	 *     "timestamp": 1622617668773, 
	 *     "token": "ajsdiuqhzmnoqiwueia"  被踢下线的token
	 * }
	 * </pre>
	 * 
	 * 登录成功后再PUBLIC一条消息到AUTH_LOGIN_CHANNEL
	 * <pre>
	 * {
	 *     "username": "rico",           登录的用户名
	 *     "timestamp": 1622617668773, 
	 *     "token": "asdasdasdasddfgsd"  登录的token
	 * }
	 * </pre>
	 * 
	 * @param username       用户名
	 * @param token          token
	 * @param expires        过期时间
	 * @param timeUnit       单位
	 * @param userdetails    Spring Security的UserDetails对象
	 * @param authorities    Spring Security的GrantedAuthority对象
	 * @param additionalInfo 额外的登录信息
	 * @return LoginResult<T>
	 */
	public static <T> boolean login(String username,
	                                       String token,
	                                       long expires, TimeUnit timeUnit,
	                                       Object userdetails,
	                                       List<?> authorities,
	                                       T additionalInfo) {
		return login(username, token, expires, timeUnit, userdetails, authorities, additionalInfo, true);
	}
	
	/**
	 * 执行登录操作, 返回登录成功与否
	 * 如果同一账号已经在别处登录, 并且singleSigin=true, 那么先对其执行登出, 
	 * 然后PUBLIC一条消息到AUTH_SINGLE_SIGNON_CHANNEL, 消息体是
	 * <pre>
	 * {
	 *     "username": "rico", 
	 *     "timestamp": 1622617668773, 
	 *     "token": "ajsdiuqhzmnoqiwueia"
	 * }
	 * </pre>
	 *
	 * @param username       用户名
	 * @param token          token
	 * @param expires        过期时间
	 * @param timeUnit       单位
	 * @param userdetails    Spring Security的UserDetails对象
	 * @param authorities    Spring Security的GrantedAuthority对象
	 * @param additionalInfo 额外的登录信息
	 * @param singleSigin    是否单点登录 -- 指一个用户只能登录一次, 如果在某处已经登录了, 在别处再次登录, 则踢掉前一个登录后重新登录   
	 * @return LoginResult<T>
	 */
	public static <T> boolean login(String username,
	                                       String token,
	                                       long expires, TimeUnit timeUnit,
	                                       Object userdetails,
	                                       List<?> authorities,
	                                       T additionalInfo, 
	                                       boolean singleSigin) {
		Objects.requireNonNull(timeUnit);
		
		Long result = JedisUtils.evalsha(sha1,
				0,
				"login",
				username,
				token,
				(expires == -1 ? expires : timeUnit.toMillis(expires)),
				toJson(userdetails),
				toJson(authorities),
				additionalInfo, 
				singleSigin);
		return toBoolean(result);
	}
	
	/**
	 * 执行登出操作 返回true表示登出成功 返回false表示该token不存在或者已经登出<p>
	 * 登出成功会发布一条消息到AUTH_LOGOUT_CHANNEL
	 * <pre>
	 * {
	 *     "username": "rico",           退出登录的用户名
	 *     "timestamp": 1622617668773, 
	 *     "token": "asdasdasdasddfgsd"  退出登录的token
	 * }
	 * </pre>
	 * 
	 * @param token
	 * @return boolean
	 */
	public static <T> boolean logout(String token) {
		Long result = JedisUtils.evalsha(sha1,
				0,
				"logout",
				token);
		return toBoolean(result);
	}
	
	/**
	 * 清除过期的token, 有token过期返回true, 否则返回false
	 * 如果有token过期, 会发布一条消息到 AUTH_TOKEN_EXPIRE_CHANNEL, 消息体是
	 * <pre>
	 * {
	 *     "username": "rico",             登录过期的用户
	 *     "timestamp": 1622617668773,     登录过期, token被清理时的时间戳
	 *     "token": "ajsdiuqhzmnoqiwueia"  登录过期的token
	 * }
	 * </pre>
	 * 
	 * @return
	 */
	public static boolean clearExpired() {
		log.info("Start cleaning token...");
		Long result = JedisUtils.evalsha(sha1, 0, "clearExpired");
		return toBoolean(result);
	}
	
	/**
	 * <pre>
	 * 检查token是否存在并且未过期
	 * 如果通过, 返回token对应的用户名
	 * 否则返回null
	 * </pre>
	 *
	 * @param token
	 */
	public static String auth(String token) {
		Objects.requireNonNull(token, "token cannot be null");
		
		byte[] bytes = JedisUtils.evalsha(sha1,
				0,
				"auth",
				token,
				autoRefresh);
		
		return toString(bytes);
	}
	
	/**
	 * 检查指定用户是否已登录并且token未过期<p>
	 * @param username
	 */
	public static boolean isLogined(String username) {
		Long result = JedisUtils.evalsha(sha1, 0, "isLogined", username, autoRefresh);
		return toBoolean(result);
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
	 * 获取这个username对应的所有token, 返回的Set有多个元素表示这个用户在多处登录了
	 * @param username
	 * @return Set<String>
	 */
	public static Set<String> tokens(String username) {
		String setKey = StringUtils.join("auth:", username, ":token");
		return JedisUtils.SET.smembers(setKey);
	}
	
	private static String toString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		
		return new String(bytes, UTF_8);
	}
	
	/**
	 * lua脚本返回true/false, Jedis拿到的是1,0
	 * @param result
	 * @return boolean
	 */
	private static boolean toBoolean(Long result) {
		if (result == null) {
			return false;
		}
		
		return result == 1L;
	}
}
