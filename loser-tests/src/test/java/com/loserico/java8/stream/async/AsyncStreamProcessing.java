package com.loserico.java8.stream.async;

import com.loserico.java8.stream.async.model.Client;
import com.loserico.java8.stream.async.service.ServiceInvoker;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;

public class AsyncStreamProcessing {
	private final ServiceInvoker serviceInvoker;

	public AsyncStreamProcessing() {
		this.serviceInvoker = new ServiceInvoker();
	}

	public static void main(String[] args) {
		new AsyncStreamProcessing().start();
	}

	private void start() {
		List<String> ids = Arrays.asList(
				"C01", "C02", "C03", "C04", "C05", "C06", "C07", "C08", "C09", "C10",
				"C11", "C12", "C13", "C14", "C15", "C16", "C17", "C18", "C19", "C20");

		long startTime = System.nanoTime();
		List<CompletableFuture<Client>> futureRequests = ids.stream()
				.map(id -> CompletableFuture.supplyAsync(() -> serviceInvoker.invoke(id)))
				.collect(toList());

		double totalPurchases = futureRequests.stream()
				.map(CompletableFuture::join)
				.collect(summingDouble(Client::getPurchases));

		long endTime = (System.nanoTime() - startTime) / 1_000_000;
		System.out.println("Async | Total time: " + endTime + " ms");
		System.out.println("Total purchases: " + totalPurchases);
	}

}