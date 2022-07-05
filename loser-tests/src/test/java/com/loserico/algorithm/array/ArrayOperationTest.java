package com.loserico.algorithm.array;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Copyright: (C), 2022-06-26 9:22
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ArrayOperationTest {
	
	private int size; //数组的长度
	
	private int[] data;
	
	private int index; // 当前已经存的数据大小
	
	public ArrayOperationTest(int size) { //数组的初始化过程
		this.size = size;
		data = new int[size]; //分配内存空间
		index = 0;
	}
	
	public void print() {
		log.info("index: " + index);
		for (int i = 0; i < index; i++) {
			log.info(data[i] + " ");
		}
		log.info("");
	}
	
	public void insert(int loc, int n) { // 时间复杂度O(n)
		if (index++ < size) {
			for (int i = size-1 ; i >loc ; i--) {
				data[i] = data[i-1]; //把数据往后移一位
			}
			data[loc] = n;
		} else {
			size = size * 2;
			int temp[] = new int[size];
		}
	}
	
	public void delete(int loc) { //O(n)
		for (int i = loc; i < size; i++) {
			if (i != size -1) { //怕越界所以加一个判断
				data[i] = data[i+1]; 
			} else {
				data[i] = 0; //默认为0, 就是没有存数据
			}
		}
		index--;
	}
	
	public void update(int loc, int n) { //O(1)
		data[loc] = n;
	}
	
	public int get(int loc) { // O(1)
		return data[loc];
	}
}
