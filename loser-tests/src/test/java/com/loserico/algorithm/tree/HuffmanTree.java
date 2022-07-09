package com.loserico.algorithm.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * <p>
 * Copyright: (C), 2022-07-08 15:56
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HuffmanTree {
	
	HfmNode root;
	List<HfmNode> leafs; //叶子节点
	Map<Character, Integer> weights; //叶子节点的权重
	
	public HuffmanTree(Map<Character, Integer> weights) {
		this.weights = weights;
		leafs = new ArrayList<>();
	}
	
	public void createTree() {
		Character[] keys = weights.keySet().toArray(new Character[0]);
		PriorityQueue<HfmNode> priorityQueue = new PriorityQueue<>();
		for (Character key : keys) {
			HfmNode hfmNode = new HfmNode();
			hfmNode.chars = key.toString();
			hfmNode.fre = weights.get(key);
			priorityQueue.add(hfmNode);
			leafs.add(hfmNode);
		}
		
		int length = priorityQueue.size();
		for (int i = 1; i < length - 1; i++) { // 每次找最小的两个点合并
			HfmNode n1 = priorityQueue.poll();
			HfmNode n2 = priorityQueue.poll();// 每次取优先队列的前面两个 就一定是两个最小的
			
			HfmNode newNode = new HfmNode();
			newNode.chars = n1.chars + n2.chars;
			newNode.fre = n1.fre + n2.fre;
			
			// 维护出树的结构
			newNode.left = n1;
			newNode.right = n2;
			n1.parent = newNode;
			n2.parent = newNode;
			
			priorityQueue.add(newNode);
		}
		root = priorityQueue.poll();// 最后这个点就是我们的根节点
		System.out.println("构建完成");
	}
	
	/**
	 * 对叶子节点进行编码
	 *
	 * @return
	 */
	public Map<Character, String> code() {
		Map<Character, String> map = new HashMap<>();
		for (HfmNode node : leafs) {
			String code = "";
			Character c = node.chars.charAt(0);// 叶子节点肯定只有一个字符
			HfmNode current = node;
			do {
				if (current.parent != null && current == current.parent.left) {
					code = "0" + code;
				} else {
					code = "1" + code;
				}
				current = current.parent;
			} while (current!= null && current.parent != null);
			map.put(c, code);
			System.out.println(c + ":" + code);
		}
		return map;
	}
	
	public static void main(String[] args) {
		// a:3 b:24 c:6 d:20 e:34 f:4 g:12
		Map<Character, Integer> weights = new HashMap<Character, Integer>();
		//一般来说：动态的加密，最开始是不知道里面有什么内容的。我们需要一个密码本，往往就是某个字典。如果是英文就用英文字典，统计次数。
		//换密码本
		//静态的文件。针对性的做编码.图像加密,没有特性的。hash加密（MD5）
		weights.put('a', 3);
		weights.put('b', 24);
		weights.put('c', 6);
		weights.put('d', 1);
		weights.put('e', 34);
		weights.put('f', 4);
		weights.put('g', 12);
		
		HuffmanTree huffmenTree = new HuffmanTree(weights);
		huffmenTree.createTree();
		Map<Character, String> code = huffmenTree.code();
		String str = "aceg";
		System.out.println("编码后的：");
		char s[] = str.toCharArray();
	}
}
