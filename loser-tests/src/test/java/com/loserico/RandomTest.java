package com.loserico;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class RandomTest {

	@Test
	public void testRandom() {
		String[] staffs = new String[] { "俞雪华", "王树广", "施祯祯", "陈建波", "阮同亮", "窦新超", "吴凯" };
		for (int i = 0; i < 7; i++) {
			System.out.println(staffs[new Random().nextInt(7)]);
		}
	}

	public static float randomWH(Integer x) {
		int[] seed = new int[3];
		seed[0] = (171 * x) % 30269;
		seed[1] = (172 * (30000 - x)) % 30307;
		seed[2] = (170 * x) % 30323;
		return (x / Math.abs(x))
				* (seed[0] / 30269.0F + seed[1] / 30307.0F + seed[2] / 30323.0F) % 1.0F;
	}

	@Test
	public void testRandomWH() {
		System.out.println(randomWH(3));
	}

	@Test
	public void testName() {
		System.out.println("\n".length());
	}

	/**
	 * This is a method of basic random generator.
	 * 
	 * @param x A seed for generator.
	 * @return A float random value between [0.0,1.0)
	 */
	public static float randomBasic(Integer x) {
		x = (x << 13) ^ x;
		return (float) Math
				.abs((1.0 - ((x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0));
	}
	
	@Test
	public void testRandomBasic() {
		System.out.println(randomBasic(30));
	}
}
