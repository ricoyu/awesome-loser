package com.loserico.search.pojo;

import com.loserico.search.ElasticUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.Operator;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2023-07-22 12:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class RequestBodyQueryTest {
	
	@Test
	public void testIgnoreUnavailableIndex() {
		List<Object> movies = ElasticUtils.Query.matchAllQuery("movies", "404index")
				.queryForList();
		assertThat(movies.size() == 9743);
	}
	
	@Test
	public void testMatchAnd() {
		List<Object> movies = ElasticUtils.Query.matchQuery("movies")
				.query("title", "King George")
				.operator(Operator.AND)
				.queryForList();
		assertEquals(1, movies.size());
	}
	
	@Test
	public void testMatchPhrase() {
		List<Object> movies = ElasticUtils.Query
				.matchPhraseQuery("movies")
				.query("title", "one love")
				.slop(1)
				.queryForList();
		
		assertEquals(movies.size(), 1);
	}
}
