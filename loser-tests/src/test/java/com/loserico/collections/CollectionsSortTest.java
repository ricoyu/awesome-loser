package com.loserico.collections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * <p>
 * Copyright: (C), 2021-10-28 10:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CollectionsSortTest {
	
	@Test
	public void testArrayListSort() {
		List<Event> events = new ArrayList<>();
		events.add(new Event(ThreadLocalRandom.current().nextLong()));
		events.add(new Event(ThreadLocalRandom.current().nextLong()));
		events.add(new Event(ThreadLocalRandom.current().nextLong()));
		events.add(new Event(ThreadLocalRandom.current().nextLong()));
		
		events = events.stream().sorted((prev, next) -> {
			if (prev.getCreateTime() < next.getCreateTime()) {
				return 1;
			}
			
			if (next.getCreateTime() > prev.getCreateTime()) {
				return -1;
			}
			return 0;
		}).collect(Collectors.toList());
		
		System.out.println("=================");
		for (Event event : events) {
			System.out.println(event.getCreateTime());
		}
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class Event {
		private Long createTime;
	}
}
