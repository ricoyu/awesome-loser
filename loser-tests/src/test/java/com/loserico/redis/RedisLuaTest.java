package com.loserico.redis;

import com.loserico.common.lang.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;

/**
 * EVAL 语法
 * 	EVAL script numkeys key [key ...] arg [arg ...]
 * 
 * https://www.redisgreen.net/blog/intro-to-lua-for-redis-programmers/
 * <p>
 * Copyright: Copyright (c) 2018-01-10 10:36
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class RedisLuaTest {

	private Jedis jedis;

	@Before
	public void setup() {
		jedis = new Jedis("192.168.1.103", 6379);
		jedis.auth("deepdata$");
	}

	@Test
	public void testLua1() {
		String script = IOUtils.readClassPathFileAsString("hello-lua.lua");
		//redis-cli -a deepdata$ --eval lua-scripts/hello-lua.lua
		Object result = jedis.eval(script);
		System.out.println(result);
	}

	/**
	 * Accessing Keys and Arguments
	 * 
	 * Suppose we’re building a URL-shortener. 
	 * Each time a URL comes in we want to store it and return a unique number that can be used to access the URL later.
	 * 
	 * 调用形式为(逗号前面为KEYS， 后面是ARGV,参数个数不传也可以)：
	 *  redis-cli --eval incrset.lua links:counter links:urls , http://malcolmgladwellbookgenerator.com/
	 * We’ll use a Lua script to get a unique ID from Redis using INCR and immediately store the URL in a hash that is keyed by the unique ID:
	 * 	local link_id = redis.call("INCR", KEYS[1])
	 * 	redis.call("HSET", KEYS[2], link_id, ARGV[1])
	 * 	return link_id
	 * 
	 * KEYS 存KEY
	 * ARGV 存参数值
	 * 这两个数据结构都相当于数组，但是index从1开始。KEYS ARGV 要大写
	 * Table不能存nil值，如果往Table中塞了nil值，后续的值将被截断 [ 1, nil, 3, 4 ] 将截断成 [ 1 ]
	 * 
	 * We’re accessing two Lua tables, KEYS and ARGV. 
	 * Tables are associative arrays, and Lua’s only mechanism for structuring data. 
	 * For our purposes you can think of them as the equivalent of an array in whatever language you’re most comfortable with, 
	 * but note these two Lua-isms that trip up folks new to the language:
	 * 
	 * Tables cannot hold nil values. 
	 * If an operation would yield a table of [ 1, nil, 3, 4 ], the result will instead be [ 1 ] — the table is truncated at the first nil value.
	 * 
	 * When we invoke this script, we need to also pass along the values for the KEYS and ARGV tables. In the raw Redis protocol, the command looks like this:
	 * 	EVAL $incrset.lua 2 links:counter links:url, http://malcolmgladwellbookgenerator.com/
	 * 
	 * When calling EVAL, after the script we provide 2 as the number of KEYS that will be accessed, then we list our KEYS, and finally we provide values for ARGV.
	 * 
	 * Normally when we build apps with Redis Lua scripts, the Redis client library will take care of specifying the number of keys. 
	 * The above code block is shown for completeness, but here’s the easier way to do this on at the command line
	 * 
	 * 调用(客户端会帮我们处理参数个数，所以不传参数个数也是可以的，逗号前面的是KEYS， 后面的是ARGV	)：
	 * 	redis-cli --eval incrset.lua links:counter links:urls , http://malcolmgladwellbookgenerator.com/
	 * 
	 * When using --eval as above, the comma separates KEYS[] from ARGV[] items.
	 * 上面那段lua脚本按照传的参数展开后就相当于这样
	 * 	local link_id = redis.call("INCR", "links:counter")
	 * 	redis.call("HSET", "links:urls", link_id, "http://malcolmgladwellbookgenerator.com")
	 * 	return link_id
	 * 
	 * We’re accessing Redis for the first time here, using the call() function. 
	 * call()’s arguments are the commands to send to Redis: first we INCR <key>, then we HSET <key> <field> <value>. 
	 * These two commands will run sequentially — Redis won’t do anything else while this script executes, and it will run extremely quickly.
	 * 
	 * When writing Lua scripts for Redis, every key that is accessed should be accessed only by the KEYS table. 
	 * The ARGV table is used for parameter-passing — here it’s the value of the URL we want to store.
	 * @on
	 */
	@Test
	public void testLuaKeyArgs() {
		String script = IOUtils.readClassPathFileAsString("key-arg.lua");
		Object result = jedis.eval(script, 2, "links:cursor", "links:url", "http://pims.mulberrylearning.cn");
		System.out.println(result);
	}

	/**
	 * Our example above saves the link for our URL-shortener, but we also need to track the number of times a URL has been accessed. 
	 * To do that we’ll keep a counter in a hash in Redis. When a user comes along with a link identifier, we’ll check to see if it exists, and increment our counter for it if it does:
	 * 
	 * if redis.call("HEXISTS", KEYS[1], ARGV[1]) == 1 then
	 *   return redis.call("HINCRBY", KEYS[1], ARGV[1], 1)
	 * else
	 *   return nil
	 * end
	 * 
	 * Each time someone clicks on a shortlink, we run this script to track that the link was shared again. 
	 * We invoke the script using EVAL and pass in links:visits for our single key and the link identifier returned from our previous script as the single argument.
	 * 
	 * The script would look almost the same without hashes. Here’s a script which increments a standard Redis key only if it exists:
	 * 
	 * if redis.call("EXISTS",KEYS[1]) == 1 then
	 *   return redis.call("INCR",KEYS[1])
	 * else
	 *   return nil
	 * end
	 * 
	 * @on
	 */
	@Test
	public void testConditionalIncr() {
		String script = IOUtils.readClassPathFileAsString("condition-incr.lua");
		Object result = jedis.eval(script, 1, "links:visits", "2");
		System.out.println(result);
	}

	/**
	 * 获取lua脚本的sha校验和
	 * 执行
	 * @on
	 */
	@Test
	public void testGetLuaSha1() {
		String script = IOUtils.readClassPathFileAsString("hello-lua.lua");
		String result = jedis.scriptLoad(script);
		System.out.println(result);
		System.out.println(jedis.evalsha(result));
	}

	/**
	 * 先检查sha1校验和锁指定的脚本在Redis中是否存在，不存在则将脚本 script 添加到脚本缓存中，但并不立即执行这个脚本。
	 * 
	 * EVAL 命令也会将脚本添加到脚本缓存中，但是它会立即对输入的脚本进行求值。
	 * 如果给定的脚本已经在缓存里面了，那么不做动作。
	 * 
	 * 在脚本被加入到缓存之后，通过 EVALSHA 命令，可以使用脚本的 SHA1 校验和来调用这个脚本。
	 * 脚本可以在缓存中保留无限长的时间，直到执行 SCRIPT FLUSH 为止。
	 * @on
	 */
	@Test
	public void testIfLuaExists() {
		String sha1 = "0b01f4a19d2178d2990bc37670e5a42b121c37a6";
		String sha2 = sha1;
		boolean exists = jedis.scriptExists(sha1);
		if (!exists) {
			System.out.println("脚本不存在");
			String script = IOUtils.readClassPathFileAsString("hello-lua.lua");
			sha1 = jedis.scriptLoad(script);
			/*
			 * 脚本只要没改过，清除过后重新load进Redis， 它的sha1校验和是一样的
			 */
			assertEquals(sha1, sha2);
		}
		System.out.println(jedis.evalsha(sha1));
	}

	/**
	 * 清除所有脚本
	 */
	@Test
	public void testScriptFlush() {
		jedis.scriptFlush();
	}

	@Test
	public void testCJson() {
		jedis.set("apple", "{'color':'red', 'type':fruit}");
		Object result = jedis.eval(IOUtils.readClassPathFileAsString("cjson.lua"), 1, "apple", "type");
		System.out.println(result);
	}
}
