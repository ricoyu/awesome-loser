package com.loserico.jvm;

public class MaxMemory {
	
	public static void main(String[] args) {
		Runtime runtime = Runtime.getRuntime();
		long totalMem = runtime.totalMemory();
		long maxMem = runtime.maxMemory();
		long freeMem = runtime.freeMemory();
		double megs = 1048576.0;

		System.out.println("Total Memory(Xms): " + totalMem + " (" + (totalMem / megs) + " MiB)");
		System.out.println("Max Memory(Xmx):   " + maxMem + " (" + (maxMem / megs) + " MiB)");
		System.out.println("Free Memory:  " + freeMem + " (" + (freeMem / megs) + " MiB)");
	}
}