package com.loserico.algorithm;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class HeBingBiaoJiLu {
    
    /**
     * 合并表记录
     * 
     * 数据表记录包含表索引和数值（int范围的整数），请对表索引相同的记录进行合并，即将相同索引的数值进行求和运算，输出按照key值升序进行输出。
     * @param args
     */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			Map<Integer, Integer> map = new TreeMap<Integer, Integer>();// 定义map
			int n = sc.nextInt();
			// 输入n值
			for (int i = 0; i < n; i++) {
				int s = sc.nextInt();
				int value = sc.nextInt();
				// 输入Key和Value值
				
				if (map.containsKey(s)) { // 如果key=s处有值，则用sum替换原值
					map.put(s, map.get(s) + value);
				} else { // 如果无值，直接放入
					map.put(s, value);
				}
			}
			// 迭代输出
			for (Integer key : map.keySet()) {
				System.out.println(key + " " + map.get(key));
			}
		}
	}
}