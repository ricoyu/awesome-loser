package com.loserico.algorithm.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * O(1) 时间插入、删除和获取随机元素
 * <p/>
 * 实现RandomizedSet 类：
 * <p/>
 * RandomizedSet() 初始化 RandomizedSet 对象 <br/>
 * bool insert(int val) 当元素 val 不存在时，向集合中插入该项，并返回 true ；否则，返回 false 。 <br/>
 * bool remove(int val) 当元素 val 存在时，从集合中移除该项，并返回 true ；否则，返回 false 。 <br/>
 * int getRandom() 随机返回现有集合中的一项（测试用例保证调用此方法时集合中至少存在一个元素）。每个元素应该有 相同的概率 被返回。 <br/>
 * 你必须实现类的所有函数，并满足每个函数的 平均 时间复杂度为 O(1) 。 <br/>
 * <p/>
 * 示例：
 * <p/>
 * 输入
 * ["RandomizedSet", "insert", "remove", "insert", "getRandom", "remove", "insert", "getRandom"] <br/>
 * [[], [1], [2], [2], [], [1], [2], []]
 * 输出
 * [null, true, false, true, 2, true, false, 2]
 * <p/>
 * 解释
 * RandomizedSet randomizedSet = new RandomizedSet(); <br/>
 * randomizedSet.insert(1); // 向集合中插入 1 。返回 true 表示 1 被成功地插入。 <br/>
 * randomizedSet.remove(2); // 返回 false ，表示集合中不存在 2 。 <br/>
 * randomizedSet.insert(2); // 向集合中插入 2 。返回 true 。集合现在包含 [1,2] 。 <br/>
 * randomizedSet.getRandom(); // getRandom 应随机返回 1 或 2 。 <br/>
 * randomizedSet.remove(1); // 从集合中移除 1 ，返回 true 。集合现在包含 [2] 。 <br/>
 * randomizedSet.insert(2); // 2 已在集合中，所以返回 false 。
 * randomizedSet.getRandom(); // 由于 2 是集合中唯一的数字，getRandom 总是返回 2 。 <br/>
 * <p>
 * 这个问题需要设计一个可以在O(1) 时间内完成插入、删除和获取随机元素的集合。我们可以使用一个 ArrayList 和一个 HashMap 来解决这个问题。具体思路如下：
 *
 * <ol>数据结构选择：
 *     <li/>ArrayList：用于存储插入的元素。这样我们可以在 O(1) 时间内通过索引访问随机元素。
 *     <li/>HashMap：用于存储每个元素对应的索引，方便我们在 O(1) 时间内判断某个元素是否存在，同时能快速删除指定元素。
 * </ol>
 *
 * <ol>操作设计：
 *     <li/>插入元素：检查 HashMap 中是否已存在该元素，若不存在则将其添加到 ArrayList 末尾，并在 HashMap 中记录该元素及其索引。
 *     <li/>删除元素：先从 HashMap 获取待删除元素的索引，然后将 ArrayList 中最后一个元素移到待删除元素的位置，更新 HashMap 中最后一个元素的索引，
 *          最后移除 ArrayList 末尾元素并从 HashMap 中删除目标元素。
 *     <li/>获取随机元素：直接从 ArrayList 中随机选取一个元素即可。
 * </ol>
 * <p/>
 * Copyright: Copyright (c) 2024-11-11 8:55
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RandomizedSet {

	// 用于存储元素的列表
	private List<Integer> list;
	// 用于记录每个元素的索引，方便快速定位和删除
	private Map<Integer, Integer> map;
	// 随机数生成器，用于随机返回集合中的元素
	private Random random;

	public RandomizedSet() {
		list = new ArrayList<>();
		map = new HashMap<>();
		random = new Random();
	}

	public boolean insert(int val) {
		if (map.containsKey(val)) {
			return false;
		}
		map.put(val, list.size());
		list.add(val);
		return true;
	}

	public boolean remove(int val) {
		if (!map.containsKey(val)) {
			return false;
		}
		// 获取要删除元素的索引
		Integer index = map.get(val);
		// 获取列表中的最后一个元素
		Integer lastElem = list.get(list.size() - 1);
		// 将最后一个元素移动到要删除元素的位置
		list.set(index, lastElem);
		// 更新最后一个元素的索引
		map.put(lastElem, index);
		// 从列表末尾删除最后一个元素
		list.remove(list.size() - 1);
		// 从映射中删除目标元素
		map.remove(val);
		return true;
	}

	public int getRandom() {
		// 在列表中随机选取一个索引并返回对应的元素
		int index = random.nextInt(list.size());
		return list.get(index);
	}

}
