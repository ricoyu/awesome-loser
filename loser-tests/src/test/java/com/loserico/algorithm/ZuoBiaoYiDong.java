package com.loserico.algorithm;

import java.util.Scanner;

public class ZuoBiaoYiDong {
	
	static Scanner scanner = new Scanner(System.in);
	
	/**
	 * 坐标移动
     * 
     * 题目描述
     * 开发一个坐标计算工具， A表示向左移动，D表示向右移动，W表示向上移动，S表示向下移动。从（0,0）点开始移动，从输入字符串里面读取一些坐标，并将最终输入结果输出到输出文件里面。
     *
     * 输入：
     * 合法坐标为A(或者D或者W或者S) + 数字（两位以内）
     * 坐标之间以;分隔。
     * 非法坐标点需要进行丢弃。如AA10;  A1A;  $%$;  YAD; 等。
     * 下面是一个简单的例子 如：
     * A10;S20;W10;D30;X;A1A;B10A11;;A10;
     *
     * 处理过程：
     * 起点（0,0）
     * +   A10   =  （-10,0）
     * +   S20   =  (-10,-20)
     * +   W10  =  (-10,-10)
     * +   D30  =  (20,-10)
     * +   x    =  无效
     * +   A1A   =  无效
     * +   B10A11   =  无效
     * +  一个空 不影响
     * +   A10  =  (10,-10)
     * 结果 （10， -10）
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		while (scanner.hasNext()) {
			String s = scanner.nextLine();
			String[] strs = s.split(";");
			place now = new place(0, 0);
			for (int i = 0; i < strs.length; i++) {
				if (judgeStr(strs[i])) {
					char ch = strs[i].charAt(0);
					int a = Integer.parseInt(strs[i].substring(1));
					if (ch == 'A') {
						now.x -= a;
					} else if (ch == 'D') {
						now.x += a;
					} else if (ch == 'W') {
						now.y += a;
					} else if (ch == 'S') {
						now.y -= a;
					}
				}
			}
			System.out.println(now.x + "," + now.y);
		}
	}
	
	private static boolean judgeStr(String str) {
		if (str.length() < 2 || str.length() > 3) {
			return false;
		}
		String num = str.substring(1);
		if (isNum(num)) {
			char c = str.charAt(0);
			if (c == 'A' || c == 'D' || c == 'W' || c == 'S') {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isNum(String num) {
		try {
			Integer a = Integer.parseInt(num);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	static class place {//定义位置对象(含，x,y)
		int x;
		int y;
		
		public place(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}