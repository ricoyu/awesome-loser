package com.loserico.json;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jsonpath.JsonPathUtils;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-04-29 16:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JsonPathUtilsTest {
	
	@Test
	public void test() {
		String json = IOUtils.readClassPathFileAsString("es-data.json");
		List<Movie> movies = JsonPathUtils.readListNode(json, "$.hits.hits[*]._source", Movie.class);
		movies.forEach(System.out::println);
		JsonPathUtils.readNode(json, "$.hits");
	}
}
