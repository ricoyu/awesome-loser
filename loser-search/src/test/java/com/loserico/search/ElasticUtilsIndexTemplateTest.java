package com.loserico.search;

import com.loserico.common.lang.utils.IOUtils;
import org.elasticsearch.cluster.metadata.IndexTemplateMetadata;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2022-01-11 17:01
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ElasticUtilsIndexTemplateTest {
	
	@Test
	public void testDeleteIndexTemplate() {
		boolean deleted = ElasticUtils.Admin.deleteIndexTemplate("event_template2");
		assertTrue(deleted);
	}
	
	@Test
	public void testCreateIndexTemplateByRestAPI() {
		boolean created = ElasticUtils.Admin.putIndexTemplate("event_template", IOUtils.readClassPathFileAsString("index_template.json"));
		assertTrue(created);
	}
	
	@Test
	public void testGetIndexTemplate() {
		Map<String, IndexTemplateMetadata> eventTemplate = ElasticUtils.Admin.getIndexTemplate("event_template");
		System.out.println(eventTemplate);
	}
}
