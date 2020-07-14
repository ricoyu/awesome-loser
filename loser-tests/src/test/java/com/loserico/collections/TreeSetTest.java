package com.loserico.collections;

import org.junit.Test;

import java.util.TreeSet;

public class TreeSetTest {

	@Test
	public void testOrdered() {
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.add("b");
		treeSet.add("a");
		treeSet.add("c");
		
		for (String string : treeSet) {
			System.out.println(string);
		}
		
		TreeSet<Integer> treeSet2 = new TreeSet<Integer>();
		treeSet2.add(1);
		treeSet2.add(3);
		treeSet2.add(2);
		for (Integer integer : treeSet2) {
			System.out.println(integer);
		}
	}
}
