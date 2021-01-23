package com.loserico.cache.operations;

import com.loserico.cache.exception.OperationNotSupportedException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * <p>
 * Copyright: Copyright (c) 2019-10-23 21:10
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface JedisOperations {
	
	public String set(final byte[] key, final byte[] value);
	
	public Long setnx(final byte[] key, final byte[] value);
	
	public byte[] get(final byte[] key);
	
	public Boolean exists(final String key);
	
	public Boolean exists(final byte[] key);
	
	public Long incr(final String key);
	
	public Long incrBy(final String key, final long increment);
	
	/**
	 * Returns the score of member in the sorted set at key.
	 * <p>
	 * If member does not exist in the sorted set, or key does not exist, nil is returned.
	 *
	 * @param key
	 * @param member
	 * @return Double
	 */
	public Double zscore(final String key, final String member);
	
	public Long zadd(String key, double score, String member);
	
	public Long zadd(String key, double score, Object member);
	
	public Long zadd(byte[] key, double score, byte[] member);
	
	public Long zcard(String key);
	
	/**
	 * 移除有序集合中给定的排名区间的所有成员
	 * @param key
	 * @return Long
	 */
	public Long zremByRank(String key, long start, long end);
	
	public Set<String> zrange(String key, long start, long end);
	
	/**
	 * Removes and returns the first element of the list stored at key.
	 *
	 * @param key
	 * @return byte[] the value of the first element, or nil when key does not exist.
	 */
	public byte[] lpop(final byte[] key);
	
	/**
	 * Removes and returns the first element of the list stored at key.
	 *
	 * @param key
	 * @return String the value of the first element, or nil when key does not exist.
	 */
	public String lpop(final String key);
	
	public Long lpush(final String key, final String... strings);
	
	public Long lpush(final byte[] key, final byte[]... strings);
	
	public Long rpush(final String key, final String... strings);
	
	public Long rpush(final byte[] key, final byte[]... strings);
	
	/**
	 * BLPOP is a blocking list pop primitive.
	 * It is the blocking version of LPOP because it blocks the connection when there are
	 * no elements to pop from any of the given lists.
	 * <p>
	 * An element is popped from the head of the first list that is non-empty,
	 * with the given keys being checked in the order that they are given.
	 *
	 * @param timeout
	 * @param key
	 * @return
	 */
	public List<String> blpop(final int timeout, final String key);
	
	public List<byte[]> blpop(final int timeout, final byte[]... keys);
	
	/**
	 * BRPOP is a blocking list pop primitive.
	 * <p>
	 * It is the blocking version of RPOP because it blocks the connection
	 * when there are no elements to pop from any of the given lists.
	 * <p>
	 * An element is popped from the tail of the first list that is non-empty,
	 * with the given keys being checked in the order that they are given.
	 *
	 * @param timeout
	 * @param key
	 * @return
	 */
	public List<String> brpop(final int timeout, final String key);
	
	public List<String> brpop(final int timeout, final String... keys);
	
	public List<byte[]> brpop(final int timeout, final byte[]... keys);
	
	/**
	 * Returns the length of the list stored at key.
	 * <p>
	 * If key does not exist, it is interpreted as an empty list and 0 is returned.
	 * An error is returned when the value stored at key is not a list.
	 *
	 * @param key
	 * @return
	 */
	public Long llen(final String key);
	
	public List<String> lrange(final String key, final long start, final long stop);
	
	public List<byte[]> lrange(final byte[] key, final long start, final long stop);
	
	/**
	 * Removes the first count occurrences of elements equal to element from the list stored at key.
	 * The count argument influences the operation in the following ways:
	 * <p>
	 * count > 0: Remove elements equal to element moving from head to tail.
	 * count < 0: Remove elements equal to element moving from tail to head.
	 * count = 0: Remove all elements equal to element.
	 * For example, LREM list -2 "hello" will remove the last two occurrences of "hello" in the list stored at list.
	 * <p>
	 * Note that non-existing keys are treated like empty lists, so when key does not exist, the command will always return 0.
	 *
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	public Long lrem(final String key, final long count, final String value);
	
	/**
	 * Add the specified members to the set stored at key.
	 * <p>
	 * Specified members that are already a member of this set are ignored.
	 * If key does not exist, a new set is created before adding the specified members.
	 * <p>
	 * An error is returned when the value stored at key is not a set.
	 * <p>
	 * return the number of elements that were added to the set, not including all
	 * the elements already present into the set.
	 *
	 * @param key
	 * @param members
	 * @return Long
	 */
	public Long sadd(final String key, final String... members);
	
	/**
	 * Add the specified members to the set stored at key.
	 * <p>
	 * Specified members that are already a member of this set are ignored.
	 * If key does not exist, a new set is created before adding the specified members.
	 * <p>
	 * An error is returned when the value stored at key is not a set.
	 * <p>
	 * return the number of elements that were added to the set, not including all
	 * the elements already present into the set.
	 *
	 * @param key
	 * @param members
	 * @return Long
	 */
	public Long sadd(final byte[] key, final byte[]... members);
	
	/**
	 * Remove the specified members from the set stored at key.
	 * <p>
	 * Specified members that are not a member of this set are ignored.
	 * If key does not exist, it is treated as an empty set and this command returns 0.
	 * <p>
	 * An error is returned when the value stored at key is not a set.
	 * <p>
	 * return the number of members that were removed from the set, not including non existing members.
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public Long srem(final byte[] key, final byte[]... member);
	
	/**
	 * Returns the set cardinality (number of elements) of the set stored at key.
	 * <p>
	 * return the cardinality (number of elements) of the set, or 0 if key does not exist.
	 *
	 * @param key
	 * @return
	 */
	public Long scard(final String key);
	
	/**
	 * Returns if member is a member of the set stored at key.
	 * <p>
	 * return:
	 * 1 if the element is a member of the set.
	 * 0 if the element is not a member of the set, or if key does not exist.
	 *
	 * @param key
	 * @param member
	 * @return
	 */
	public Boolean sismember(final byte[] key, final byte[] member);
	
	/**
	 * Returns all the members of the set value stored at key.
	 * <p>
	 * This has the same effect as running SINTER with one argument key.
	 *
	 * @param key
	 * @return Set<byte [ ]>
	 */
	public Set<byte[]> smembers(final byte[] key);
	
	/**
	 * Returns all the members of the set value stored at key.
	 * <p>
	 * This has the same effect as running SINTER with one argument key.
	 *
	 * @param key
	 * @return Set<String>
	 */
	public Set<String> smembers(final String key);
	
	public Boolean hexists(final byte[] key, final byte[] field);
	
	public Boolean hexists(final String key, final String field);
	
	/**
	 * 获取hash的长度(包含field的数量)
	 * @param key
	 * @return int
	 */
	public Long hlen(final byte[] key);
	
	public byte[] hget(final byte[] key, final byte[] field);
	
	public Long hset(final byte[] key, final byte[] field, final byte[] value);
	
	public String hmset(final String key, final Map<String, String> hash);
	
	public List<byte[]> hmget(final byte[] key, final byte[]... fields);
	
	public List<String> hmget(final String key, final String... fields);
	
	public Map<byte[], byte[]> hgetAll(final byte[] key);
	
	public Map<String, String> hgetAll(final String key);
	
	public List<String> hvals(String key);
	
	/**
	 * Set a timeout on key.
	 * After the timeout has expired, the key will automatically be deleted.
	 * A key with an associated timeout is often said to be volatile in Redis terminology.
	 * <p>
	 * The timeout will only be cleared by commands that delete or overwrite the contents
	 * of the key, including DEL, SET, GETSET and all the *STORE commands.
	 * <p>
	 * This means that all the operations that conceptually alter the value stored at the
	 * key without replacing it with a new one will leave the timeout untouched.
	 * <p>
	 * For instance, incrementing the value of a key with INCR, pushing a new value into a
	 * list with LPUSH, or altering the field value of a hash with HSET are all operations
	 * that will leave the timeout untouched.
	 * <p>
	 * The timeout can also be cleared, turning the key back into a persistent key, using the PERSIST command.
	 * <p>
	 * If a key is renamed with RENAME, the associated time to live is transferred to the new key name.
	 * <p>
	 * If a key is overwritten by RENAME, like in the case of an existing key Key_A that is
	 * overwritten by a call like RENAME Key_B Key_A, it does not matter if the original Key_A
	 * had a timeout associated or not, the new key Key_A will inherit all the characteristics of Key_B.
	 * <p>
	 * Note that calling EXPIRE/PEXPIRE with a non-positive timeout or EXPIREAT/PEXPIREAT with
	 * a time in the past will result in the key being deleted rather than expired
	 * (accordingly, the emitted key event will be del, not expired).
	 * <p>
	 * Refreshing expires
	 * <p>
	 * It is possible to call EXPIRE using as argument a key that already has an existing expire set.
	 * In this case the time to live of a key is updated to the new value.
	 *
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(final byte[] key, final int seconds);
	
	public Long expire(final String key, final int seconds);
	
	public Long expireAt(final String key, final long unixTime);
	
	public Long expireAt(final byte[] key, final long unixTime);
	
	/**
	 * Remove the existing timeout on key, turning the key from volatile (a key with an expire set)
	 * to persistent (a key that will never expire as no timeout is associated).
	 * <p>
	 * Return:
	 * 1 if the timeout was removed.
	 * 0 if key does not exist or does not have an associated timeout.
	 *
	 * @param key
	 * @return
	 */
	public Long persist(final String key);
	
	public Long persist(final byte[] key);
	
	/**
	 * Returns the remaining time to live of a key that has a timeout.
	 * This introspection capability allows a Redis client to check how
	 * many seconds a given key will continue to be part of the dataset.
	 * <p>
	 * The command returns -2 if the key does not exist.
	 * The command returns -1 if the key exists but has no associated expire.
	 *
	 * @param key
	 * @return
	 */
	public Long ttl(final String key);
	
	public Long ttl(final byte[] key);
	
	public Long del(final String key);
	
	public Long del(final byte[] key);
	
	public default Object eval(final String script) {
		throw new OperationNotSupportedException("这个API是针对单Instance或者Redis Sentinel的");
	}
	
	;
	
	/**
	 * @param script
	 * @param sampleKey Command will be executed in the node where the hash slot of this key is assigned to
	 * @return
	 */
	public default Object eval(String script, String sampleKey) {
		throw new OperationNotSupportedException("这个API是针对JedisCluster的");
	}
	
	public Object eval(final String script, final int keyCount, final String... params);
	
	/**
	 * 这个是默认实现, Redis单节点适用
	 * @param script
	 * @return
	 */
	public default String scriptLoad(final String script) {
		throw new OperationNotSupportedException();
	}
	
	/**
	 * 这个是基于Redis集群环境的实现, 需要提供一个sampleKey
	 * 暂时没有实现
	 * @param script
	 * @param sampleKey Command will be executed in the node where the hash slot of this key is assigned to
	 * @return
	 */
	public default String scriptLoad(String script, String sampleKey) {
		throw new OperationNotSupportedException();
	}
	
	/**
	 * 这个是基于Redis集群环境的实现, 需要提供一个sampleKey
	 * 暂时没有实现
	 * @param script
	 * @param sampleKey
	 * @return
	 */
	public default String scriptLoad(String script, Object sampleKey) {
		throw new OperationNotSupportedException();
	}
	
	/**
	 * 这个是基于Redis集群环境的实现, 需要提供一个sampleKey
	 * 暂时没有实现
	 * @param script
	 * @param sampleKey
	 * @return
	 */
	public default String scriptLoad(String script, byte[] sampleKey) {
		throw new OperationNotSupportedException();
	}
	
	/**
	 * 这个是基于Redis集群环境的实现, 需要提供一个sampleKey
	 * 暂时没有实现
	 * @param script
	 * @param sampleKey
	 * @return
	 */
	public default byte[] scriptLoad(byte[] script, byte[] sampleKey) {
		throw new OperationNotSupportedException();
	}
	
	public default Object evalsha(final String sha1) {
		throw new UnsupportedOperationException();
	}
	
	public default Object evalsha(final String sha1, final String sampleKey) {
		throw new UnsupportedOperationException();
	}
	
	public Object evalsha(final String sha1, final int keyCount, final String... params);
	
	public Object evalsha(final byte[] sha1, final int keyCount, final byte[]... params);
	
	public Long publish(final byte[] channel, final byte[] message);
	
	/**
	 * 订阅频道
	 * @param jedisPubSub
	 * @param channels
	 * @return
	 */
	public void subscribe(JedisPubSub jedisPubSub, String... channels);
	
	/**
	 * Subscribes the client to the given patterns.
	 * 
	 * Supported glob-style patterns:
	 *
	 * h?llo subscribes to hello, hallo and hxllo
	 * h*llo subscribes to hllo and heeeello
	 * h[ae]llo subscribes to hello and hallo, but not hillo
	 * 
	 * @param jedisPubSub
	 * @param patterns
	 */
	public void psubscribe(JedisPubSub jedisPubSub, String... patterns);
	
	/**
	 * 通过Redis pipeline执行
	 * @return
	 */
	public default List<Object> executePipelined(Consumer<Pipeline> consumer) {
		throw new UnsupportedOperationException();
	};
	
	public default String ping() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 直接暴露Jedis供客户端操作
	 * @return
	 */
	public Jedis jedis();
}
