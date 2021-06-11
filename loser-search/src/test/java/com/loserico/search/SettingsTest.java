package com.loserico.search;

import com.loserico.search.enums.FieldType;
import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2021-06-02 9:28
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SettingsTest {
	
	@Test
	public void testMakeSettings() {
		ElasticUtils.Admin.deleteIndex("test666");
		ElasticUtils.Admin.createIndex("test666")
				.settings()
				.numberOfShards(1)
				.numberOfReplicas(1)
				.and()
				.mapping()
				.field("name", FieldType.KEYWORD)
				.thenCreate();
	}
}
