package com.loserico.jvm.herong;

class GarbageCollection2 {
	
	public static void main(String[] args) {
		java.util.Random rnd = new java.util.Random();
		int max = 10000;
		int min = 32;
		Object[] arr = new Object[min];
		Runtime rt = Runtime.getRuntime();
		System.out.println("Step/TotalMemory/FreeMemory/UsedMemory:");
		for (int m = 0; m < max; m++) {
			int k = rnd.nextInt(min);
			for (int n = min - 1; n >= k + 1; n--) {
				arr[n] = arr[n - 1];
			}
			arr[k] = getOneMega();
			long total = rt.totalMemory();
			long free = rt.freeMemory();
			System.out.println((m + 1) + "   " + total + "   " + free + "   " + (total - free));
		}
	}
	
	private static Object getOneMega() {
		return new long[1024 * 128]; // 1MB
	}
}