package com.loserico.caffeine;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-07-01 17:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CaffeineTest {
	
	@SneakyThrows
	@Test
	public void testManual() {
		Cache<String, DataObject> cache = Caffeine.newBuilder()
				.expireAfterWrite(1, MINUTES)
				.maximumSize(100)
				.weakKeys()
				.weakValues()
				.build();
		
		String key = "A";
		DataObject data = cache.getIfPresent(key);
		assertNull(data);
		
		DataObject ipInfo = new DataObject("172.23.100.161");
		cache.put("172.23.100.161", ipInfo);
		cache.put("172.23.100.161", ipInfo);
		
		data = cache.getIfPresent("172.23.100.161");
		assertNotNull(data);
		data = cache.getIfPresent("172.23.100.161");
		assertNotNull(data);
		
		MINUTES.sleep(1);
		
		data = cache.getIfPresent("172.23.100.161");
		assertNull(data);
	}
	
	/**
	 * We can also get the value using the get method, which takes a Function along with a key as an argument.
	 * This function will be used for providing the fallback value if the key is not present in the cache,
	 * which would be inserted in the cache after computation
	 */
	@Test
	public void testPutIfAbsent() {
		Cache<String, DataObject> cache = Caffeine.newBuilder()
				.expireAfterWrite(1, MINUTES)
				.maximumSize(100)
				.build();
		
		DataObject data = cache.get("rico", (k) -> new DataObject(k));
		assertNotNull(data);
		assertThat(data.getData()).isEqualTo("rico");
	}
	
	@Test
	public void testInvalidate() {
		Cache<String, DataObject> cache = Caffeine.newBuilder()
				.expireAfterWrite(1, MINUTES)
				.maximumSize(100)
				.build();
		
		DataObject data = cache.get("rico", (k) -> new DataObject(k));
		assertNotNull(data);
		assertThat(data.getData()).isEqualTo("rico");
		
		cache.invalidate("rico");
		data = cache.getIfPresent("rico");
		assertNull(data);
	}
	
	/**
	 * This method of loading the cache takes a Function, which is used for initializing values, similar to the get method of the manual strategy.
	 */
	@Test
	public void testSynchronousLoading() {
		LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
				.maximumSize(100)
				.expireAfterWrite(1, MINUTES)
				.build(k -> new DataObject(k));
		
		DataObject dataObject = cache.get("rico");
		assertEquals(dataObject.getData(), "rico");
	}
	
	@Test
	public void testGetAllValues() {
		Cache<String, DataObject> cache = Caffeine.newBuilder()
				.expireAfterWrite(1, MINUTES)
				.maximumSize(100)
				.build();
		cache.put("rico", new DataObject("帅哥"));
		cache.put("vivi", new DataObject("大肥猪"));
		cache.put("arthy", new DataObject("小肥猪"));
		Map<String, DataObject> map = cache.getAllPresent(asList("rico", "vivi", "arthy"));
		assertThat(map.size()).isEqualTo(3);
		assertThat(map.get("arthy").getData()).isEqualTo("小肥猪");
	}
	
	@Test
	public void testAnsycLoading() {
		AsyncLoadingCache<Object, DataObject> cache = Caffeine.newBuilder()
				.maximumSize(100)
				.expireAfterWrite(1, MINUTES)
				.buildAsync(k -> DataObject.get("Data for " + k));
		
		String key = "A";
		cache.get(key).thenAccept((dataObject) -> {
			assertNotNull(dataObject);
			assertEquals("Data for " + key, dataObject.getData());
		});
		
		cache.getAll(Arrays.asList("A", "B", "C"))
				.thenAccept(dataObjectMap -> assertEquals(3, dataObjectMap.size()));
	}
	
	/**
	 * This type of eviction assumes that eviction occurs when the configured size limit of the cache is exceeded. 
	 * There are two ways of getting the size — counting objects in the cache, or getting their weights.
	 */
	@Test
	public void testSizeBasedEviction() {
		LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
				.maximumSize(1)
				.build(k -> DataObject.get("Data for " + k));
		assertEquals(0, cache.estimatedSize());
		
		cache.get("A");
		assertEquals(1, cache.estimatedSize());
		
		cache.get("B");
		cache.cleanUp();
		
		assertEquals(1, cache.estimatedSize());
	}
	
	@SneakyThrows
	@Test
	public void testTimeBasedEviction() {
		LoadingCache<Object, DataObject> cache = Caffeine.newBuilder()
				.expireAfterAccess(5, SECONDS)
				.build(k -> DataObject.get("Data for " + k));
		
		DataObject rico = cache.get("rico");
		assertThat(rico.getData()).isEqualTo("Data for rico");
		assertEquals(1, cache.estimatedSize());
		SECONDS.sleep(2);
		rico = cache.get("rico");
		assertThat(rico.getData()).isEqualTo("Data for rico");
		SECONDS.sleep(3);
		cache.cleanUp();
		assertEquals(1, cache.estimatedSize());
		SECONDS.sleep(2);
		cache.cleanUp();
		assertNull(rico);
		assertEquals(0, cache.estimatedSize());
		
		LoadingCache<Object, DataObject> cache2 = Caffeine.newBuilder()
				.expireAfterWrite(5, SECONDS)
				.weakKeys()
				.weakValues()
				.build(k -> DataObject.get("Data for " + k));
		
	}
	
	@Test
	public void testReferenceBasedEviction() {
		LoadingCache<Object, DataObject> cache = Caffeine.newBuilder()
				.expireAfterAccess(5, SECONDS)
				.weakKeys()
				.weakValues()
				.build(k -> DataObject.get("Data for " + k));
		
		LoadingCache<Object, DataObject> cache2 = Caffeine.newBuilder()
				.expireAfterAccess(5, SECONDS)
				.weakKeys()
				.softValues()
				.build(k -> DataObject.get("Data for " + k));
	}
	
	@Data
	static class DataObject {
		
		private final String data;
		
		private static int objectCounter = 0;
		
		
		public DataObject(String data) {
			this.data = data;
		}
		
		public static DataObject get(String data) {
			objectCounter++;
			return new DataObject(data);
		}
	}
}
