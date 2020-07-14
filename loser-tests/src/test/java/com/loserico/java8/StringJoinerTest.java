package com.loserico.java8;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static java.util.stream.Collectors.joining;

public class StringJoinerTest {

	/**
	 * Join String by a delimiter
	 */
	@Test
	public void testStringJoiner() {
		StringJoiner sj = new StringJoiner(",");
		sj.add("aaa");
		sj.add("bbb");
		sj.add("ccc");
		System.out.println(sj.toString());
	}
	
	@Test
	public void testStringJoiner2() {
		String[] arr = new String[] {"sishuok", "mutex", "server1"};
		StringJoiner stringJoiner = new StringJoiner("/");
		stringJoiner.add("");
		for (int i = 0; i < arr.length; i++) {
			String path = arr[i];
			stringJoiner.add(path);
			System.out.println(stringJoiner.toString());
		}
	}

	/**
	 * Join String by a delimiter and starting with a supplied prefix and ending with a supplied suffix.
	 */
	@Test
	public void testStringJoinerWithPrefixSuffix() {
		StringJoiner sj = new StringJoiner("/", "prefix-", "-suffix");
		sj.add("2016");
		sj.add("02");
		sj.add("26");
		System.out.println(sj.toString());
	}

	/**
	 * StringJoiner is used internally by static String.join().
	 */
	@Test
	public void testStringJoin() {
		//2015-10-31
		String result = String.join("-", "2015", "10", "31");
		System.out.println(result);

		List<String> list = Arrays.asList("java", "python", "nodejs", "ruby");
		//java, python, nodejs, ruby
		result = String.join(", ", list);
		System.out.println(result);
	}

	@Test
	public void testCollectorsJoin() {
		List<String> list = Arrays.asList("java", "python", "nodejs", "ruby");
		String result = list.stream().collect(joining(" | "));
		System.out.println(result);
	}

	@Test
	public void testJoinListObject() {
		List<Game> list = Arrays.asList(
				new Game("Dragon Blaze", 5),
				new Game("Angry Bird", 5),
				new Game("Candy Crush", 5));
		String result = list.stream()
				.map(Game::getName)
				.collect(joining(", ", "{", "}"));
		System.out.println(result);
	}

	static class Game {
		String name;
		int ranking;

		public Game(String name, int ranking) {
			this.name = name;
			this.ranking = ranking;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getRanking() {
			return ranking;
		}

		public void setRanking(int ranking) {
			this.ranking = ranking;
		}
	}
}
