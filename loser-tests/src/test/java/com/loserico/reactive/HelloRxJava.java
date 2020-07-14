package com.loserico.reactive;

import rx.Observable;

public class HelloRxJava {
	
	public static void main(String[] args) {
		hello("Ben", "George");
	}

	public static void hello(String... names) {
		Observable.from(names).subscribe((s) -> {
			System.out.println("Hello " + s);
		});
	}
}
