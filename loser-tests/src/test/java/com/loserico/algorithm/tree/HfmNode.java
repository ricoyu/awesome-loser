package com.loserico.algorithm.tree;

/**
 * <p>
 * Copyright: (C), 2022-07-08 15:54
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HfmNode implements Comparable<HfmNode> {
	
	String chars;
	int fre; //表示频率
	HfmNode left;
	HfmNode right;
	HfmNode parent;
	
	@Override
	public int compareTo(HfmNode o) {
		return this.fre - o.fre;
	}
}
