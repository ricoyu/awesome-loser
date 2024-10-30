package com.loserico.search;

import org.elasticsearch.index.engine.VersionConflictEngineException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElasticCreateTest {

	@Test(expected = VersionConflictEngineException.class)
	public void testCreateOneOk() {
		ElasticUtils.delete("users", "1");
		String id = ElasticUtils.create("users", """
                {
					"name": "三少爷",
					"age": 42
				}
				""", 1);
		assertEquals(1, 1);
		id = ElasticUtils.create("users", """
				{
					"name": "三少爷",
					"age": 42
				}
				""", 1);
	}
}
