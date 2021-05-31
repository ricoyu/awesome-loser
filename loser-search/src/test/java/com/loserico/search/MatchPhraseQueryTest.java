package com.loserico.search;

import com.loserico.search.pojo.Movie;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2021-05-28 9:09
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MatchPhraseQueryTest {
	
	@Test
	public void testMatchPhraseQuery() {
		List<Movie> movies1 = ElasticUtils.Query.matchPhraseQuery("movies")
				.query("title", "one love")
				.resultType(Movie.class)
				.sort("title.keyword:asc")
				.queryForList();
		movies1.forEach(System.out::println);
		
		List<Movie> movies2 = ElasticUtils.Query.matchPhraseQuery("movies")
				.query("title", "one love")
				.slop(1)
				.resultType(Movie.class)
				.queryForList();
		movies2.forEach(System.out::println);
		
		assertThat(movies1.size()).isLessThan(movies2.size());
	}
}
