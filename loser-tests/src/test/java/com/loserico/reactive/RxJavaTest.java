package com.loserico.reactive;

import org.junit.Test;

import rx.Observable;

/**
 * Reactive Extensions (Rx)
 * <p>
 * Functional Reactive Concepts
 * <ul>
 * <li>On one side, functional programming is the process of building software
 * by composing pure functions, avoiding shared state, mutable data, and side-effects.
 * <li>On the other side, reactive programming is an asynchronous programming
 * paradigm concerned with data streams and the propagation of change.
 * <p>
 * 
 * Together, functional reactive programming forms a combination of functional
 * and reactive techniques that can represent an elegant approach to
 * event-driven programming – with values that change over time and where the
 * consumer reacts to the data as it comes in.
 * 
 * Copyright: Copyright (c) 2018-11-22 14:22
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class RxJavaTest {

	/*
	 * Generally, an Observable is going to be asynchronous, but it doesn’t need to
	 * be. An Observable can be synchronous, and in fact defaults to being
	 * synchronous. RxJava never adds concurrency unless it is asked to do so. A
	 * synchronous Observable would be subscribed to, emit all data using the
	 * subscriber’s thread, and complete (if finite).
	 * 
	 * An Observable backed by blocking network I/O would synchronously block the
	 * subscribing thread and then emit via onNext() when the blocking network I/O
	 * returned.
	 */
	@Test
	public void testObservable() {
		Observable.create((s) -> {
			s.onNext("Hello World!");
			s.onCompleted();
		}).subscribe(System.out::println);
	}

	@Test
	public void testSynchronousComputation() {
		Observable<Integer> o = Observable.create((s) -> {
			s.onNext(1);
			s.onNext(2);
			s.onNext(3);
			s.onCompleted();
		});

		o.map((i) -> "Number " + i)
				.subscribe(System.out::println);
	}

}
