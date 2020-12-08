package com.loserico.java8.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Copyright: (C), 2020-12-04 15:33
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ListSliceTest {
	
	@Test
	public void testListStreamSlice() {
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		List<String> results = list.stream().limit(3).collect(toList());
		assertThat(results.size() == 3);
		
		List<String> list2 = new ArrayList<>();
		list.add("1");
		list.add("2");
		List<String> results2 = list2.stream().limit(3).collect(toList());
		assertThat(results2.size() == 2);
	}
}
