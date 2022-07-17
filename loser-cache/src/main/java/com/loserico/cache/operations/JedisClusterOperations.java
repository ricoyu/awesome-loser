package com.loserico.cache.operations;

import com.loserico.json.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
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
	public Long zremRangeByScore(String key, String min, String max) {
		return jedisCluster.zremrangeByScore(key, min, max);
	}
	
	@Override
	public double zincrby(String key, String member, int increment) {
		return jedisCluster.zincrby(key, (double)increment, member);
	}
	
	@Override
	public Set<String> zrevrange(String key, int start, int end) {
		return jedisCluster.zrevrange(key, (long)start, (long)end);
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
	public String rpop(String key) {
		return jedisCluster.rpop(key);
	}
	
	@Override
	public List<String> rpop(String key, Integer count) {
		return jedisCluster.rpop(key, count);
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
	public List<String> sirandmember(String key, int count) {
		return jedisCluster.srandmember(key, count);
	}
	
	@Override
	public Set<String> spop(String key, int count) {
		return jedisCluster.spop(key, (long)count);
	}
	
	@Override
	public Set<String> sinter(String key1, String key2) {
		return jedisCluster.sinter(key1, key2);
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
	public long hincrby(String key, String field, long value) {
		return jedisCluster.hincrBy(key, field, value);
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
	public Set<String> zrangeByScore(String key, String min, String max) {
		return jedisCluster.zrangeByScore(key, min, max);
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
	public Boolean setbit(String key, long offset, int value) {
		return jedisCluster.setbit(key, offset, value+"");
	}
	
	@Override
	public Boolean setbit(String key, long offset, Boolean value) {
		return jedisCluster.setbit(key, offset, value);
	}
	
	@Override
	public Boolean getbit(String key, long offset) {
		return jedisCluster.getbit(key, offset);
	}
	
	@Override
	public Long bitAnd(String destKey, String[] srcKeys) {
		return jedisCluster.bitop(BitOP.AND, destKey, srcKeys);
	}
	
	@Override
	public Long bitOr(String destKey, String[] srcKeys) {
		return jedisCluster.bitop(BitOP.OR, destKey, srcKeys);
	}
	
	@Override
	public Long bitNot(String destKey, String srcKey) {
		return jedisCluster.bitop(BitOP.NOT, destKey, srcKey);
	}
	
	@Override
	public long bitCount(String key, int start, int end) {
		return jedisCluster.bitcount(key, start, end);
	}
	
	@Override
	public Long pfadd(String key, String... elements) {
		return jedisCluster.pfadd(key, elements);
	}
	
	@Override
	public Long pfcount(String... keys) {
		return jedisCluster.pfcount(keys);
	}
	
	@Override
	public String pfmerge(String destKey, String[] sourceKeys) {
		return jedisCluster.pfmerge(destKey, sourceKeys);
	}
	
	@Override
	public Long geoadd(String key, double longitude, double latitude, String member) {
		return jedisCluster.geoadd(key, longitude, latitude, member);
	}
	
	@Override
	public Long geoadd(String key, Map<String, GeoCoordinate> geoCoordinateMap) {
		return jedisCluster.geoadd(key, geoCoordinateMap);
	}
	
	@Override
	public Double geoDist(String key, String member1, String member2) {
		return jedisCluster.geodist(key, member1, member2);
	}
	
	@Override
	public Double geoDist(String key, String member1, String member2, GeoUnit unit) {
		return jedisCluster.geodist(key, member1, member2, unit);
	}
	
	@Override
	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
		return jedisCluster.georadiusByMember(key, member, radius, unit);
	}
	
	@Override
	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
		return jedisCluster.georadius(key, longitude, latitude, radius, unit);
	}
	
	@Override
	public Jedis jedis() {
		throw new UnsupportedOperationException("JedisClusterOperations不支持暴露Jedis");
	}
}
