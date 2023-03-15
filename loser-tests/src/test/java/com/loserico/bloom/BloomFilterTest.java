package com.loserico.bloom;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

/**
 * https://www.baeldung.com/guava-bloom-filter
 *
 * <p>
 * Copyright: (C), 2021-02-20 16:12
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BloomFilterTest {
	
	/**
	 * Bloom Filter说这个element存在, 那么它基本就是存在的, 但也不是100%正确
	 * 下面示例Bloom Filter有1%的可能说某个element存在, 但实际不存在
	 * <p>
	 * Suppose we want to create a Bloom filter for up to 500 Integers and
	 * that we can tolerate a one-percent (0.01) probability of false positives.
	 * <p>
	 * false positives
	 * positive是正数, false是错误的, 那么合起来的意思就是: Bloom确认:"该元素存在!" -- 这是positive; 但实际不存在 -- 这叫false
	 */
	@Test
	public void test1PercentFalsePositive() {
		BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(),
				500, //期望的元素数量, 要尽可能提供准确的元素数量, 否则将会产生较高的误报率。
				0.01); //误报率
		
		filter.put(1); //向过滤器插入元素
		filter.put(2);
		filter.put(3);
		
		assertThat(filter.mightContain(1)).isTrue(); //测试1是否存在于布隆过滤器中
		assertThat(filter.mightContain(2)).isTrue();
		assertThat(filter.mightContain(3)).isTrue();
		assertThat(filter.mightContain(4)).isFalse();
		
		for (int j = 0; j < 1000; j++) {
			for (int i = 5; i < 500; i++) {
				assertThat(filter.mightContain(i)).isFalse();
			}
		}
	}
	
	@Test
	public void testFlasePositive() {
		/*
		 * 容量设计为5, 1%的false positive
		 * Because the expected number of elements is so small, the filter will occupy very little memory.
		 */
		BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(),
				5,
				0.01);
		IntStream.range(0, 100_000).forEach(filter::put);
		
		/*
		 * However, as we add more items than expected, 
		 * the filter becomes over-saturated and has a much higher probability 
		 * of returning false positive results than the desired one percent:
		 */
		assertThat(filter.mightContain(1)).isTrue();
		assertThat(filter.mightContain(2)).isTrue();
		assertThat(filter.mightContain(3)).isTrue();
		assertThat(filter.mightContain(4)).isTrue();
		assertThat(filter.mightContain(10_000)).isTrue();
		assertThat(filter.mightContain(1_000_000)).isTrue(); //这个实际不存在, 但Bloom Filter极有可能说它存在
	}
}
