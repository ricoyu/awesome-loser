package com.loserico.cache;


import com.fasterxml.jackson.databind.JavaType;
import com.loserico.cache.collection.QueueListener;
import com.loserico.cache.concurrent.BlockingLock;
import com.loserico.cache.concurrent.Lock;
import com.loserico.cache.concurrent.NonBlockingLock;
import com.loserico.cache.exception.JedisValueOperationException;
import com.loserico.cache.factory.JedisOperationFactory;
import com.loserico.cache.listeners.MessageListener;
import com.loserico.cache.operations.JedisClusterOperations;
import com.loserico.cache.operations.JedisOperations;
import com.loserico.cache.status.HSet;
import com.loserico.cache.status.TTL;
import com.loserico.cache.utils.KeyUtils;
import com.loserico.cache.utils.UnMarshaller;
import com.loserico.common.lang.concurrent.LoserThreadExecutor;
import com.loserico.common.lang.utils.IOUtils;
import com.loserico.common.lang.utils.PrimitiveUtils;
import com.loserico.json.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.loserico.cache.status.HSet.INSERTED;
import static com.loserico.cache.status.HSet.UPDATED;
import static com.loserico.cache.utils.ByteUtils.toBytes;
import static com.loserico.cache.utils.KeyUtils.joinKey;
import static com.loserico.cache.utils.StringUtils.requireNonEmpty;
import static com.loserico.cache.utils.UnMarshaller.toList;
import static com.loserico.cache.utils.UnMarshaller.toLong;
import static com.loserico.cache.utils.UnMarshaller.toObject;
import static com.loserico.cache.utils.UnMarshaller.toSeconds;
import static com.loserico.json.jackson.JacksonUtils.toJson;
import static java.lang.String.join;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.*;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Jedis 的工具类, key/value 支持任意类型
 * <p>
 * 通过lua脚本实现了一些原生Redis不具备的接口, 如带过期时间的setnx(key, value, expires, timeUnit)
 *
 * <p>
 * Copyright: Copyright (c) 2018-05-12 18:16
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class JedisUtils {
	
	public static final String STATUS_SUCCESS = "OK";
	
	/**
	 * 用户缓存lua脚本的sha
	 */
	private static final ConcurrentHashMap<String, String> shaHashs = new ConcurrentHashMap<>();
	
	private static JedisOperations jedisOperations = JedisOperationFactory.create();
	
	private static final LoserThreadExecutor EXECUTOR = new LoserThreadExecutor(Runtime.getRuntime().availableProcessors() + 1,
			500,
			10, MINUTES);
	
	/**
	 * key/value 都是字符串的版本
	 *
	 * @param key
	 * @param value
	 * @return String
	 */
	public static boolean set(String key, String value) {
		return set(toBytes(key), toBytes(value));
	}
	
	/**
	 * value不是String类型的情况
	 * <p>
	 * 如果value实现了Serializable接口, 那么用Java的序列化机制
	 * 否则使用Jackson序列化成byte[]
	 *
	 * @param key
	 * @param value
	 * @return boolean 是否set成功
	 */
	public static boolean set(String key, Object value) {
		return set(toBytes(key), toBytes(value));
	}
	
	/**
	 * value是List类型, 通过Jackson序列化成json串
	 *
	 * @param key
	 * @param values
	 * @return boolean 是否set成功
	 */
	public static boolean set(String key, List<?> values) {
		return set(key, toJson(values));
	}
	
	/**
	 * key不是String类型的情况
	 * <p>
	 * 如果key实现了Serializable接口, 那么用Java的序列化机制
	 * 否则使用Jackson序列化成byte[]
	 *
	 * @param key
	 * @param value
	 * @return boolean 是否set成功
	 * @on
	 */
	public static boolean set(Object key, String value) {
		return set(toBytes(key), toBytes(value));
	}
	
	/**
	 * key/value都不是String类型的情况, 如果key/value实现了Serializable接口, 那么用Java的序列化机制,
	 * 否则使用Jackson序列化成byte[]
	 *
	 * @param key
	 * @param value
	 * @return boolean 是否set成功
	 */
	public static boolean set(Object key, Object value) {
		return set(toBytes(key), toBytes(value));
	}
	
	/**
	 * key/value 都是byte[]情况
	 *
	 * @param key
	 * @param value
	 * @return true表示设置成功
	 */
	public static boolean set(byte[] key, byte[] value) {
		return STATUS_SUCCESS.equals(jedisOperations.set(key, value));
	}
	
	/**
	 * key/value 都是字符串版本, 同时带过期时间
	 *
	 * @param key
	 * @param value
	 * @return boolean 表示是否设置成功
	 */
	public static boolean set(String key, String value, long expires, TimeUnit timeUnit) {
		Objects.requireNonNull(timeUnit);
		return set(toBytes(key), toBytes(value), toBytes(expires, timeUnit));
	}
	
	/**
	 * value不是String类型的情况, 如果value实现了Serializable接口, 那么用Java的序列化机制,
	 * 否则使用Jackson序列化成byte[], 同时带过期时间
	 *
	 * @param key
	 * @param value
	 * @return String
	 */
	public static boolean set(String key, Object value, long expires, TimeUnit timeUnit) {
		Objects.requireNonNull(timeUnit);
		return set(toBytes(key), toBytes(value), toBytes(expires, timeUnit));
	}
	
	/**
	 * key不是String类型的情况, 如果key实现了Serializable接口, 那么用Java的序列化机制,
	 * 否则使用Jackson序列化成byte[], 同时设置过期时间
	 *
	 * @param key
	 * @param value
	 * @return boolean 表示是否设置成功
	 */
	public static boolean set(Object key, String value, long expires, TimeUnit timeUnit) {
		Objects.requireNonNull(key);
		return set(toBytes(key), toBytes(value), toBytes(expires, timeUnit));
	}
	
	/**
	 * key/value都不是String类型的情况, 如果key/value实现了Serializable接口, 那么用Java的序列化机制,
	 * 否则使用Jackson序列化成byte[]
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(Object key, Object value, long expires, TimeUnit timeUnit) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(timeUnit);
		return set(toBytes(key), toBytes(value), toBytes(expires, timeUnit));
	}
	
	/**
	 * key/value 都是byte[]情况, 同时设置过期时间
	 *
	 * @param key
	 * @param value
	 * @return true 表示设置成功
	 */
	public static boolean set(byte[] key, byte[] value, byte[] expires) {
		String sampleKey = "setExpire.lua";
		String setExpireSha1 = shaHashs.computeIfAbsent(sampleKey, (x) -> {
			log.debug("Load script {}", sampleKey);
			if (jedisOperations instanceof JedisClusterOperations) {
				jedisOperations.scriptLoad(IOUtils.readClassPathFileAsBytes("/lua-scripts/setExpire.lua"), key);
			}
			return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/setExpire.lua"));
		});
		long result = (long) jedisOperations.evalsha(toBytes(setExpireSha1), 1, key, value, expires);
		return result == 1;
	}
	
	/**
	 * Integer, Long等数字类型的cas
	 * <ul>
	 *     <li/>如果原来key不存在, 直接set值
	 *     <li/>如果mode<0, value要比原来的的值小才会set
	 *     <li/>如果mode>0, value要比原来的值大才会set
	 * </ul>
	 * @param key
	 * @param value
	 * @param mode
	 * @return boolean
	 */
	public static boolean casNumber(String key, Object value, int mode) {
		return casNumber(toBytes(key), toBytes(value), toBytes(mode));
	}
	
	public static boolean casNumber(byte[] key, byte[] value, byte[] mode) {
		String sampleKey = "cas.lua";
		String setExpireSha1 = shaHashs.computeIfAbsent(sampleKey, (x) -> {
			log.debug("Load script {}", sampleKey);
			if (jedisOperations instanceof JedisClusterOperations) {
				jedisOperations.scriptLoad(IOUtils.readClassPathFileAsBytes("/lua-scripts/cas.lua"), key);
			}
			return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/cas.lua"));
		});
		long result = (long) jedisOperations.evalsha(toBytes(setExpireSha1), 1, key, value, mode);
		return result == 1;
	}
	
	/**
	 * 如果 key 不存在则设置 key=value
	 * 原子操作
	 *
	 * @param key
	 * @param value
	 * @return boolean    是否设置成功
	 */
	public static boolean setnx(String key, String value) {
		return 1L == jedisOperations.setnx(toBytes(key), toBytes(value));
	}
	
	/**
	 * 如果 key 不存在则设置 key=value
	 * 原子操作
	 *
	 * @param key
	 * @param value
	 * @return boolean    是否设置成功
	 */
	public static boolean setnx(String key, Object value) {
		Objects.requireNonNull(key);
		return 1L == jedisOperations.setnx(toBytes(key), toBytes(value));
	}
	
	/**
	 * 如果 key 不存在则设置 key=value
	 * 原子操作
	 *
	 * @param key
	 * @param value
	 * @return boolean    是否设置成功
	 */
	public static boolean setnx(Object key, Object value) {
		Objects.requireNonNull(key);
		return 1L == jedisOperations.setnx(toBytes(key), toBytes(value));
	}
	
	/**
	 * 如果 key 不存在则设置 key=value, 同时设置过期时间
	 * 原子操作
	 *
	 * @param key
	 * @param value
	 * @param expires  过期时间
	 * @param timeUnit 过期单位, 毫秒、秒等
	 * @return boolean    是否设置成功
	 */
	public static boolean setnx(String key, Object value, long expires, TimeUnit timeUnit) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(timeUnit);
		
		String sampleKey = "setnx.lua";
		String setnxSha1 = shaHashs.computeIfAbsent("setnx.lua", x -> {
			log.debug("Load script {}", "setnx.lua");
			if (jedisOperations instanceof JedisClusterOperations) {
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/setnx.lua"), key);
			}
			return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/setnx.lua"));
		});
		
		long expireInSeconds = timeUnit.toSeconds(expires);
		long result = (long) jedisOperations.evalsha(toBytes(setnxSha1),
				1,
				toBytes(key),
				toBytes(value),
				toBytes(expireInSeconds));
		
		return result == 1;
	}
	
	/**
	 * key/value都是字符串
	 *
	 * @param key
	 * @return String
	 */
	public static String get(String key) {
		return get(toBytes(key));
	}
	
	/**
	 * value不是字符串的情况, 使用Jackson反序列化
	 *
	 * @param key
	 * @param clazz
	 * @return T
	 */
	public static <T> T get(String key, Class<T> clazz) {
		return get(toBytes(key), clazz);
	}
	
	/**
	 * 根据key从缓存中取, 如果取不到对应的value则调用supplier并回填, 回填后默认5分钟过期
	 *
	 * @param key
	 * @param clazz
	 * @param supplier
	 * @return T
	 */
	public static <T> T get(String key, Class<T> clazz, Supplier<T> supplier) {
		return get(key, clazz, supplier, 5, MINUTES);
	}
	
	/**
	 * 根据key从缓存中取, 如果取不到对应的value则调用supplier并回填, 同时制定key的过期时间
	 *
	 * @param key
	 * @param clazz
	 * @param supplier
	 * @return T
	 */
	public static <T> T get(String key, Class<T> clazz, Supplier<T> supplier, long expires, TimeUnit timeUnit) {
		T object = get(key, clazz);
		// 没有命中则调用supplier.get()并回填缓存
		if (object == null) {
			// 成功获取锁才调supplier并回填
			String requestId = UUID.randomUUID().toString();
			boolean locked = lock(key, requestId, expires, timeUnit);
			if (locked) {
				T result = supplier.get();
				set(key, result, expires, timeUnit);
				unlock(key, requestId);
				return result;
			} else {// 没有获得锁就再从缓存取一遍, 取不到拉倒
				return get(key, clazz);
			}
		}
		
		return object;
	}
	
	/**
	 * key不是String类型的情况, 如果key实现了Serializable接口, 那么用Java的序列化机制,
	 * 否则使用Jackson序列化成byte[]
	 *
	 * @param key
	 * @return String
	 */
	public static String get(Object key) {
		return get(toBytes(key));
	}
	
	/**
	 * key/value 都不是字符串的情况
	 * 如果key/value 实现了Serializable接口, 那么用Java的序列化机制, 否则使用Jackson反序列化
	 *
	 * @param key
	 * @param clazz
	 * @return T
	 * @on
	 */
	public static <T> T get(Object key, Class<T> clazz) {
		return get(toBytes(key), clazz);
	}
	
	public static String get(byte[] key) {
		byte[] value = jedisOperations.get(key);
		if (value != null && value.length > 0) {
			return new String(value, UTF_8);
		}
		return null;
	}
	
	public static <T> T get(byte[] key, Class<T> clazz) {
		byte[] value = jedisOperations.get(key);
		return toObject(value, clazz);
	}
	
	/**
	 * 获取Long类型的值, 如果这个key不存在这返回null
	 *
	 * @param key
	 * @return Long
	 */
	public static Long getLong(String key) {
		byte[] value = jedisOperations.get(toBytes(key));
		if (value != null && value.length > 0) {
			return toLong(value);
		}
		return null;
	}
	
	/**
	 * key是字符串, value是一个ArrayList, 通过Jackson序列化反序列化
	 *
	 * @param key
	 * @param clazz
	 * @return List<T>
	 */
	public static <T> List<T> getList(String key, Class<T> clazz) {
		return getList(toBytes(key), clazz);
	}
	
	public static <T> List<T> getList(byte[] key, Class<T> clazz) {
		byte[] value = jedisOperations.get(key);
		return toList(value, clazz);
	}
	
	/**
	 * 根据key从缓存中取, 如果取不到对应的value则调用supplier并回填, 默认5分钟过期
	 *
	 * @param key
	 * @param clazz
	 * @param supplier
	 * @return T
	 */
	public static <T> List<T> getList(String key, Class<T> clazz, Supplier<List<T>> supplier) {
		List<T> object = getList(key, clazz);
		// 没有命中则调用supplier.get()并回填缓存
		if (object == null || object.isEmpty()) {
			// 成功获取锁才调supplier并回填
			String requestId = UUID.randomUUID().toString();
			boolean locked = lock(key, requestId, 1, MINUTES);
			if (locked) {
				try {
					List<T> result = supplier.get();
					set(key, result, 5, MINUTES);
					return result;
				} finally {
					unlock(key, requestId);
				}
			} else {// 没有获得锁就再从缓存取一遍
				return getList(key, clazz);
			}
		}
		
		return object;
	}
	
	/**
	 * 根据key从缓存中取, 如果取不到对应的value则调用supplier并回填, 同时指定key的过期时间
	 *
	 * @param key
	 * @param clazz
	 * @param supplier
	 * @return List<T>
	 */
	public static <T> List<T> getList(String key, Class<T> clazz, Supplier<List<T>> supplier, long expires,
	                                  TimeUnit timeUnit) {
		Objects.requireNonNull(timeUnit);
		
		List<T> list = getList(key, clazz);
		// 没有命中则调用supplier.get()并回填缓存
		if (list == null || list.isEmpty()) {
			/*
			 * 成功获取锁才调supplier并回填
			 * 这里锁的时间没关系, 因为拿到锁后执行set操作后就会解锁
			 */
			String requestId = UUID.randomUUID().toString();
			boolean locked = lock(key, requestId, expires, timeUnit);
			if (locked) {
				try {
					List<T> result = supplier.get();
					if (isNotEmpty(result)) {
						set(key, result, expires, timeUnit);
					}
					return result;
				} finally {
					unlock(key, requestId);
				}
			} else {// 没有获得锁就再从缓存取一遍, 取不到就拉倒
				return getList(key, clazz);
			}
		}
		
		return list;
	}
	
	/**
	 * 递增1
	 *
	 * @param key
	 * @return Long
	 */
	public static Long incr(String key) {
		return jedisOperations.incr(key);
	}
	
	/**
	 * 递增1, 并且在首次递增的时候设置过期时间, key有效期内再次执行不会重新更新TTL, 直到key过期后调用则重新设置过期时间
	 *
	 * @param key
	 * @return Long
	 */
	public static Long incr(String key, long expires, TimeUnit timeUnit) {
		String sampleKey = "incrExpire.lua";
		String setnxSha1 = shaHashs.computeIfAbsent(sampleKey, x -> {
			log.debug("Load script {}", sampleKey);
			if (jedisOperations instanceof JedisClusterOperations) {
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/incrExpire.lua"), key);
			}
			return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/incrExpire.lua"));
		});
		
		long expireInSeconds = timeUnit.toSeconds(expires);
		long currentValue = (long) jedisOperations.evalsha(toBytes(setnxSha1),
				1,
				toBytes(key),
				toBytes(expireInSeconds));
		return currentValue;
	}
	
	/**
	 * 一次增加size长度
	 *
	 * @param key
	 * @param size
	 * @return Long
	 */
	public static Long incrBy(String key, long size) {
		return jedisOperations.incrBy(key, size);
	}
	
	/**
	 * <h4>Redis list 相关操作</h4>
	 * <p>
	 * List 是简单的字符串列表, 它按插入顺序排序<p>
	 * <ul>
	 * <li>{@code lpush}  向指定的列表左侧插入元素, 返回插入后列表的长度
	 * <li>{@code rpush}  向指定的列表右侧插入元素, 返回插入后列表的长度
	 * <li>{@code llen}   返回指定列表的长度
	 * <li>{@code lrange} 返回指定列表中指定范围的元素值
	 * </ul>
	 * <p>
	 * Copyright: Copyright (c) 2018-08-01 19:08
	 * <p>
	 * Company: DataSense
	 * <p>
	 *
	 * @author Rico Yu	ricoyu520@gmail.com
	 * @version 1.0
	 * @on
	 */
	public static final class LIST {
		
		/**
		 * lpush 向指定的列表左侧(头部)插入元素, 返回插入后列表的长度
		 *
		 * @param key
		 * @param values
		 * @return long
		 */
		public static long lpush(String key, String... values) {
			return jedisOperations.lpush(key, values);
		}
		
		/**
		 * lpush 向指定的列表左侧(头部)插入元素, 返回插入后列表的长度
		 *
		 * @param key
		 * @param values
		 * @return long
		 */
		public static long lpush(String key, Object... values) {
			return jedisOperations.lpush(toBytes(key), toBytes(values));
		}
		
		/**
		 * 往list里面lpush元素, 现在list最多limit个元素, 超过的话就不插入
		 *
		 * @param key
		 * @param limit
		 * @param values
		 * @return long list当前的长度
		 */
		public static long lpushLimit(String key, int limit, Object... values) {
			String hashSha = shaHashs.computeIfAbsent("lpush.lua", x -> {
				log.debug("Load script {}", "lpush.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/lpush.lua"), key);
				} else {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/lpush.lua"));
				}
			});
			
			byte[][] objects = new byte[values.length + 2][];
			objects[0] = toBytes(key);
			objects[1] = toBytes(limit);
			for (int i = 0; i < values.length; i++) {
				objects[i + 2] = toBytes(values[i]);
			}
			return (Long) jedisOperations.evalsha(
					toBytes(hashSha),
					1,
					objects);
		}
		
		/**
		 * rpush 向指定的列表右侧插入元素, 返回插入后列表的长度
		 *
		 * @param key
		 * @param values
		 * @return long
		 */
		public static long rpush(String key, String... values) {
			return jedisOperations.rpush(key, values);
		}
		
		/**
		 * rpush 向指定的列表右侧插入元素, 返回插入后列表的长度
		 *
		 * @param key
		 * @param values
		 * @return long
		 */
		public static long rpush(String key, Object... values) {
			return jedisOperations.rpush(toBytes(key), toBytes(values));
		}
		
		/**
		 * 从左侧(头部)弹出一个元素
		 *
		 * @param key
		 * @return String
		 */
		public static String lpop(String key) {
			return jedisOperations.lpop(key);
		}
		
		/**
		 * 从左侧(头部)弹出一个元素
		 *
		 * @param key
		 * @return T
		 */
		public static <T> T lpop(String key, Class<T> clazz) {
			byte[] data = jedisOperations.lpop(toBytes(key));
			return toObject(data, clazz);
		}
		
		/**
		 * 从左侧(头部)弹出一个元素,阻塞版本
		 * <pre>
		 * BLPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里是 list2; 第二个元素表示返回的元素本身 a
		 * </pre>
		 * <p>
		 * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this commands as blocking
		 * versions of LPOP and RPOP able to block if the specified keys don't exist or contain empty
		 * lists.
		 * <p>
		 * The following is a description of the exact semantic. We describe BLPOP but the two commands
		 * are identical, the only difference is that BLPOP pops the element from the left (head) of the
		 * list, and BRPOP pops from the right (tail).
		 * <p>
		 * <b>Non blocking behavior</b>
		 * <p>
		 * When BLPOP is called, if at least one of the specified keys contain a non empty list, an
		 * element is popped from the head of the list and returned to the caller together with the name
		 * of the key (BLPOP returns a two elements array, the first element is the key, the second the
		 * popped value).
		 * <p>
		 * Keys are scanned from left to right, so for instance if you issue BLPOP list1 list2 list3 0
		 * against a dataset where list1 does not exist but list2 and list3 contain non empty lists, BLPOP
		 * guarantees to return an element from the list stored at list2 (since it is the first non empty
		 * list starting from the left).
		 * <p>
		 * <b>Blocking behavior</b>
		 * <p>
		 * If none of the specified keys exist or contain non empty lists, BLPOP blocks until some other
		 * client performs a LPUSH or an RPUSH operation against one of the lists.
		 * <p>
		 * Once new data is present on one of the lists, the client finally returns with the name of the
		 * key unblocking it and the popped value.
		 * <p>
		 * When blocking, if a non-zero timeout is specified, the client will unblock returning a nil
		 * special value if the specified amount of seconds passed without a push operation against at
		 * least one of the specified keys.
		 * <p>
		 * The timeout argument is interpreted as an integer value. A timeout of zero means instead to
		 * block forever.
		 * <p>
		 * <b>Multiple clients blocking for the same keys</b>
		 * <p>
		 * Multiple clients can block for the same key. They are put into a queue, so the first to be
		 * served will be the one that started to wait earlier, in a first-blpopping first-served fashion.
		 * <p>
		 * <b>blocking POP inside a MULTI/EXEC transaction</b>
		 * <p>
		 * BLPOP and BRPOP can be used with pipelining (sending multiple commands and reading the replies
		 * in batch), but it does not make sense to use BLPOP or BRPOP inside a MULTI/EXEC block (a Redis
		 * transaction).
		 * <p>
		 * The behavior of BLPOP inside MULTI/EXEC when the list is empty is to return a multi-bulk nil
		 * reply, exactly what happens when the timeout is reached. If you like science fiction, think at
		 * it like if inside MULTI/EXEC the time will flow at infinite speed :)
		 * <p>
		 * Time complexity: O(1)
		 *
		 * @param key
		 * @param timeoutSeconds 阻塞多少秒后超时退出
		 * @return BLPOP returns a two-elements array via a multi bulk reply in order to return both the
		 * unblocking key and the popped value.
		 * <p>
		 * When a non-zero timeout is specified, and the BLPOP operation timed out, the return
		 * value is a nil multi bulk reply. Most client values will return false or nil
		 * accordingly to the programming language used.
		 * @on
		 * @see #brpop(int, String...)
		 */
		public static List<String> blpop(String key, int timeoutSeconds) {
			return jedisOperations.blpop(timeoutSeconds, key);
		}
		
		/**
		 * 从左侧(头部)弹出一个元素,阻塞版本
		 * <pre>
		 * BLPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里shi list2
		 * 第二个元素表示返回的元素本身 a
		 *
		 * </pre>
		 *
		 * @param key
		 * @return
		 */
		public static String blpop(String key) {
			List<String> list = jedisOperations.blpop(0, key);
			if (isEmpty(list)) {
				return null;
			}
			return list.get(0);
		}
		
		/**
		 * 从左侧(头部)弹出一个元素
		 *
		 * @param key
		 * @return T
		 */
		public static <T> T blpop(String key, Class<T> clazz) {
			byte[][] keys = new byte[1][];
			keys[0] = toBytes(key);
			List<byte[]> elements = jedisOperations.blpop(0, keys);
			return toObject(elements.get(0), clazz);
		}
		
		/**
		 * 从左侧(头部)弹出一个元素, 阻塞版本
		 * <pre>
		 * BLPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里是 list2
		 * 第二个元素表示返回的元素本身 a
		 *
		 * 有元素出队后queueListener会被调用
		 * </pre>
		 *
		 * @param key
		 * @return
		 * @on
		 */
		public static void blpop(String key, QueueListener listener) {
			List<String> results = jedisOperations.blpop(0, key);
			if (isNotEmpty(results)) {
				listener.onDeque(results.get(0), results.get(1));
			}
		}
		
		/**
		 * 从右侧(尾部)弹出一个元素, 阻塞版本
		 * <pre>
		 * BRPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里是 list2; 第二个元素表示返回的元素本身 a
		 * </pre>
		 * <p>
		 * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this commands as blocking
		 * versions of LPOP and RPOP able to block if the specified keys don't exist or contain empty
		 * lists.
		 * <p>
		 * The following is a description of the exact semantic. We describe BLPOP but the two commands
		 * are identical, the only difference is that BLPOP pops the element from the left (head) of the
		 * list, and BRPOP pops from the right (tail).
		 * <p>
		 * <b>Non blocking behavior</b>
		 * <p>
		 * When BLPOP is called, if at least one of the specified keys contain a non empty list, an
		 * element is popped from the head of the list and returned to the caller together with the name
		 * of the key (BLPOP returns a two elements array, the first element is the key, the second the
		 * popped value).
		 * <p>
		 * Keys are scanned from left to right, so for instance if you issue BLPOP list1 list2 list3 0
		 * against a dataset where list1 does not exist but list2 and list3 contain non empty lists, BLPOP
		 * guarantees to return an element from the list stored at list2 (since it is the first non empty
		 * list starting from the left).
		 * <p>
		 * <b>Blocking behavior</b>
		 * <p>
		 * If none of the specified keys exist or contain non empty lists, BLPOP blocks until some other
		 * client performs a LPUSH or an RPUSH operation against one of the lists.
		 * <p>
		 * Once new data is present on one of the lists, the client finally returns with the name of the
		 * key unblocking it and the popped value.
		 * <p>
		 * When blocking, if a non-zero timeout is specified, the client will unblock returning a nil
		 * special value if the specified amount of seconds passed without a push operation against at
		 * least one of the specified keys.
		 * <p>
		 * The timeout argument is interpreted as an integer value. A timeout of zero means instead to
		 * block forever.
		 * <p>
		 * <b>Multiple clients blocking for the same keys</b>
		 * <p>
		 * Multiple clients can block for the same key. They are put into a queue, so the first to be
		 * served will be the one that started to wait earlier, in a first-blpopping first-served fashion.
		 * <p>
		 * <b>blocking POP inside a MULTI/EXEC transaction</b>
		 * <p>
		 * BLPOP and BRPOP can be used with pipelining (sending multiple commands and reading the replies
		 * in batch), but it does not make sense to use BLPOP or BRPOP inside a MULTI/EXEC block (a Redis
		 * transaction).
		 * <p>
		 * The behavior of BLPOP inside MULTI/EXEC when the list is empty is to return a multi-bulk nil
		 * reply, exactly what happens when the timeout is reached. If you like science fiction, think at
		 * it like if inside MULTI/EXEC the time will flow at infinite speed :)
		 * <p>
		 * Time complexity: O(1)
		 *
		 * @param timeout
		 * @param keys
		 * @return BLPOP returns a two-elements array via a multi bulk reply in order to return both the
		 * unblocking key and the popped value.
		 * <p>
		 * When a non-zero timeout is specified, and the BLPOP operation timed out, the return
		 * value is a nil multi bulk reply. Most client values will return false or nil
		 * accordingly to the programming language used.
		 */
		public static List<String> brpop(int timeout, String... keys) {
			return jedisOperations.brpop(timeout, keys);
		}
		
		/**
		 * 从右侧(尾部)弹出一个元素,阻塞版本
		 * <pre>
		 * BRPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里是 list2; 第二个元素表示返回的元素本身 a
		 * </pre>
		 *
		 * @on
		 */
		public static String brpop(int timeout, String key) {
			List<String> list = jedisOperations.brpop(timeout, key);
			if (isNotEmpty(list)) {
				return list.get(1);
			}
			return null;
		}
		
		/**
		 * 从右侧(尾部)弹出一个元素,阻塞版本
		 * <pre>
		 * BRPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里是 list2; 第二个元素表示返回的元素本身 a
		 * </pre>
		 *
		 * @on
		 */
		public static String brpop(String key) {
			return brpop(0, key);
		}
		
		/**
		 * 从右侧(尾部)弹出一个元素,阻塞版本
		 * <pre>
		 * BRPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里是 list2; 第二个元素表示返回的元素本身 a
		 * </pre>
		 *
		 * @on
		 */
		public static <T> T brpop(int timeout, String key, Class<T> clazz) {
			List<byte[]> list = jedisOperations.brpop(timeout, toBytes(key));
			if (isNotEmpty(list)) {
				byte[] data = list.get(1);
				return toObject(data, clazz);
			}
			return null;
		}
		
		/**
		 * 从右侧(尾部)弹出一个元素,阻塞版本
		 * <pre>
		 * BRPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里是 list2; 第二个元素表示返回的元素本身 a
		 * </pre>
		 */
		public static <T> T brpop(String key, Class<T> clazz) {
			return brpop(0, key, clazz);
		}
		
		/**
		 * 从右侧(尾部部)弹出一个元素,阻塞版本
		 * <pre>
		 * BLPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里shi list2
		 * 第二个元素表示返回的元素本身 a
		 *
		 * 有元素出队后queueListener会被调用
		 * </pre>
		 *
		 * @param key
		 * @param listener
		 * @return
		 */
		public static void brpop(String key, QueueListener listener) {
			List<String> results = jedisOperations.brpop(0, key);
			if (isNotEmpty(results)) {
				listener.onDeque(results.get(0), results.get(1));
			}
		}
		
		/**
		 * 持续从从右侧(尾部)弹出一个元素,阻塞版本, listener如果抛异常不会往上抛, 只是记录一次啊异常信息
		 * <pre>
		 * BLPOP list1 list2 list3
		 * 假设 list1不存在,  list2有一个元素 a
		 * 那么返回的List包含两个元素,  第一个表示从哪个key返回的, 这里shi list2
		 * 第二个元素表示返回的元素本身 a
		 *
		 * 有元素出队后queueListener会被调用
		 * </pre>
		 *
		 * @param key
		 * @param listener
		 * @return
		 */
		public static void keepBrpop(String key, QueueListener listener) {
			EXECUTOR.execute(() -> {
				try {
					while (true) {
						List<String> results = jedisOperations.brpop(0, key);
						if (isNotEmpty(results)) {
							listener.onDeque(results.get(0), results.get(1));
						}
					}
				} catch (Throwable e) {
					log.error("", e);
				}
			});
		}
		
		public static String rpop(String key) {
			return jedisOperations.rpop(key);
		}
		
		public static <T> T rpop(String key, Class<T> clazz) {
			String value = jedisOperations.rpop(key);
			if (isBlank(value)) {
				return null;
			}
			
			return JacksonUtils.toObject(value, clazz);
		}
		
		/**
		 * llen 返回指定列表的长度
		 *
		 * @param key
		 * @return
		 */
		public static long llen(String key) {
			return jedisOperations.llen(key);
		}
		
		/**
		 * lrange 返回指定列表中指定范围的元素值
		 * index从0开始,  -1表示最后一个元素
		 *
		 * @param key
		 * @param start
		 * @param end
		 * @return List<String>
		 * @on
		 */
		public static List<String> lrange(String key, long start, long end) {
			return jedisOperations.lrange(key, start, end);
		}
		
		/**
		 * lrange 返回指定列表中指定范围的元素值
		 * index从0开始,  -1表示最后一个元素
		 *
		 * @param key
		 * @param start
		 * @param end
		 * @return List<String>
		 * @on
		 */
		public static <T> List<T> lrange(String key, long start, long end, Class<T> clazz) {
			List<byte[]> list = jedisOperations.lrange(toBytes(key), start, end);
			if (isEmpty(list)) {
				return new ArrayList<>();
			}
			
			List<T> result = new ArrayList<>();
			for (byte[] bytes : list) {
				result.add(toObject(bytes, clazz));
			}
			return result;
		}
		
		/**
		 * 获取整个List里面的元素
		 *
		 * @param key
		 * @return List<String>
		 */
		public static List<String> list(String key) {
			return jedisOperations.lrange(key, 0, -1);
		}
		
		/**
		 * 获取整个List里面的元素
		 *
		 * @param key
		 * @return List<T>
		 */
		public static <T> List<T> list(String key, Class<T> clazz) {
			List<byte[]> list = jedisOperations.lrange(toBytes(key), 0, -1);
			if (isEmpty(list)) {
				return new ArrayList<>();
			}
			
			List<T> result = new ArrayList<>();
			for (byte[] bytes : list) {
				result.add(toObject(bytes, clazz));
			}
			return result;
		}
		
		/**
		 * 从列表中删除指定个数的元素
		 * Removes the first count occurrences of elements equal to value from the list stored at key.
		 * The count argument influences the operation in the following ways:<p>
		 * <ul>
		 * <li/><code>count > 0</code>: Remove elements equal to value moving from head to tail. 从左边开始删
		 * <li/><code>count < 0</code>: Remove elements equal to value moving from tail to head. 从右边开始删
		 * <li/><code>count = 0</code>: Remove all elements equal to value. 删除所有匹配
		 * </ul>
		 * For example, LREM list -2 "hello" will remove the last two occurrences of "hello" in the list stored at list.
		 * Note that non-existing keys are treated like empty lists, so when key does not exist, the command will always return 0.
		 *
		 * @param key
		 * @param count
		 * @param value
		 * @return long 删除的元素个数
		 * @on
		 */
		public static long lrem(String key, long count, String value) {
			return jedisOperations.lrem(key, count, value);
		}
		
	}
	
	/**
	 * Redis Set 相关操作
	 * <p>
	 * Copyright: Copyright (c) 2018-08-01 19:09
	 * <p>
	 * Company: DataSense
	 * <p>
	 *
	 * @author Rico Yu	ricoyu520@gmail.com
	 * @version 1.0
	 * @on
	 */
	public static class SET {
		
		/**
		 * 向Set中添加元素
		 *
		 * @param key
		 * @param element
		 * @return long 本次添加的元素个数
		 */
		public static long sadd(String key, String element) {
			if (isBlank(element)) {
				return 0;
			}
			return jedisOperations.sadd(key, element);
		}
		
		/**
		 * 向Set中添加元素
		 *
		 * @param key
		 * @param elements
		 * @return long 本次添加的元素个数
		 */
		public static long sadd(String key, Object... elements) {
			return jedisOperations.sadd(toBytes(key), toBytes(elements));
		}
		
		/**
		 * 获取Set所有元素
		 *
		 * @param key
		 * @return Set<String>
		 */
		public static Set<String> smembers(String key) {
			return jedisOperations.smembers(key);
		}
		
		/**
		 * 获取Set所有元素
		 *
		 * @param key
		 * @return Set<String>
		 */
		public static <T> Set<T> smembers(String key, Class<T> clazz) {
			Set<byte[]> byteSet = jedisOperations.smembers(toBytes(key));
			if (isEmpty(byteSet)) {
				return Collections.emptySet();
			}
			Set<T> resultSet = new HashSet<>();
			for (byte[] data : byteSet) {
				resultSet.add(toObject(data, clazz));
			}
			return resultSet;
		}
		
		/**
		 * 检查 value 是否在 Set中
		 *
		 * @param key
		 * @param element
		 * @return boolean
		 */
		public static boolean sismember(String key, Object element) {
			return jedisOperations.sismember(toBytes(key), toBytes(element));
		}
		
		/**
		 * 返回Set中元素个数
		 *
		 * @param key
		 * @return long
		 */
		public static long scard(String key) {
			return jedisOperations.scard(key);
		}
		
		/**
		 * 从Set中移除元素, 返回实际移除的元素个数
		 *
		 * @param key
		 * @param elements
		 * @return long
		 */
		public static long srem(String key, Object... elements) {
			return jedisOperations.srem(toBytes(key), toBytes(elements));
		}
		
	}
	
	/**
	 * Redis Sorted Set 相关操作
	 * <p>
	 * Copyright: Copyright (c) 2018-07-27 21:28
	 * <p>
	 * Company: DataSense
	 * <p>
	 *
	 * @author Rico Yu	ricoyu520@gmail.com
	 * @version 1.0
	 */
	public static final class ZSET {
		
		/**
		 * 获取member的score
		 *
		 * @param key
		 * @param member
		 * @return double
		 */
		public static double zscore(String key, String member) {
			return jedisOperations.zscore(key, member);
		}
		
		public static Long zadd(String key, double score, String member) {
			return jedisOperations.zadd(key, score, member);
		}
		
		public static Long zadd(String key, double score, Object member) {
			return jedisOperations.zadd(key, score, member);
		}
		
		public static Long zadd(byte[] key, double score, byte[] member) {
			return jedisOperations.zadd(key, score, member);
		}
		
		/**
		 * 返回zset的size
		 *
		 * @param key
		 * @return Long
		 */
		public static Long size(String key) {
			return jedisOperations.zcard(key);
		}
		
		/**
		 * 返回zset的size
		 *
		 * @param key
		 * @return Long
		 */
		public static Long zcard(String key) {
			return jedisOperations.zcard(key);
		}
		
		/**
		 * 移除有序集合中给定的排名区间的所有成员
		 * 按元素的索引下标
		 *
		 * @param key
		 * @param start 开始, 第一个是0
		 * @param end   结束
		 * @return Long 删除的个数
		 */
		public static Long zremRangeByRank(String key, long start, long end) {
			return jedisOperations.zremByRank(key, start, end);
		}
		
		/**
		 * 同zremRangeByRank, 根据元素的index下标删除, index从0开始
		 *
		 * @param key
		 * @param start
		 * @param end
		 * @return Long 删除的个数
		 */
		public static Long remoteByIndex(String key, long start, long end) {
			return jedisOperations.zremByRank(key, start, end);
		}
		
		/**
		 * 移除有序集key中, 所有score值介于min和max之间(包括等于min或max)的成员
		 *
		 * @param key
		 * @param min
		 * @param max
		 * @return
		 */
		public static Long zremRangeByScore(String key, String min, String max) {
			return jedisOperations.zremRangeByScore(key, min, max);
		}
		
		public static Set<String> zrange(String key, long start, long end) {
			return jedisOperations.zrange(key, start, end);
		}
		
		public static <T> Set<T> zrange(String key, long start, long end, Class<T> clazz) {
			return jedisOperations.zrange(key, start, end)
					.stream()
					.map(json -> JacksonUtils.toObject(json, clazz))
					.collect(toSet());
			
		}
		
		/**
		 * 默认min, max都是包含的, 可以通过 (1 5 设置不包含最小值,
		 * 如:
		 * ZRANGEBYSCORE zset (1 5    返回 1 < score <= 5
		 * ZRANGEBYSCORE zset (5 (10  返回 5 < score < 10
		 *
		 * @param key
		 * @param min score最小值, 包含, -inf 表示负无穷大
		 * @param max score最大值, 包含, +inf 表示正无穷大
		 * @return Set<String>
		 */
		public static Set<String> zrangeByScore(String key, String min, String max) {
			return jedisOperations.zrangeByScore(key, min, max);
		}
		
		/**
		 * 默认min, max都是包含的, 可以通过 (1 5 设置不包含最小值,
		 * 如:
		 * ZRANGEBYSCORE zset (1 5    返回 1 < score <= 5
		 * ZRANGEBYSCORE zset (5 (10  返回 5 < score < 10
		 *
		 * @param key
		 * @param min score最小值, 包含, -inf 表示负无穷大
		 * @param max score最大值, 包含, +inf 表示正无穷大
		 * @return Set<T>
		 */
		public static <T> Set<T> zrangeByScore(String key, String min, String max, Class<T> clazz) {
			return jedisOperations.zrangeByScore(key, min, max)
					.stream()
					.map(json -> JacksonUtils.toObject(json, clazz))
					.collect(Collectors.toSet());
		}
	}
	
	/**
	 * Redis HASH 相关操作
	 * <p>
	 * Copyright: Copyright (c) 2018-08-07 21:01
	 * <p>
	 * Company: DataSense
	 * <p>
	 *
	 * @author Rico Yu	ricoyu520@gmail.com
	 * @version 1.0
	 * @on
	 */
	public static final class HASH {
		
		// hash每个field的过期时间记录在key为 jedis_utils:__timeout__set:key 的zset中
		private static final String HASH_EXPIRE_ZSET_PREFIX = "jedis_utils:__timeout__set";
		
		/**
		 * key 是Map的名字
		 * field 是Map里面的field
		 * <ul>返回
		 * <li>0 表示更新了map中的field
		 * <li>1 表示在map上新增了一个field
		 *
		 * @param key
		 * @param field
		 * @param value
		 * @return int
		 */
		public static int hset(String key, Object field, Object value) {
			return hset(toBytes(key), toBytes(field), toBytes(value));
		}
		
		/**
		 * key 是Map的名字
		 * field 是Map里面的field
		 * <ul>返回
		 * <li>0 表示更新了map中的field
		 * <li>1 表示在map上新增了一个field
		 *
		 * @param key
		 * @param field
		 * @param value
		 * @return int
		 */
		public static int hset(byte[] key, byte[] field, byte[] value) {
			Long result = (Long) jedisOperations.hset(key, field, value);
			return result.intValue();
		}
		
		/**
		 * 设置Hash某个field值,同时指定其过期时间,单位秒
		 * <ul>返回
		 * <li>UPDATED(0) 表示更新了map中的field
		 * <li>INSERTED(1) 表示在map上新增了一个field
		 * </ul>
		 *
		 * @param key
		 * @param field
		 * @param value
		 * @param ttl   field过期时间,秒
		 * @return HSetStatus
		 */
		public static HSet hset(String key, Object field, Object value, long ttl) {
			return hset(toBytes(key), toBytes(field), toBytes(value), ttl);
		}
		
		/**
		 * 设置Hash某个field值,同时指定其过期时间
		 * <ul>返回
		 * <li>UPDATED(0) 表示更新了map中的field
		 * <li>INSERTED(1) 表示在map上新增了一个field
		 * </ul>
		 *
		 * @param key
		 * @param field
		 * @param value
		 * @param ttl
		 * @param timeUnit
		 * @return
		 */
		public static HSet hset(String key, Object field, Object value, long ttl, TimeUnit timeUnit) {
			return hset(toBytes(key), toBytes(field), toBytes(value), timeUnit.toSeconds(ttl));
		}
		
		/**
		 * 设置Hash某个field值,同时指定其过期时间,单位秒
		 * <ul>返回
		 * <li>UPDATED(0) 表示更新了map中的field
		 * <li>INSERTED(1) 表示在map上新增了一个field
		 * </ul>
		 *
		 * @param key
		 * @param field
		 * @param value
		 * @param ttl   field过期时间,秒
		 * @return HSetStatus
		 */
		public static HSet hset(byte[] key, byte[] field, byte[] value, long ttl) {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"), key);
				} else {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"));
				}
			});
			
			String zsetKey = joinKey(HASH_EXPIRE_ZSET_PREFIX, key);
			Long result = (Long) jedisOperations.evalsha(toBytes(hashSha),
					2,
					key, // hash key
					toBytes(zsetKey), // zset key
					toBytes("hset"), // 调用的lua function名字
					field,
					value,
					toBytes(ttl));
			return result.intValue() == 0 ? UPDATED : INSERTED;
		}
		
		/**
		 * 都是字符串的情况
		 *
		 * @param key
		 * @param hash
		 */
		public static boolean hmset(String key, Map<String, String> hash) {
			String result = jedisOperations.hmset(key, hash);
			return STATUS_SUCCESS.equalsIgnoreCase(result);
		}
		
		/**
		 * 将整个Map添加到Redis中, 返回AtomicLongArray
		 * 第一个元素表示更新的field数量
		 * 第二个元素表示新增的field数量
		 *
		 * @param key
		 * @param map
		 * @return AtomicLongArray
		 */
		public static <K, V> AtomicLongArray hmsetGeneric(String key, Map<K, V> map) {
			AtomicLongArray statistic = new AtomicLongArray(2);
			map.entrySet().forEach((entry) -> {
				Long type = jedisOperations.hset(toBytes(key), toBytes(entry.getKey()), toBytes(entry.getValue()));
				if (type == 0) {
					statistic.incrementAndGet(0);
				} else {
					statistic.incrementAndGet(1);
				}
			});
			return statistic;
		}
		
		/**
		 * field 和 value 都是字符串的情况调这个接口
		 *
		 * @param key
		 * @param field
		 * @return String
		 */
		
		public static String hget(String key, String field) {
			if (key == null || "".equals(key.trim())) {
				return null;
			}
			if (field == null || "".equals(field.trim())) {
				return null;
			}
			
			byte[] data = hget(toBytes(key), toBytes(field));
			return UnMarshaller.toString(data);
		}
		
		/**
		 * field 和 value 都是字符串的情况调这个接口
		 *
		 * @param key
		 * @param field
		 * @return String
		 */
		
		public static byte[] hget(byte[] key, byte[] field) {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				String script = IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(script, key);
				}
				return jedisOperations.scriptLoad(script);
			});
			
			String zsetKey = joinKey(HASH_EXPIRE_ZSET_PREFIX, key);
			byte[] data = (byte[]) jedisOperations.evalsha(toBytes(hashSha),
					2,
					toBytes(key), // hash key
					toBytes(zsetKey), // zset key
					toBytes("hget"), // 调用的lua function名字
					toBytes(field));
			return data;
		}
		
		/**
		 * field是任意对象, 但是value是字符串的情况调这个
		 *
		 * @param key
		 * @param field
		 * @return String
		 */
		public static String hget(String key, Object field) {
			if (field == null) {
				return null;
			}
			return hget(key, field, String.class);
		}
		
		/**
		 * key 是 String
		 * field 是任意类型
		 * javaType 可以通过 TypeUtils 获取
		 *
		 * @param key
		 * @param field
		 * @param javaType
		 * @return T
		 * @on
		 */
		public static <T> T hget(String key, Object field, JavaType javaType) {
			byte[] data = jedisOperations.hget(toBytes(key), toBytes(field));
			try {
				return JacksonUtils.objectMapper().readValue(UnMarshaller.toString(data), javaType);
			} catch (IOException e) {
				log.error("Convert value to Collection type failed", e);
				throw new JedisValueOperationException(e);
			}
		}
		
		/**
		 * field是任意对象, value是Class<T>指定的类型
		 *
		 * @param key
		 * @param field
		 * @param clazz
		 * @return T
		 */
		public static <T> T hget(String key, Object field, Class<T> clazz) {
			if (field == null) {
				return null;
			}
			byte[] data = jedisOperations.hget(toBytes(key), toBytes(field));
			return toObject(data, clazz);
		}
		
		/**
		 * Map的field对应的值是一个List的情况
		 *
		 * @param key
		 * @param field
		 * @param clazz
		 * @return List<T>
		 */
		public static <T> List<T> hgetList(String key, Object field, Class<T> clazz) {
			if (field == null) {
				return null;
			}
			byte[] data = jedisOperations.hget(toBytes(key), toBytes(field));
			return toList(data, clazz);
		}
		
		/**
		 * key/value 都是字符串
		 *
		 * @param key
		 * @return Map<String, String>
		 */
		public static Map<String, String> hgetAll(String key) {
			return jedisOperations.hgetAll(key);
		}
		
		/**
		 * key 是 String
		 * value 是 List<V>
		 * CollectionType 可以通过 TypeUtils.listType(Class<T>) 获取
		 *
		 * @param key
		 * @param javaType
		 * @return Map<String, V>
		 * @on
		 */
		public static <V> Map<String, V> hgetAll(String key, JavaType javaType) {
			Map<byte[], byte[]> map = jedisOperations.hgetAll(toBytes(key));
			return map.entrySet().stream()
					.collect(toMap(
							(entry) -> UnMarshaller.toString(entry.getKey()),
							(entry) -> {
								try {
									return JacksonUtils.objectMapper().readValue(UnMarshaller.toString(entry.getValue()), javaType);
								} catch (IOException e) {
									log.error("Convert value to Collection type failed", e);
									throw new JedisValueOperationException(e);
								}
							}));
		}
		
		/**
		 * key/value 是任意确定的类型, 如value可以都是Long, 但不可以同时Long、LocalDate类型
		 *
		 * @param key
		 * @param clazzKey
		 * @param clazzValue
		 * @return Map<K, V>
		 */
		public static <K, V> Map<K, V> hgetAll(String key, Class<K> clazzKey, Class<V> clazzValue) {
			return hgetAll(toBytes(key), clazzKey, clazzValue);
		}
		
		/**
		 * key/value 是任意确定的类型, 如value可以都是Long, 但不可以同时Long、LocalDate类型
		 *
		 * @param key
		 * @param clazzKey
		 * @param javaType
		 * @return Map<K, List < V>>
		 */
		public static <K, V> Map<K, V> hgetAll(String key, Class<K> clazzKey, JavaType javaType) {
			Map<byte[], byte[]> map = jedisOperations.hgetAll(toBytes(key));
			return map.entrySet().stream()
					.collect(toMap(
							(entry) -> toObject(entry.getKey(), clazzKey),
							(entry) -> {
								try {
									return JacksonUtils.objectMapper().readValue(UnMarshaller.toString(entry.getValue()), javaType);
								} catch (IOException e) {
									log.error("Convert value to Collection type failed", e);
									throw new JedisValueOperationException(e);
								}
							}));
		}
		
		/**
		 * key/value 是任意确定的类型, 如value可以都是Long, 但不可以同时Long、LocalDate类型
		 *
		 * @param key
		 * @param clazzKey
		 * @param clazzValue
		 * @return Map<K, V>
		 */
		public static <K, V> Map<K, V> hgetAll(byte[] key, Class<K> clazzKey, Class<V> clazzValue) {
			Map<byte[], byte[]> map = jedisOperations.hgetAll(key);
			return map.entrySet().stream()
					.collect(toMap(
							(entry) -> toObject(entry.getKey(), clazzKey),
							(entry) -> toObject(entry.getValue(), clazzValue)));
		}
		
		/**
		 * 拿到HASH的所有value
		 *
		 * @param key
		 * @param clazz
		 * @param <T>
		 * @return List<T>
		 */
		public static <T> List<T> hvals(String key, Class<T> clazz) {
			List<String> values = jedisOperations.hvals(key);
			return values.stream()
					.map((value) -> JacksonUtils.toObject(value, clazz))
					.collect(Collectors.toList());
		}
		
		/**
		 * 根据指定的key和要获取的field列表, 找到对应的value, 最后以Map形式返回
		 *
		 * @param key
		 * @param fields
		 * @return Map<String, String>
		 */
		public static Map<String, String> hmget(String key, String... fields) {
			List<String> values = jedisOperations.hmget(key, fields);
			Map<String, String> resultMap = new HashMap<>();
			for (int i = 0; i < fields.length; i++) {
				String field = fields[i];
				resultMap.put(field, values.get(i));
			}
			return resultMap;
		}
		
		/**
		 * 根据指定的key和要获取的field列表, 找到对应的value, 最后以Map形式返回
		 *
		 * @param key
		 * @param fields
		 * @return Map<String, String>
		 */
		public static <K, V> Map<K, V> hmget(String key, List<K> fields, Class<V> clazzValue) {
			List<byte[]> values = jedisOperations.hmget(toBytes(key), toBytes(fields));
			Map<K, V> resultMap = new HashMap<>();
			for (int i = 0; i < fields.size(); i++) {
				K field = fields.get(i);
				resultMap.put(field, toObject(values.get(i), clazzValue));
			}
			return resultMap;
		}
		
		/**
		 * 查看哈希表 key 中, 指定的field字段是否存在。
		 *
		 * @param key
		 * @param field
		 * @return
		 */
		public static boolean hexists(String key, String field) {
			return jedisOperations.hexists(key, field);
		}
		
		/**
		 * 查看哈希表 key 中, 指定的field字段是否存在。
		 *
		 * @param key
		 * @param field
		 * @return
		 */
		public static boolean hexists(String key, Object field) {
			return jedisOperations.hexists(toBytes(key), toBytes(field));
		}
		
		/**
		 * 返回Hash包含的field数量
		 *
		 * @param key
		 * @return int
		 */
		public static long hlen(String key) {
			return jedisOperations.hlen(toBytes(key));
		}
		
		/**
		 * 删除Hash的某个field
		 *
		 * @param key
		 * @param field
		 * @return int 删除的field数量
		 */
		public static Long hdel(String key, Object field) {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"), key);
				}
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"));
			});
			
			String zsetKey = joinKey(HASH_EXPIRE_ZSET_PREFIX, key);
			return (Long) jedisOperations.evalsha(toBytes(hashSha),
					2,
					toBytes(key), // hash key
					toBytes(zsetKey), // zset key
					toBytes("hdel"), // 调用的lua function名字
					toBytes(field));
		}
		
		/**
		 * 删除Hash的某个field, 并返回该字段的值
		 *
		 * @param key
		 * @param field
		 * @return int 删除的field数量
		 */
		public static String hdelGet(String key, Object field) {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"), key);
				}
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"));
			});
			
			String zsetKey = joinKey(HASH_EXPIRE_ZSET_PREFIX, key);
			byte[] data = (byte[]) jedisOperations.evalsha(toBytes(hashSha),
					2,
					toBytes(key), // hash key
					toBytes(zsetKey), // zset key
					toBytes("hdelGet"), // 调用的lua function名字
					toBytes(field));
			return PrimitiveUtils.toString(data);
		}
		
		/**
		 * 返回 hash field 的剩余存活时间(秒)
		 * <ul>
		 * <li>返回KEY_NOT_EXIST(-3) 表示HASH key不存在
		 * <li>返回FIELD_NOT_EXIST(-2) 表示字段不存在
		 * <li>返回NO_EXPIRE(-1) 表示字段存在但是没有过期时间
		 * <li>返回剩余的ttl
		 * </ul>
		 *
		 * @param key   hash的key
		 * @param field hash的field
		 * @return long
		 * @on
		 */
		public static TTL ttl(String key, String field) {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"), key);
				}
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"));
			});
			
			String zsetKey = joinKey(HASH_EXPIRE_ZSET_PREFIX, key);
			Long result = (Long) jedisOperations.evalsha(toBytes(hashSha),
					2,
					toBytes(key),
					toBytes(zsetKey),
					toBytes("ttl"),
					toBytes(field));
			int code = result.intValue();
			switch (code) {
				case -3:
					return TTL.KEY_NOT_EXIST;
				case -2:
					return TTL.FIELD_NOT_EXIST;
				case -1:
					return TTL.NO_EXPIRE;
				default:
					TTL ttlStatus = TTL.TTL;
					ttlStatus.setTtl(code);
					return ttlStatus;
			}
		}
		
		/**
		 * 设置hash field过期时间
		 * <ul>
		 * <li>如果设置成功返回1
		 * <li>如果key或者field不存在则返回0
		 * <ul/>
		 *
		 * @param key
		 * @param field
		 * @param ttl
		 * @return
		 */
		public static int expire(String key, String field, int ttl) {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"), key);
				}
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"));
			});
			
			String zsetKey = joinKey(HASH_EXPIRE_ZSET_PREFIX, key);
			Long result = (Long) jedisOperations.evalsha(toBytes(hashSha),
					2,
					toBytes(key),
					toBytes(zsetKey),
					toBytes("expire"),
					toBytes(field),
					toBytes(ttl));
			return result.intValue();
		}
		
		/**
		 * 移除hash field过期时间
		 * <ul>
		 * <li>返回1表示成功移除过期时间
		 * <li>返回0表示key或field不存在或者没有设置过期时间
		 *
		 * @param key
		 * @param field
		 * @return
		 * @on
		 */
		public static int persist(String key, String field) {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"), key);
				}
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"));
			});
			
			String zsetKey = joinKey(HASH_EXPIRE_ZSET_PREFIX, key);
			Long result = (Long) jedisOperations.evalsha(toBytes(hashSha),
					2,
					toBytes(key),
					toBytes(zsetKey),
					toBytes("persist"),
					toBytes(field));
			return result.intValue();
		}
		
		/**
		 * 拿redis服务器当前时间戳
		 *
		 * @return
		 */
		public static long time() {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"), "hash.lua");
				}
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"));
			});
			
			long milis = (long) jedisOperations.evalsha(toBytes(hashSha), 0, toBytes("time"));
			return milis;
		}
		
		/**
		 * 拿过期的field
		 *
		 * @return
		 */
		public static List<String> expiredFields(String key) {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"), key);
				}
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"));
			});
			
			String zsetKey = joinKey(HASH_EXPIRE_ZSET_PREFIX, key);
			byte[] bytes = (byte[]) jedisOperations.evalsha(toBytes(hashSha),
					1,
					toBytes(zsetKey),
					toBytes("expiredFields"));
			String json = UnMarshaller.toString(bytes);
			return JacksonUtils.toList(json, String.class);
		}
		
		/**
		 * 调试用
		 */
		public static void testPurpose(String key, String field) {
			String hashSha = shaHashs.computeIfAbsent("hash.lua", x -> {
				log.debug("Load script {}", "hash.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"), key);
				}
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/hash.lua"));
			});
			
			String zsetKey = joinKey(HASH_EXPIRE_ZSET_PREFIX, key);
			Object data = (Object) jedisOperations.evalsha(toBytes(hashSha),
					2,
					toBytes(key),
					toBytes(zsetKey),
					toBytes("ttl"),
					toBytes(field));
			if (!PrimitiveUtils.isByteArray(data)) {
				System.out.println(data);
			} else {
				System.out.println(UnMarshaller.toString((byte[]) data));
			}
		}
	}
	
	/**
	 * 地理位置信息查询
	 * <p>
	 * Copyright: Copyright (c) 2020-10-16 17:35
	 * <p>
	 * Company: Sexy Uncle Inc.
	 * <p>
	 *
	 * @author Rico Yu  ricoyu520@gmail.com
	 * @version 1.0
	 */
	public static final class GEO {
		
	}
	
	/**
	 * 一些比较我特色的功能
	 */
	public static final class AFFLUENT {
		
		/**
		 * 对给定时间内(expire 秒数)访问次数不超过 count 次
		 * 返回     true  表示没有超过
		 * false 表示超过
		 *
		 * @param key
		 * @param expire
		 * @param count
		 * @return boolean
		 */
		public static boolean rateLimit(String key, int expire, int count) {
			
			String hashSha = shaHashs.computeIfAbsent("rateLimit.lua", x -> {
				log.debug("Load script {}", "rateLimit.lua");
				if (jedisOperations instanceof JedisClusterOperations) {
					return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/rateLimit.lua"), key);
				}
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/rateLimit.lua"));
			});
			
			long result = (long) jedisOperations.evalsha(toBytes(hashSha),
					1,
					toBytes(join(":", "rate", "limit", key)),
					toBytes(expire), toBytes(count));
			return result == 1;
		}
	}
	
	/**
	 * 指定key是否存在
	 *
	 * @param key
	 * @return
	 */
	public static boolean exists(String key) {
		return jedisOperations.exists(key);
	}
	
	public static boolean exists(Object key) {
		if (Serializable.class.isInstance(key)) {
			return jedisOperations.exists(toBytes(key));
		}
		
		return jedisOperations.exists(JacksonUtils.toBytes(key));
	}
	
	/**
	 * 设置过期时间, 单位秒
	 *
	 * @param key
	 * @param timeout
	 * @return boolean 是否成功设置了过期时间
	 */
	public static boolean expire(String key, int timeout) {
		return jedisOperations.expire(key, timeout) == 1;
	}
	
	/**
	 * 设置过期时间, 单位秒
	 *
	 * @param key
	 * @param timeout
	 * @return boolean 是否成功设置了过期时间
	 */
	public static boolean expire(Object key, int timeout) {
		return jedisOperations.expire(toBytes(key), timeout) == 1;
	}
	
	/**
	 * 设置过期时间及单位
	 *
	 * @param key
	 * @param timeout
	 * @return boolean 是否成功设置了过期时间
	 */
	public static boolean expire(String key, int timeout, TimeUnit timeUnit) {
		return jedisOperations.expire(key, toSeconds(timeout, timeUnit)) == 1;
	}
	
	/**
	 * 设置过期时间及单位
	 *
	 * @param key
	 * @param timeout
	 * @return boolean 是否成功设置了过期时间
	 */
	public static boolean expire(Object key, int timeout, TimeUnit timeUnit) {
		return jedisOperations.expire(toBytes(key), toSeconds(timeout, timeUnit)) == 1;
	}
	
	/**
	 * 设置过期时间及单位
	 *
	 * @param key
	 * @param unixTime UNIX时间戳
	 * @return boolean 是否成功设置了过期时间
	 */
	public static boolean expireAt(String key, long unixTime) {
		return jedisOperations.expireAt(key, unixTime) == 1;
	}
	
	/**
	 * 设置过期时间及单位
	 *
	 * @param key
	 * @param unixTime UNIX时间戳
	 * @return boolean 是否成功设置了过期时间
	 */
	public static boolean expireAt(Object key, long unixTime) {
		return jedisOperations.expireAt(toBytes(key), unixTime) == 1;
	}
	
	/**
	 * 清除key的过期时间
	 *
	 * @param key
	 * @return
	 */
	public static boolean persist(String key) {
		return jedisOperations.persist(key) == 1;
	}
	
	/**
	 * 清除key的过期时间
	 *
	 * @param key
	 * @return
	 */
	public static boolean persist(Object key) {
		return jedisOperations.persist(toBytes(key)) == 1;
	}
	
	/**
	 * 返回key的过期时间, 单位秒。 如果key没有设置过期时间, 返回 -1 如果key不存在, 返回 -2
	 *
	 * @param key
	 * @return long 返回key的过期时间, 单位秒, -1 表示没有过期时间 -2 表示可以不存在
	 */
	public static long ttl(String key) {
		return jedisOperations.ttl(key);
	}
	
	/**
	 * 返回key的过期时间, 单位秒。 如果key没有设置过期时间, 返回 -1 如果key不存在, 返回 -2
	 *
	 * @param key
	 * @return long 返回key的过期时间, 单位秒, -1 表示没有过期时间 -2 表示可以不存在
	 */
	public static long ttl(Object key) {
		return jedisOperations.ttl(toBytes(key));
	}
	
	public static void del(String key) {
		jedisOperations.del(key);
	}
	
	public static void del(Object key) {
		jedisOperations.del(toBytes(key));
	}
	
	/**
	 * 删除并返回key对应的value
	 *
	 * @param key
	 * @return String
	 */
	public static String delGet(String key) {
		Objects.requireNonNull(key);
		byte[] data = delGet(toBytes(key));
		return UnMarshaller.toString(data);
	}
	
	/**
	 * 删除并返回key对应的value
	 *
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return T
	 */
	public static <T> T delGet(String key, Class<T> clazz) {
		Objects.requireNonNull(key);
		byte[] data = delGet(toBytes(key));
		return toObject(data, clazz);
	}
	
	/**
	 * 删除并返回key对应的value
	 *
	 * @param key
	 * @return String
	 */
	public static String delGet(Object key) {
		Objects.requireNonNull(key);
		byte[] data = delGet(toBytes(key));
		return UnMarshaller.toString(data);
	}
	
	/**
	 * 删除并返回key对应的value
	 *
	 * @param key
	 * @return String
	 */
	public static <T> T delGet(Object key, Class<T> clazz) {
		Objects.requireNonNull(key);
		byte[] data = delGet(toBytes(key));
		return toObject(data, clazz);
	}
	
	/**
	 * 删除并返回key对应的value
	 *
	 * @param key
	 * @return String
	 */
	public static byte[] delGet(byte[] key) {
		Objects.requireNonNull(key);
		
		String delGetSha1 = shaHashs.computeIfAbsent("delGet.lua", x -> {
			log.debug("Load script {}", "delGet.lua");
			if (jedisOperations instanceof JedisClusterOperations) {
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/delGet.lua"), key);
			}
			return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/delGet.lua"));
		});
		
		byte[] value = (byte[]) jedisOperations.evalsha(toBytes(delGetSha1),
				1,
				key);
		
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T eval(String script) {
		return (T) jedisOperations.eval(script);
	}
	
	/**
	 * keyCount是1则params中第一个是key, 余下的是value
	 *
	 * @param script
	 * @param keyCount
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T eval(String script, int keyCount, String... params) {
		return (T) jedisOperations.eval(script, keyCount, params);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T evalsha(String sha) {
		return (T) jedisOperations.evalsha(sha);
	}
	
	public static <T> T evalsha(byte[] sha, int keyCount, byte[]... params) {
		return (T) jedisOperations.evalsha(sha, keyCount, params);
	}
	
	public static <T> T evalsha(String sha, int keyCount, Object... params) {
		return (T) jedisOperations.evalsha(toBytes(sha), keyCount, toBytes(params));
	}
	
	/**
	 * 发布消息, 返回接收到消息的订阅者数量
	 *
	 * @param channel
	 * @param message
	 * @return long
	 */
	public static long publish(String channel, Object message) {
		return publish(toBytes(channel), toBytes(message));
	}
	
	/**
	 * 发布消息, 返回接收到消息的订阅者数量
	 *
	 * @param channel
	 * @param message
	 * @return long
	 */
	public static long publish(String channel, String message) {
		return publish(toBytes(channel), toBytes(message));
	}
	
	/**
	 * 发布消息, 返回接收到消息的订阅者数量
	 *
	 * @param channel
	 * @param message
	 * @return long
	 */
	public static long publish(byte[] channel, byte[] message) {
		return jedisOperations.publish(channel, message);
	}
	
	/**
	 * 异步方式订阅频道, 收到消息后回调 messageListener
	 *
	 * @param chnannels
	 * @param messageListener
	 * @return JedisPubSub 用于取消订阅
	 */
	public static JedisPubSub subscribe(MessageListener messageListener, String... chnannels) {
		JedisPubSub jedisPubSub = new JedisPubSub() {
			
			@Override
			public void onMessage(String channel, String message) {
				messageListener.onMessage(channel, message);
			}
		};
		jedisOperations.subscribe(jedisPubSub, chnannels);
		return jedisPubSub;
	}
	
	/**
	 * 异步方式订阅频道, 收到消息后回调 messageListener
	 * Subscribes the client to the given patterns.
	 * <p>
	 * Supported glob-style patterns:
	 * <p>
	 * h?llo subscribes to hello, hallo and hxllo
	 * h*llo subscribes to hllo and heeeello
	 * h[ae]llo subscribes to hello and hallo, but not hillo
	 *
	 * @param chnannelPatterns
	 * @param messageListener
	 * @return JedisPubSub 用于取消订阅
	 */
	public static JedisPubSub psubscribe(MessageListener messageListener, String... chnannelPatterns) {
		JedisPubSub jedisPubSub = new JedisPubSub() {
			@Override
			public void onPMessage(String pattern, String channel, String message) {
				messageListener.onMessage(channel, message);
			}
		};
		jedisOperations.psubscribe(jedisPubSub, chnannelPatterns);
		return jedisPubSub;
	}
	
	/**
	 * 取消订阅
	 *
	 * @param jedisPubSub
	 * @param channel
	 */
	public static void unsubscribe(JedisPubSub jedisPubSub, String channel) {
		jedisPubSub.unsubscribe(channel);
	}
	
	/**
	 * <pre>
	 * <b>非公平锁</b></p>
	 * Redis 下获取分布式锁, 不会等待锁, 拿不到直接返回
	 * @param key
	 * @return Lock
	 */
	public static boolean lock(String key, String requestId) {
		KeyUtils.requireNonBlank(key);
		requireNonEmpty(requestId);
		return setnx(key, requestId);
	}
	
	/**
	 * <pre>
	 * <b>非公平锁</b></p>
	 * Redis 下获取分布式锁
	 * 不会等待锁, 拿不到直接返回
	 *
	 * 为了确保分布式锁可用, 我们至少要确保锁的实现同时满足以下四个条件：
	 *
	 * 1 互斥性			在任意时刻, 只有一个客户端能持有锁。
	 * 2 不会发生死锁		即使有一个客户端在持有锁的期间崩溃而没有主动解锁, 也能保证后续其他客户端能加锁。
	 * 3 具有容错性		只要大部分的Redis节点正常运行, 客户端就可以加锁和解锁。
	 * 4 解铃还须系铃人		加锁和解锁必须是同一个客户端, 客户端自己不能把别人加的锁给解了。
	 *
	 * 第一个为key		我们使用key来当锁, 因为key是唯一的。
	 * 第二个为value		我们传的是requestId, 很多童鞋可能不明白, 有key作为锁不就够了吗, 为什么还要用到value？原因就是我们在上面讲到可靠性时, 分布式锁要满足第四个条件解铃还须系铃人,
	 * 			通过给value赋值为requestId, 我们就知道这把锁是哪个请求加的了, 在解锁的时候就可以有依据。requestId可以使用UUID.randomUUID().toString()方法生成。
	 * 第三个为nxxx		这个参数我们填的是NX, 意思是SET IF NOT EXIST, 即当key不存在时, 我们进行set操作；若key已经存在, 则不做任何操作；
	 * 第四个为expx		这个参数我们传的是PX, 意思是我们要给这个key加一个过期的设置, 具体时间由第五个参数决定。
	 * 第五个为time		与第四个参数相呼应, 代表key的过期时间。
	 *
	 * Safety and Liveness guarantees
	 *
	 * We are going to model our design with just three properties that, from our point of view,
	 * are the minimum guarantees needed to use distributed locks in an effective way.
	 *
	 * Safety property: 	Mutual exclusion. At any given moment, only one client can hold a lock.
	 * Liveness property A: Deadlock free.
	 * 						Eventually it is always possible to acquire a lock, even if the client that locked a resource crashes or gets partitioned.
	 * Liveness property B: Fault tolerance.
	 * 						As long as the majority of Redis nodes are up, clients are able to acquire and release locks.
	 *
	 * 但是如果再考虑下面的场景：
	 * 	- 如果Redis是Master/Slave模式
	 * 	- 客户端A对资源A加锁, 加锁成功后Master突然挂掉但此时Master还未来得及同步刚刚加的锁到Salve
	 * 	- Slave晋升为Master
	 * 	- 客户端B对资源A加锁成功, 但实际上客户端A已经加锁成功了
	 *
	 * Superficially this works well, but there is a problem: this is a single point of failure in our architecture.
	 * What happens if the Redis master goes down? Well, let’s add a slave! And use it if the master is unavailable.
	 * This is unfortunately not viable. By doing so we can’t implement our safety property of mutual exclusion, because Redis replication is asynchronous.
	 *
	 * There is an obvious race condition with this model:
	 *
	 * Client A acquires the lock in the master.
	 * The master crashes before the write to the key is transmitted to the slave.
	 * The slave gets promoted to master.
	 * Client B acquires the lock to the same resource A already holds a lock for. SAFETY VIOLATION!
	 *
	 * Sometimes it is perfectly fine that under special circumstances, like during a failure, multiple clients can hold the lock at the same time.
	 * If this is the case, you can use your replication based solution. </pre>
	 *
	 * @param key       锁的名字
	 * @param leaseTime key过期时间, 单位秒
	 * @return String token 返回null表示没有获取到锁,  返回一个token表示成功获取锁, 解锁的时候用这个token来解锁
	 * @on
	 */
	public static boolean lock(String key, String requestId, long leaseTime, TimeUnit timeUnit) {
		return setnx(key, requestId, leaseTime, timeUnit);
	}
	
	/**
	 * 阻塞非公平锁
	 *
	 * @param key
	 * @return
	 */
	public static Lock blockingLock(String key) {
		return new BlockingLock(key);
	}
	
	/**
	 * 非阻塞非公平锁
	 *
	 * @param key
	 * @return
	 */
	public static Lock nonBlockingLock(String key) {
		return new NonBlockingLock(key);
	}
	
	/**
	 * 释放分布式锁
	 * <p>
	 * token 是成功加锁后返回的, 这里回传是为了确定解锁的是自己加的锁, 否则会把别人加的锁给解锁了。
	 * <p>
	 * Lua 脚本:
	 * if redis.call("get",KEYS[1]) == ARGV[1] then
	 * return redis.call("del",KEYS[1])
	 * else
	 * return 0
	 * end
	 * <p>
	 * 上述Lua脚本告诉Redis: remove the key only if it exists and the value stored at the key is exactly the one I expect to be.
	 * <p>
	 * This is important in order to avoid removing a lock that was created by another client.
	 * For example a client may acquire the lock, get blocked in some operation for longer than the lock validity time (the time at which the key will expire),
	 * and later remove the lock, that was already acquired by some other client.
	 * <p>
	 * Using just DEL is not safe as a client may remove the lock of another client.
	 * With the above script instead every lock is “signed” with a random string,
	 * so the lock will be removed only if it is still the one that was set by the client trying to remove it.
	 *
	 * @param key   锁
	 * @param value 用于解锁的
	 * @return boolean 是否释放成功
	 */
	public static boolean unlock(String key, String value) {
		String setnxSha1 = shaHashs.computeIfAbsent("unlock.lua", x -> {
			log.debug("Load script {}", "unlock.lua");
			if (jedisOperations instanceof JedisClusterOperations) {
				return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/unlock.lua"), key);
			}
			return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString("/lua-scripts/unlock.lua"));
		});
		
		long result = (long) jedisOperations.evalsha(setnxSha1, 1, key, value);
		return result == 1L;
	}
	
	/**
	 * lua脚本加载到Redis
	 *
	 * @param luaPath
	 * @return lua脚本加载到Redis之后得到的SHA1值
	 */
	public static String scriptLoad(String luaPath) {
		log.debug("Load script {}", luaPath);
		if (jedisOperations instanceof JedisClusterOperations) {
			return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString(luaPath), luaPath);
		}
		return jedisOperations.scriptLoad(IOUtils.readClassPathFileAsString(luaPath));
	}
	
	public static <R> R execute(Function<Jedis, R> func) {
		try (Jedis jedis = jedisOperations.jedis()) {
			return func.apply(jedis);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException("", e);
		}
	}
	
	/**
	 * 在pipeline中执行多条命令, 一次返回所有结果
	 *
	 * @param consumer
	 * @return
	 */
	public static <T> List<T> pipeline(Consumer<Pipeline> consumer) {
		return (List<T>) jedisOperations.executePipelined(consumer);
	}
	
	private static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
	
	private static boolean isNotEmpty(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}
}
