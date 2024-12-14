package com.loserico.cache.operations;

import com.loserico.cache.concurrent.ThreadPool;
import com.loserico.cache.exception.JedisException;
import com.loserico.cache.utils.ByteUtils;
import com.loserico.json.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.args.BitOP;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.resps.GeoRadiusResponse;
import redis.clients.jedis.util.Pool;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * <p>
 * Copyright: (C), 2019/10/24 7:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisPoolOperations implements JedisOperations {
	
	private static final Logger log = LoggerFactory.getLogger(JedisPoolOperations.class);
	
	private final Pool<Jedis> pool;
	
	private static final ExecutorService THREAD_POOL = ThreadPool.newThreadPool();
	
	public JedisPoolOperations(Pool<Jedis> pool) {
		this.pool = pool;
	}
	
	@Override
	public String set(byte[] key, byte[] value) {
		return operate((jedis) -> jedis.set(key, value));
	}
	
	@Override
	public Long setnx(byte[] key, byte[] value) {
		return operate((jedis) -> jedis.setnx(key, value));
	}
	
	@Override
	public byte[] get(byte[] key) {
		return operate((jedis) -> jedis.get(key));
	}
	
	@Override
	public Boolean exists(String key) {
		return operate((jedis) -> jedis.exists(key));
	}
	
	@Override
	public Boolean exists(byte[] key) {
		return operate((jedis) -> jedis.exists(key));
	}
	
	@Override
	public List<String> keys(String pattern) {
		Set<String> keys = operate((jedis) -> jedis.keys(pattern));
		return keys.stream().collect(Collectors.toList());
	}
	
	@Override
	public Long incr(String key) {
		return operate((jedis) -> jedis.incr(key));
	}
	
	@Override
	public Long incrBy(String key, long increment) {
		return operate((jedis) -> jedis.incrBy(key, increment));
	}
	
	@Override
	public Double zscore(String key, String member) {
		return operate((jedis) -> jedis.zscore(key, member));
	}
	
	@Override
	public Long zadd(String key, double score, String member) {
		return operate((jedis) -> jedis.zadd(key, score, member));
	}
	
	@Override
	public Long zadd(byte[] key, double score, byte[] member) {
		return operate((jedis) -> jedis.zadd(key, score, member));
	}
	
	@Override
	public Long zadd(String key, double score, Object member) {
		return operate((jedis) -> jedis.zadd(ByteUtils.toBytes(key), score, JacksonUtils.toBytes(member)));
	}
	
	@Override
	public Long zcard(String key) {
		return operate((jedis) -> jedis.zcard(key));
	}
	
	@Override
	public double zincrby(String key, String member, int increment) {
		return operate((jedis) -> jedis.zincrby(key, (double)increment, member));
	}
	
	@Override
	public List<String> zrevrange(String key, int start, int end) {
		return operate((jedis) -> jedis.zrevrange(key, (long)start, (long)end));
	}
	
	@Override
	public Long zremByRank(String key, long start, long end) {
		return operate((jedis) -> jedis.zremrangeByRank(key, start, end));
	}
	
	@Override
	public Long zremRangeByScore(String key, String min, String max) {
		return operate(jedis -> jedis.zremrangeByScore(key, min, max));
	}
	
	@Override
	public List<String> zrange(String key, long start, long end) {
		return operate((jedis) -> jedis.zrange(key, start, end));
	}
	
	@Override
	public List<String> zrangeByScore(String key, String min, String max) {
		return operate(jedis -> jedis.zrangeByScore(key, min, max));
	}
	
	@Override
	public byte[] lpop(byte[] key) {
		return operate((jedis) -> jedis.lpop(key));
	}
	
	@Override
	public String lpop(String key) {
		return operate((jedis) -> jedis.lpop(key));
	}
	
	@Override
	public Long lpush(String key, String... strings) {
		return operate((jedis) -> jedis.lpush(key, strings));
	}
	
	@Override
	public Long lpush(byte[] key, byte[]... strings) {
		return operate((jedis) -> jedis.lpush(key, strings));
	}
	
	@Override
	public Long rpush(String key, String... strings) {
		return operate((jedis) -> jedis.rpush(key, strings));
	}
	
	@Override
	public Long rpush(byte[] key, byte[]... strings) {
		return operate((jedis) -> jedis.rpush(key, strings));
	}
	
	@Override
	public List<String> blpop(int timeout, String key) {
		return operate((jedis) -> jedis.blpop(timeout, key));
	}
	
	@Override
	public List<byte[]> blpop(int timeout, byte[]... keys) {
		return operate((jedis) -> jedis.blpop(timeout, keys));
	}
	
	@Override
	public List<String> brpop(int timeout, String key) {
		return operate((jedis) -> jedis.brpop(timeout, key));
	}
	
	@Override
	public List<String> brpop(int timeout, String... keys) {
		return operate((jedis) -> jedis.brpop(timeout, keys));
	}
	
	@Override
	public List<byte[]> brpop(int timeout, byte[]... keys) {
		return operate((jedis) -> jedis.brpop(timeout, keys));
	}
	
	@Override
	public String rpop(String key) {
		return operate(jedis -> jedis.rpop(key));
	}
	
	@Override
	public List<String> rpop(String key, Integer count) {
		List<String> list = operate(jedis -> jedis.rpop(key, count));
		if (list == null) {
			return emptyList();
		}
		return list;
	}
	
	@Override
	public Long llen(String key) {
		return operate((jedis) -> jedis.llen(key));
	}
	
	@Override
	public String lindex(String key, int index) {
		return operate((jedis) -> jedis.lindex(key, (long)index));
	}
	
	@Override
	public String lindex(String key, long index) {
		return operate((jedis) -> jedis.lindex(key, index));
	}
	
	@Override
	public List<String> lrange(String key, long start, long stop) {
		return operate((jedis) -> jedis.lrange(key, start, stop));
	}
	
	@Override
	public List<byte[]> lrange(byte[] key, long start, long stop) {
		return operate((jedis) -> jedis.lrange(key, start, stop));
	}
	
	@Override
	public Long lrem(String key, long count, String value) {
		return operate((jedis) -> jedis.lrem(key, count, value));
	}
	
	@Override
	public Long sadd(String key, String... members) {
		return operate((jedis) -> jedis.sadd(key, members));
	}
	
	@Override
	public Long sadd(byte[] key, byte[]... members) {
		return operate((jedis) -> jedis.sadd(key, members));
	}
	
	@Override
	public Long srem(byte[] key, byte[]... members) {
		return operate((jedis) -> jedis.srem(key, members));
	}
	
	@Override
	public Long scard(String key) {
		return operate((jedis) -> jedis.scard(key));
	}
	
	@Override
	public Boolean sismember(byte[] key, byte[] member) {
		return operate((jedis) -> jedis.sismember(key, member));
	}
	
	@Override
	public Set<byte[]> smembers(byte[] key) {
		return operate((jedis) -> jedis.smembers(key));
	}
	
	@Override
	public Set<String> smembers(String key) {
		return operate((jedis) -> jedis.smembers(key));
	}
	
	@Override
	public List<String> sirandmember(String key, int count) {
		return operate((jedis) -> jedis.srandmember(key, count));
	}
	
	@Override
	public Set<String> spop(String key, int count) {
		return operate((jedis) -> jedis.spop(key, (long)count));
	}
	
	@Override
	public Set<String> sinter(String key1, String key2) {
		return operate((jedis) -> jedis.sinter(key1, key2));
	}
	
	@Override
	public Boolean hexists(byte[] key, byte[] field) {
		return operate((jedis) -> jedis.hexists(key, field));
	}
	
	@Override
	public Boolean hexists(String key, String field) {
		return operate((jedis) -> jedis.hexists(key, field));
	}
	
	@Override
	public byte[] hget(byte[] key, byte[] field) {
		return operate((jedis) -> jedis.hget(key, field));
	}
	
	@Override
	public Long hset(byte[] key, byte[] field, byte[] value) {
		return operate((jedis) -> jedis.hset(key, field, value));
	}
	
	@Override
	public String hmset(String key, Map<String, String> hash) {
		return operate((jedis) -> jedis.hmset(key, hash));
	}
	
	@Override
	public long hincrby(String key, String field, long value) {
		return operate((jedis) -> jedis.hincrBy(key, field, value));
	}
	
	@Override
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		return operate((jedis) -> jedis.hmget(key, fields));
	}
	
	@Override
	public List<String> hmget(String key, String... fields) {
		return operate((jedis) -> jedis.hmget(key, fields));
	}
	
	@Override
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return operate((jedis) -> jedis.hgetAll(key));
	}
	
	@Override
	public Map<String, String> hgetAll(String key) {
		return operate((jedis) -> jedis.hgetAll(key));
	}
	
	@Override
	public List<String> hvals(String key) {
		return operate((jedis) -> jedis.hvals(key));
	}
	
	@Override
	public Long expire(byte[] key, int seconds) {
		return operate((jedis) -> jedis.expire(key, seconds));
	}
	
	@Override
	public Long hlen(byte[] key) {
		return operate((jedis) -> jedis.hlen(key));
	}
	
	@Override
	public Long expire(String key, int seconds) {
		return operate((jedis) -> jedis.expire(key, seconds));
	}
	
	@Override
	public Long expireAt(String key, long unixTime) {
		return operate((jedis) -> jedis.expireAt(key, unixTime));
	}
	
	@Override
	public Long expireAt(byte[] key, long unixTime) {
		return operate((jedis) -> jedis.expireAt(key, unixTime));
	}
	
	@Override
	public Long persist(String key) {
		return operate((jedis) -> jedis.persist(key));
	}
	
	@Override
	public Long persist(byte[] key) {
		return operate((jedis) -> jedis.persist(key));
	}
	
	@Override
	public Long ttl(String key) {
		return operate((jedis) -> jedis.ttl(key));
	}
	
	@Override
	public Long ttl(byte[] key) {
		return operate((jedis) -> jedis.ttl(key));
	}
	
	@Override
	public Long del(String key) {
		return operate((jedis) -> jedis.del(key));
	}
	
	@Override
	public Long del(byte[] key) {
		return operate((jedis) -> jedis.del(key));
	}
	
	@Override
	public Object eval(String script) {
		return operate((jedis) -> jedis.eval(script));
	}
	
	@Override
	public Object eval(String script, int keyCount, String... params) {
		return operate((jedis) -> jedis.eval(script, keyCount, params));
	}
	
	@Override
	public String scriptLoad(String script) {
		return operate((jedis) -> jedis.scriptLoad(script));
	}
	
	@Override
	public Object evalsha(String sha1) {
		return operate((jedis) -> jedis.evalsha(sha1));
	}
	
	@Override
	public Object evalsha(String sha1, int keyCount, String... params) {
		return operate((jedis) -> jedis.evalsha(sha1, keyCount, params));
	}
	
	@Override
	public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
		return operate((jedis) -> jedis.evalsha(sha1, keyCount, params));
	}
	
	@Override
	public Long publish(byte[] channel, byte[] message) {
		return operate((jedis) -> {
			return jedis.publish(channel, message);
		});
	}
	
	/**
	 * 订阅消息用的Jedis实例不要用完就关掉, 否则接收消息的时候会抛异常
	 * redis.clients.jedis.exceptions.JedisConnectionException: Unexpected end of stream.
	 *
	 * @param jedisPubSub
	 * @param channels
	 */
	@Override
	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		//这边必须要走多线程, 否则会阻塞, 比如SpringBoot启动的时候, 如果有订阅消息的操作, 会阻塞SpringBoot启动
		THREAD_POOL.execute(() -> pool.getResource().subscribe(jedisPubSub, channels));
		//pool.getResource().subscribe(jedisPubSub, channels);
	}
	
	/**
	 * 订阅消息用的Jedis实例不要用完就关掉, 否则接收消息的时候会抛异常
	 * redis.clients.jedis.exceptions.JedisConnectionException: Unexpected end of stream.
	 *
	 * @param jedisPubSub
	 * @param patterns
	 */
	@Override
	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		THREAD_POOL.execute(() -> pool.getResource().psubscribe(jedisPubSub, patterns));
	}
	
	@Override
	public String ping() {
		return operate((jedis) -> jedis.ping());
	}
	
	@Override
	public Jedis jedis() {
		return pool.getResource();
	}
	
	@Override
	public List<Object> executePipelined(Consumer<Pipeline> consumer) {
		Jedis jedis = pool.getResource();
		try {
			Pipeline pipelined = jedis.pipelined();
			consumer.accept(pipelined);
			return pipelined.syncAndReturnAll();
		} catch (Throwable e) {
			log.error("", e);
			throw new JedisException(e);
		} finally {
			jedis.close();
		}
	}
	
	private <R> R operate(Function<Jedis, R> func) {
		Jedis jedis = pool.getResource();
		try {
			return func.apply(jedis);
		} catch (Throwable e) {
			log.error("", e);
			throw new JedisException(e);
		} finally {
			jedis.close();
		}
	}
	
	@Override
	public Boolean setbit(String key, long offset, int value) {
		return operate((jedis) -> {
			return jedis.setbit(key, offset, value == 1);
		});
	}
	
	@Override
	public Boolean setbit(String key, long offset, Boolean value) {
		return operate((jedis) -> {
			return jedis.setbit(key, offset, value);
		});
	}
	
	@Override
	public Boolean getbit(String key, long offset) {
		return operate((jedis) -> {
			return jedis.getbit(key, offset);
		});
	}
	
	@Override
	public Long bitAnd(String destKey, String[] srcKeys) {
		return operate((jedis) -> {
			return jedis.bitop(BitOP.AND, destKey, srcKeys);
		});
	}
	
	@Override
	public Long bitOr(String destKey, String[] srcKeys) {
		return operate((jedis) -> {
			return jedis.bitop(BitOP.OR, destKey, srcKeys);
		});
	}
	
	@Override
	public Long bitNot(String destKey, String srcKey) {
		return operate((jedis) -> {
			return jedis.bitop(BitOP.NOT, destKey, srcKey);
		});
	}
	
	@Override
	public long bitCount(String key, int start, int end) {
		return operate((jedis) -> {
			return jedis.bitcount(key, start, end);
		});
	}
	
	@Override
	public long bitCount(String key) {
		return jedis().bitcount(key);
	}
	
	@Override
	public Long pfadd(String key, String... elements) {
		return operate((jedis) -> {
			return jedis.pfadd(key, elements);
		});
	}
	
	@Override
	public Long pfcount(String... keys) {
		return operate((jedis) -> {
			return jedis.pfcount(keys);
		});
	}
	
	@Override
	public String pfmerge(String destKey, String[] sourceKeys) {
		return operate((jedis) -> {
			return jedis.pfmerge(destKey, sourceKeys);
		});
	}
	
	@Override
	public Long geoadd(String key, Map<String, GeoCoordinate> geoCoordinateMap) {
		return operate((jedis) -> {
			return jedis.geoadd(key, geoCoordinateMap);
		});
	}
	
	@Override
	public Long geoadd(String key, double longitude, double latitude, String member) {
		return operate((jedis) -> {
			return jedis.geoadd(key, longitude, latitude, member);
		});
	}
	
	@Override
	public Double geoDist(String key, String member1, String member2) {
		return operate((jedis) -> {
			return jedis.geodist(key, member1, member2);
		});
	}
	
	@Override
	public Double geoDist(String key, String member1, String member2, GeoUnit unit) {
		return operate((jedis) -> {
			return jedis.geodist(key, member1, member2, unit);
		});
	}
	
	@Override
	public List<GeoRadiusResponse>  georadiusByMember(String key, String member, double radius, GeoUnit unit) {
		return operate((jedis) -> {
			return jedis.georadiusByMember(key, member, radius, unit);
		});
	}
	
	@Override
	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
		return operate((jedis) -> {
			return jedis.georadius(key, longitude, latitude, radius, unit);
		});
	}
}
