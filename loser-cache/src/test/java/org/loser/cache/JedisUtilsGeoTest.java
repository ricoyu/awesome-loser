package org.loser.cache;

import com.loserico.cache.JedisUtils;
import org.junit.Test;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2022-07-17 18:06
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisUtilsGeoTest {
	
	@Test
	public void test1() {
		Long geoadd = JedisUtils.GEO.geoadd("a-geo", 120.744625, 31.324218, "湖畔天城");
		Long geoadd2 = JedisUtils.GEO.geoadd("a-geo", 120.743807,31.247955, "淞泽家园");
		System.out.println(geoadd2);
		Double dist = JedisUtils.GEO.geoDist("a-geo", "淞泽家园", "湖畔天城");
		System.out.println("湖畔天城距离淞泽家园: " + dist +" 米");
	}
	
	@Test
	public void testGeo2() {
		List<GeoRadiusResponse> responses = JedisUtils.GEO.georadiusByMember("a-geo", "湖畔天城", 9, GeoUnit.KM);
		for (GeoRadiusResponse respons : responses) {
			System.out.println(respons.getMemberByString());
		}
		System.out.println("----------------");
		List<GeoRadiusResponse> responses2 = JedisUtils.GEO.georadius("a-geo", 120.741705, 31.260843, 9, GeoUnit.KM);
		for (GeoRadiusResponse response : responses2) {
			System.out.println(response.getMemberByString());
		}
	}
	
	@Test
	public void test3() {
		List<String> sets = JedisUtils.ZSET.zrange("a-geo", 0, -1);
		for (String set : sets) {
			System.out.println(set);
		}
	}
}
