package com.loserico.algorithm.bplustree;

/**
 * Searching on a B+ tree
 * 
 * <p>
 * Copyright: (C), 2020-10-15 17:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BPlusTree {
	
	int m;
	
	public class Node {
		InternalNode parent;
	}
	
	private class InternalNode extends Node {
		int maxDegree;
		int minDegree;
		
		/**
		 * 一个节点含有的子树的个数称为该节点的度
		 */
		int degree;
		InternalNode leftSibling;
		InternalNode rightSibling;
		Integer[] keys;
		Node[] childPointers;
		
		/**
		 * 添加孩子节点, childPointers尾部插入
		 * @param pointer
		 */
		private void appendChildPointer(Node pointer) {
			this.childPointers[degree] = pointer;
			//节点的度+1
			this.degree++;
		}
		
		/**
		 * 将子节点加入到指定位置, 该位置及后面的节点都后移一位
		 * @param pointer
		 * @param index
		 */
		private void insertChildPointer(Node pointer, int index) {
			for (int i = degree -1; i >= index ; i--) {
				childPointers[i + 1] = childPointers[i];
			}
			
			this.childPointers[index] = pointer;
			this.degree++;
		}
		
		/**
		 * 添加孩子节点, childPointers头部插入
		 */
		private void prependChildPointer() {
			for (int i = degree - 1; i >= 0 ; i++) {
				
			}
		}
		
		private int findIndexOfPointer(Node pointer) {
			for (int i = 0; i < childPointers.length; i++) {
				if (childPointers[i] == pointer) {
					return i;
				}
			}
			
			return -1;
		}
		
		private boolean isDeficient() {
			return this.degree < this.minDegree;
		}
		
		private boolean isLendable() {
			return this.degree > this.minDegree;
		}
		
		private boolean isMergeable() {
			return this.degree == this.minDegree;
		}
		
		private boolean isOverfull() {
			return this.degree == this.maxDegree + 1;
		}
	}
	
}
