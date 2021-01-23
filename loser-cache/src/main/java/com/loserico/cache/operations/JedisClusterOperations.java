package com.loserico.cache.operations;

import com.loserico.json.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.loserico.cache.utils.ByteUtils.toBytes;

/**
 * <p>
 * Copyright: (C), 2019/10/24 7:38
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JedisClusterOperations implements JedisOperations {
	
	private final JedisCluster jedisCluster;
	
	public JedisClusterOperations(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	
	@Override
	public String set(byte[] key, byte[] value) {
		return jedisCluster.set(key, value);
	}
	
	@Override
	public Long setnx(byte[] key, byte[] value) {
		return jedisCluster.setnx(key, value);
	}
	
	@Override
	public byte[] get(byte[] key) {
		return jedisCluster.get(key);
	}
	
	@Override
	public Boolean exists(String key) {
		return jedisCluster.exists(key);
	}
	
	@Override
	public Boolean exists(byte[] key) {
		return jedisCluster.exists(key);
	}
	
	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}
	
	@Override
	public Long incrBy(String key, long increment) {
		return jedisCluster.incrBy(key, increment);
	}
	
	@Override
	public Double zscore(String key, String member) {
		return jedisCluster.zscore(key, member);
	}
	
	@Override
	public Long zadd(String key, double score, String member) {
		return jedisCluster.zadd(key, score, member);
	}
	
	@Override
	public Long zadd(byte[] key, double score, byte[] member) {
		return jedisCluster.zadd(key, score, member);
	}
	
	@Override
	public Long zadd(String key, double score, Object member) {
		return jedisCluster.zadd(toBytes(key), score, JacksonUtils.toBytes(member));
	}
	
	@Override
	public Long zremByRank(String key, long start, long end) {
		return jedisCluster.zremrangeByRank(key, start, end);
	}
	
	@Override
	public Long zcard(String key) {
		return jedisCluster.zcard(key);
	}
	
	@Override
	public byte[] lpop(byte[] key) {
		return jedisCluster.lpop(key);
	}
	
	@Override
	public String lpop(String key) {
		return jedisCluster.lpop(key);
	}
	
	@Override
	public Long lpush(String key, String... values) {
		return jedisCluster.lpush(key, values);
	}
	
	@Override
	public Long lpush(byte[] key, byte[]... values) {
		return jedisCluster.lpush(key, values);
	}
	
	@Override
	public Long rpush(String key, String... values) {
		return jedisCluster.rpush(key, values);
	}
	
	@Override
	public Long rpush(byte[] key, byte[]... values) {
		return jedisCluster.rpush(key, values);
	}
	
	@Override
	public List<String> blpop(int timeout, String key) {
		return jedisCluster.blpop(timeout, key);
	}
	
	@Override
	public List<byte[]> blpop(int timeout, byte[]... keys) {
		return jedisCluster.blpop(timeout, keys);
	}
	
	@Override
	public List<String> brpop(int timeout, String key) {
		return jedisCluster.brpop(timeout, key);
	}
	
	@Override
	public List<String> brpop(int timeout, String... keys) {
		return jedisCluster.brpop(timeout, keys);
	}
	
	@Override
	public List<byte[]> brpop(int timeout, byte[]... keys) {
		return jedisCluster.brpop(timeout, keys);
	}
	
	@Override
	public Long llen(String key) {
		return jedisCluster.llen(key);
	}
	
	@Override
	public List<String> lrange(String key, long start, long stop) {
		return jedisCluster.lrange(key, start, stop);
	}
	
	@Override
	public List<byte[]> lrange(byte[] key, long start, long stop) {
		return jedisCluster.lrange(key, start, stop);
	}
	
	@Override
	public Long lrem(String key, long count, String value) {
		return jedisCluster.lrem(key, count, value);
	}
	
	@Override
	public Long sadd(String key, String... members) {
		return jedisCluster.sadd(key, members);
	}
	
	@Override
	public Long sadd(byte[] key, byte[]... members) {
		return jedisCluster.sadd(key, members);
	}
	
	@Override
	public Long srem(byte[] key, byte[]... member) {
		return jedisCluster.srem(key, member);
	}
	
	@Override
	public Long scard(String key) {
		return jedisCluster.scard(key);
	}
	
	@Override
	public Boolean sismember(byte[] key, byte[] member) {
		return jedisCluster.sismember(key, member);
	}
	
	@Override
	public Set<byte[]> smembers(byte[] key) {
		return jedisCluster.smembers(key);
	}
	
	@Override
	public Set<String> smembers(String key) {
		return jedisCluster.smembers(key);
	}
	
	@Override
	public Boolean hexists(byte[] key, byte[] field) {
		return jedisCluster.hexists(key, field);
	}
	
	@Override
	public Boolean hexists(String key, String field) {
		return jedisCluster.hexists(key, field);
	}
	
	@Override
	public byte[] hget(byte[] key, byte[] field) {
		return jedisCluster.hget(key, field);
	}
	
	@Override
	public Long hset(byte[] key, byte[] field, byte[] value) {
		return jedisCluster.hset(key, field, value);
	}
	
	@Override
	public String hmset(String key, Map<String, String> hash) {
		return jedisCluster.hmset(key, hash);
	}
	
	@Override
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		return jedisCluster.hmget(key, fields);
	}
	
	@Override
	public List<String> hmget(String key, String... fields) {
		return jedisCluster.hmget(key, fields);
	}
	
	@Override
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return jedisCluster.hgetAll(key);
	}
	
	@Override
	public Map<String, String> hgetAll(String key) {
		return jedisCluster.hgetAll(key);
	}
	
	@Override
	public List<String> hvals(String key) {
		return jedisCluster.hvals(key);
	}
	
	@Override
	public Set<String> zrange(String key, long start, long end) {
		return jedisCluster.zrange(key, start, end);
	}
	
	@Override
	public Long hlen(byte[] key) {
		return jedisCluster.hlen(key);
	}
	
	@Override
	public Long expire(String key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}
	
	@Override
	public Long expire(byte[] key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}
	
	@Override
	public Long expireAt(String key, long unixTime) {
		return jedisCluster.expireAt(key, unixTime);
	}
	
	@Override
	public Long expireAt(byte[] key, long unixTime) {
		return jedisCluster.expireAt(key, unixTime);
	}
	
	@Override
	public Long persist(String key) {
		return jedisCluster.persist(key);
	}
	
	@Override
	public Long persist(byte[] key) {
		return jedisCluster.persist(key);
	}
	
	@Override
	public Long ttl(String key) {
		return jedisCluster.ttl(key);
	}
	
	@Override
	public Long ttl(byte[] key) {
		return jedisCluster.ttl(key);
	}
	
	@Override
	public Long del(String key) {
		return jedisCluster.del(key);
	}
	
	@Override
	public Long del(byte[] key) {
		return jedisCluster.del(key);
	}
	
	@Override
	public Object eval(String script, String sampleKey) {
		return jedisCluster.eval(script, sampleKey);
	}
	
	@Override
	public Object eval(String script, int keyCount, String... params) {
		return jedisCluster.eval(script, keyCount, params);
	}
	
	@Override
	public String scriptLoad(String script, String sampleKey) {
		return jedisCluster.scriptLoad(script, sampleKey);
	}
	
	@Override
	public byte[] scriptLoad(byte[] script, byte[] sampleKey) {
		return jedisCluster.scriptLoad(script, sampleKey);
	}
	
	@Override
	public Object evalsha(String sha1, String sampleKey) {
		return jedisCluster.evalsha(sha1, sampleKey);
	}
	
	@Override
	public Object evalsha(String sha1, int keyCount, String... params) {
		return jedisCluster.evalsha(sha1, keyCount, params);
	}
	
	@Override
	public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
		return jedisCluster.evalsha(sha1, keyCount, params);
	}
	
	@Override
	public Long publish(byte[] channel, byte[] message) {
		return jedisCluster.publish(channel, message);
	}
	
	@Override
	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		log.info("Not implemented yet!");
	}
	
	@Override
	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		log.info("Not implemented yet!");
	}
	
	@Override
	public Jedis jedis() {
		throw new UnsupportedOperationException("JedisClusterOperations不支持暴露Jedis");
	}
}
