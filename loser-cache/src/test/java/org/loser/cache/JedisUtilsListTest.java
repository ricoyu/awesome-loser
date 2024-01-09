package org.loser.cache;

import com.loserico.cache.JedisUtils;
import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-11-08 18:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JedisUtilsListTest {
	
	@Test
	public void testLPush() {
		String metadata = IOUtils.readClassPathFileAsString("metadata.json");
		List<String> metadatas = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			metadatas.add(metadata);
		}
		JedisUtils.LIST.rpush("metadata-list", metadatas);
	}
	
	@Test
	public void testLRange() {
		List<String> metadatas = JedisUtils.LIST.lrange("metadata-list", 0, 10010);
		System.out.println(metadatas.size());
		for (String metadata : metadatas) {
			if (metadata == null) {
				System.out.println("超过返回返回了null值");
			}
		}
	}
	
	@Test
	public void testRpopCount() {
		List<String> metadatas = JedisUtils.LIST.rpop("metadata-list", 1000);
		System.out.println(metadatas.size());
	}
	
	@Test
	public void testBrpop() {
		String val = JedisUtils.LIST.brpop(1, "metadata-list");
		System.out.println("ok");
	}
}
