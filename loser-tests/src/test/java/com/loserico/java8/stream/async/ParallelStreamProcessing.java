package com.loserico.java8.stream.async;

import com.loserico.java8.stream.async.model.Client;
import com.loserico.java8.stream.async.service.ServiceInvoker;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.summingDouble;

public class ParallelStreamProcessing {
	private final ServiceInvoker serviceInvoker;
	
	public ParallelStreamProcessing() {
		this.serviceInvoker = new ServiceInvoker();
	}
	
	public static void main(String[] args) {
		new ParallelStreamProcessing().start();
	}

	private void start() {
		List<String> ids = Arrays.asList(
				"C01", "C02", "C03", "C04", "C05", "C06", "C07", "C08", "C09", "C10", 
				"C11", "C12", "C13", "C14", "C15", "C16", "C17", "C18", "C19", "C20");
		
		long startTime = System.nanoTime();
		double totalPurchases = ids.parallelStream()
			.map(id -> serviceInvoker.invoke(id))
			.collect(summingDouble(Client::getPurchases));
		
		long endTime = (System.nanoTime() - startTime) / 1_000_000;
		System.out.println("Parallel | Total time: " + endTime + " ms");
		System.out.println("Total purchases: " + totalPurchases);
	}
}