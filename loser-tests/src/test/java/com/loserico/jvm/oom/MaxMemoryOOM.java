package com.loserico.jvm.oom;

import java.util.ArrayList;
import java.util.List;

public class MaxMemoryOOM {

	private byte[] data = new byte[1024 * 1024];

	public static void main(String[] args) {
		List<MaxMemoryOOM> list = new ArrayList<>();

		int count = 0;
		try {
			while (true) {
				count++;
				list.add(new MaxMemoryOOM());
			}
		} catch (Error e) {
			System.out.println("count=" + count);
			e.printStackTrace();
		}
	}
}
