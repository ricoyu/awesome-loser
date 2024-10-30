package com.loserico.algorithm.huawei_exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.nextLine();//清理缓存

		List<CheckRecord> records = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			String input = scanner.nextLine().trim();
			String[] parts = input.split(",");
			int staffId = Integer.parseInt(parts[0]);
			int recordTime = Integer.parseInt(parts[1]);
			int distince = Integer.parseInt(parts[2]);
			String actualDevNo = parts[3];
			String registDevNo = parts[4];

			CheckRecord record = new CheckRecord(staffId, recordTime,
					distince,
					actualDevNo,
					registDevNo);
			records.add(record);
		}

		scanner.close();

		List<CheckRecord> invalidRecords = findInvalidRecord(records);
		if (invalidRecords.isEmpty()) {
			System.out.println("null");
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < invalidRecords.size(); i++) {
			CheckRecord checkRecord = invalidRecords.get(i);
			sb.append(checkRecord.toString());
			if (i!= n-1) {
				sb.append(";");
			}
		}
		System.out.println(sb.toString());
	}

	public static List<CheckRecord> findInvalidRecord(List<CheckRecord> records) {
		List<CheckRecord> invalidRecords = new ArrayList<>();
		int n = records.size();
		for (int i = 0; i < n - 1; i++) {
			CheckRecord record1 = records.get(i);
			CheckRecord record2 = records.get(i + 1);
			if (!record1.isDevNoMatch()) {
				invalidRecords.add(record1);
			}
			if (!record2.isDevNoMatch()) {
				invalidRecords.add(record2);
			}

			if (!record1.isValid(record2)) {
				if (!invalidRecords.contains(record1)) {
					invalidRecords.add(record1);
				}
				if (!invalidRecords.contains(record2)) {
					invalidRecords.add(record2);
				}
			}
		}

		return invalidRecords;
	}
}

class CheckRecord {
	int staffId; //工号

	int recordTime; //打卡卡时间

	int distance; //打卡距离

	String actualDevNo; //实际设备号

	String registDevNo; //注册设备号

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		CheckRecord that = (CheckRecord) obj;
		return this.staffId == that.staffId && this.recordTime == that.recordTime;
	}

	public CheckRecord(int staffId, int recordTime,
	                   int distance,
	                   String actualDevNo,
	                   String registDevNo) {
		this.staffId = staffId;
		this.recordTime = recordTime;
		this.distance = distance;
		this.actualDevNo = actualDevNo;
		this.registDevNo = registDevNo;
	}

	/**
	 * 注册设备号与实际设备号是否一致
	 *
	 * @return
	 */
	public boolean isDevNoMatch() {
		return actualDevNo.equals(registDevNo);
	}

	public boolean isValid(CheckRecord record) {
		//同一个员工的两次打卡时间不能小于60分钟, 距离不能超过5公里
		if (this.staffId == record.staffId &&
				(Math.abs(record.recordTime - this.recordTime) < 60 || Math.abs(record.distance - this.distance) > 5)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.staffId+","+this.recordTime+","+this.distance+","+this.actualDevNo+","+this.registDevNo;
	}
}
