package com.loserico.search;

import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

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
	public void testCreateIndexTemplateByRestAPI() {
		ElasticUtils.Admin.putIndexTemplate("event_template2", IOUtils.readClassPathFileAsString("index_template.json"));
	}
}
