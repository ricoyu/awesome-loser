package com.loserico.algorithm;

import java.util.Scanner;

public class ShiBieYouXiaoIp {
	
	private static int a = 0;
	private static int b = 0;
	private static int c = 0;
	private static int d = 0;
	private static int e = 0;
	private static int errors = 0;
	private static int privateIP = 0;
	
	/**
	 * 识别有效的IP地址和掩码并进行分类统计
	 *
	 * 题目描述
	 * 请解析IP地址和对应的掩码，进行分类识别。要求按照A/B/C/D/E类地址归类，不合法的地址和掩码单独归类。
	 *
	 * 所有的IP地址划分为 A,B,C,D,E五类
	 * A类地址1.0.0.0~126.255.255.255;
	 * B类地址128.0.0.0~191.255.255.255;
	 * C类地址192.0.0.0~223.255.255.255;
	 * D类地址224.0.0.0~239.255.255.255；
	 * E类地址240.0.0.0~255.255.255.255
	 *
	 * 私网IP范围是：
	 * 10.0.0.0～10.255.255.255
	 * 172.16.0.0～172.31.255.255
	 * 192.168.0.0～192.168.255.255
	 *
	 * 子网掩码为二进制下前面是连续的1，然后全是0。（例如：255.255.255.32就是一个非法的掩码）
	 * 注意二进制下全是1或者全是0均为非法
	 *
	 * 注意：
	 * 1. 类似于【0.*.*.*】的IP地址不属于上述输入的任意一类，也不属于不合法ip地址，计数时可以忽略
	 * 2. 私有IP地址和A,B,C,D,E类地址是不冲突的
	 *
	 * 输入描述:
	 * 多行字符串。每行一个IP地址和掩码，用~隔开。
	 *
	 * 输出描述:
	 * 统计A、B、C、D、E、错误IP地址或错误掩码、私有IP的个数，之间以空格隔开。
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String str = sc.next();
			checkIp(str);
		}
		System.out.println(a + " " + b + " " + c + " " + d + " " + e + " " + errors + " " + privateIP);
		sc.close();
	}
	
	private static void checkIp(String str) {//str:10.70.44.68~255.254.255.0
		String[] s = str.split("~");
		
		String[] ipStr = s[0].split("\\.");//获得IP:  10/70/44/68
		String[] maskStr = s[1].split("\\.");//获得掩码：255.254.255.0
		
		//****1 注意!!!错误IP地址或错误掩码的判断一定要放在最前面判断，因为判断出了是错误IP地址或错误掩码后
		//就直接return,不用判断是不是后面的ABCDE类或者私有IP  *****
		
		//1-1 错误IP
		if (ipStr.length != 4 || maskStr.length != 4) {
			errors++;//错误IP
			return;
		}
		// 判断IP中的每一部分格式
		for (int i = 0; i < 4; i++) {
			if (" ".equals(ipStr[i]) || " ".equals(maskStr[i])) {
				errors++;
				return;
			}
		}
		
		int[] ipInts = new int[4];
		int[] maskInts = new int[4];
		// 将IP解析成数字
		for (int i = 0; i < 4; i++) {
			ipInts[i] = Integer.parseInt(ipStr[i]);
			maskInts[i] = Integer.parseInt(maskStr[i]);
			if (ipInts[i] > 255 || maskInts[i] > 255) {
				errors++;
				return;
			}
		}
		//1-2   错误掩码:255.255.255.32  必须是前面全为1，后面全为0，即从后往前至少有一位是0，且0前的数字全为1
		//即首先得到第一个0的位置，再判断0后是否有1，有即为错误掩码
		
		//将掩码转为为二进制：好判断0，1位置
		String tempStr = "";
		//例如将255.254.255.0转换成二进制：
		//255:1111 1111
		//254:1111 1110
		//255:1111 1111
		//	0:0
		for (int i = 0; i < maskInts.length; i++) {
			String str2 = Integer.toBinaryString(maskInts[i]);
			//判断一下：如果转化为二进制为0或者1或者不满8位，要在数后补0
			int bit = 8 - str2.length();
			if (str2.length() < 8) {
				for (int j = 0; j < bit; j++) {
					str2 = "0" + str2;
				}
			}
			tempStr += str2;
		}
		
		int indexOfFirstZero = tempStr.indexOf("0");
		tempStr = tempStr.substring(indexOfFirstZero + 1);
		
		if (tempStr.contains("1")) {
			errors++;
			return;
		}
		
		//获得IP地址第一部分数字 
		int ipFirstPart = Integer.parseInt(ipStr[0]);
		int ipSecondPart = Integer.parseInt(ipStr[1]);
		
		
		//3  判断ABCDE 类IP 地址
		if (ipFirstPart >= 1 && ipFirstPart <= 126) {
			a++;
			//2 判断私有IP个数,私有IP和ABCDE 类IP地址判断不冲突，即IP是私有IP一类，也可以是ABCDE一类
			if (ipFirstPart == 10) {
				privateIP++;
			}
		}
		if (ipFirstPart >= 128 && ipFirstPart <= 191) {
			b++;
			//2 判断私有IP个数
			if (ipFirstPart == 172) {
				if (ipSecondPart >= 16 && ipSecondPart <= 31) {
					privateIP++;
				}
			}
		}
		if (ipFirstPart >= 192 && ipFirstPart <= 223) {
			c++;
			//2 判断私有IP个数
			if (ipFirstPart == 192) {
				if (ipSecondPart == 168) {
					privateIP++;
				}
			}
		}
		if (ipFirstPart >= 224 && ipFirstPart <= 239) {
			d++;
		}
		if (ipFirstPart >= 240 && ipFirstPart <= 255) {
			e++;
		}
		
	}
	
}