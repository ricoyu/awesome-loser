package com.loserico.jvm.oom;

import java.util.ArrayList;
import java.util.List;

public class HeapOOM {

	static class OOMObject {
	}

	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<>();
		while (true) {
			list.add(new OOMObject());
		}
	}
}