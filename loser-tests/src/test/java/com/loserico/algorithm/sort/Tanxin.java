package com.loserico.algorithm.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 贪心算法
 * <p>
 * Copyright: (C), 2022-07-05 13:59
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Tanxin {
	
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		List<Meeting> meetings = new ArrayList<>();
		int n = cin.nextInt();
		for (int i = 0; i < n; i++) {
			int start = cin.nextInt();
			int end = cin.nextInt();
			Meeting meeting = new Meeting(i+1, start, end);
			meetings.add(meeting);
		}
		meetings.sort(null);
		int curtime = 0; //当前时间, 从一天的0点开始
		for (int i = 0; i < n; i++) {
			Meeting meeting = meetings.get(i);
			if (meeting.startTime >- curtime) {
				System.out.println(meeting.toString());
				curtime = meeting.endtime;
			}
		}
	}
}

class Meeting implements Comparable<Meeting> {
	int metNum; //会议编号
	int startTime; //开始时间
	int endtime; //结束时间
	
	public Meeting(int metNum, int startTime, int endtime) {
		this.metNum = metNum;
		this.startTime = startTime;
		this.endtime = endtime;
	}
	
	@Override
	public int compareTo(Meeting o) {
		if(this.endtime > o.endtime) {
			return 1;
		} else {
			return -1;
		}
	}
}
