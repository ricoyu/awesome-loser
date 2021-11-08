package com.loserico.search;

import com.loserico.json.jackson.JacksonUtils;
import com.loserico.search.pojo.Asset;
import com.loserico.search.support.BulkResult;
import com.loserico.search.support.UpdateResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * Copyright: (C), 2021-11-08 9:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticInnerObjUpdateTest {
	
	@Test
	public void testUpdateAssetToNull() {
		HashMap<String, Object> doc = new HashMap<>();
		doc.put("asset", null);
		UpdateResult updateResult = ElasticUtils.update("netlog_2021-11-07", "UxyI-nwBIZVr6K7KrGih", doc);
		System.out.println(updateResult.getResult());
	}
	
	@Test
	public void testUpdateAsset() {
		Asset asset = new Asset();
		asset.setName("机皇二代");
		HashMap<String, Object> doc = new HashMap<>();
		doc.put("asset", asset);
		UpdateResult updateResult = ElasticUtils.update("netlog_2021-11-07", "UxyI-nwBIZVr6K7KrGih", doc);
		System.out.println(updateResult.getResult());
	}
	
	@Test
	public void testBulkUpdateAsset() {
		Asset asset = new Asset();
		asset.setName("机皇二代");
		asset.setDapartment("基础架构组");
		HashMap<String, Object> doc = new HashMap<>();
		doc.put("asset", JacksonUtils.pojoToMap(asset));
		
		BulkResult bulkResult = ElasticUtils.bulkUpdate()
				.doc("netlog_2021-11-07", "UxyI-nwBIZVr6K7KrGih", doc)
				.execute();
		
		System.out.println(bulkResult.getSuccessCount());
	}
	
	@SneakyThrows
	@Test
	public void test() {
		AtomicLong quietPeriod = new AtomicLong();
		while (true) {
			System.out.println("...");
			quietPeriod.addAndGet(100L);
			if (quietPeriod.get() <= 1000) {
				try {
					Thread.sleep(quietPeriod.get());
				} catch (InterruptedException e) {
					log.error("", e);
				}
			} else {
				quietPeriod.set(0L);
			}
		}
	}
}
