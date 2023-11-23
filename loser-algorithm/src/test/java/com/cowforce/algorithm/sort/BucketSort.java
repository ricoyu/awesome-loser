package com.cowforce.algorithm.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * 桶排序
 * <p>
 * Copyright: (C), 2022-12-15 12:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BucketSort {
	
	/**
	 * @param array     待排序的数组
	 * @param bucketcap 桶的容量
	 * @return
	 */
	public static List<Integer> sort(List<Integer> array, int bucketcap) {
		if (array == null || array.size() < 2) {
			return array;
		}
		//找到数组中最大, 最小值
		int max = array.get(0), min = array.get(0);
		for (Integer value : array) {
			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}
		}
		
		//获得桶的数量
		int bucketCount = (max - min) / bucketcap +1;
		
		//构建桶
		List<List<Integer>> buckets = new ArrayList<>(bucketCount);
		List<Integer> results = new ArrayList<>();
		for (int i = 0; i < bucketCount; i++) {
			buckets.add(new ArrayList<>());
		}
		
		//将原始数组中的数据分配到桶中
		for (int i = 0; i < array.size(); i++) {
			Integer value = array.get(i);
			buckets.get((value - min) / bucketcap).add(value);
		}
		
		//打印一下桶中数据的分布
		for (int i = 0; i < buckets.size(); i++) {
			System.out.print("第"+i+"个桶包含数据：");
			PrintArray.printObject(buckets.get(i));
		}
		
		for (int i = 0; i < buckets.size(); i++) {
			if (bucketcap == 1) {
				List<Integer> bucket = buckets.get(i);
				results.add(bucket.get(0));
			} else {
				if (bucketCount == 1) {
					bucketcap--;
				}
					System.out.println("对第"+i+"桶中的数据再次用桶进行排序----");
					List<Integer> tempResults = sort(buckets.get(i), bucketcap);
					for (Integer value : tempResults) {
						results.add(value);
					}
			}
		}
		
		return results;
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> array = new ArrayList<>();
		array.add(86);
		array.add(11);
		array.add(77);
		array.add(23);
		array.add(32);
		array.add(45);
		array.add(58);
		array.add(63);
		array.add(93);
		array.add(4);
		array.add(37);
		array.add(22);
		PrintArray.printObject(array);
		System.out.println("============================================");
		List<Integer> dest = BucketSort.sort(array,2);
		System.out.println("排序后的数组");
		PrintArray.printObject(dest);
	}
}
