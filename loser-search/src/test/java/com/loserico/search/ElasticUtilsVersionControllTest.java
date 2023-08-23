package com.loserico.search;

import com.loserico.search.support.UpdateResult;
import com.loserico.search.vo.VersionedResult;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2023-08-14 15:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsVersionControllTest {
	
	@Test
	public void testGet() {
		VersionedResult<String> versionedResult = ElasticUtils.getWithVersion("products", 1);
		System.out.println(versionedResult.getDoc());
		
		UpdateResult updateResult = ElasticUtils.update("products")
				.id(versionedResult.getId())
				.doc("{\"title\": \"iphone\", \"count\": 98 }")
				.ifPrimaryTerm(333L)
				.ifSeqNo(versionedResult.getIfSeqNo())
				.update();
		
		assertTrue(updateResult.getResult() == UpdateResult.Result.UPDATED);
		
		updateResult = ElasticUtils.update("products")
				.id(versionedResult.getId())
				.doc("{\"title\": \"iphone\", \"count\": 97 }")
				.ifPrimaryTerm(versionedResult.getIfPrimaryTerm())
				.ifSeqNo(versionedResult.getIfSeqNo())
				.update();
		
		assertTrue(updateResult.getResult() == UpdateResult.Result.UPDATED);
		
		
	}
}
