package com.loserico.reactive;

import static java.util.Arrays.asList;

import org.junit.Test;

import rx.Observable;

public class CreateObservableTest {

	/*
	 * You use the Observable just() and from() methods to convert objects, lists,
	 * or arrays of objects into Observables that emit those objects:
	 * 
	 * These converted Observables will synchronously invoke the onNext() method of
	 * any subscriber that subscribes to them, for each item to be emitted by the
	 * Observable, and will then invoke the subscriber’s onCompleted() method.
	 */
	@Test
	public void testFromExistingDataStructure() {
		Observable<String> o = Observable.from(new String[] { "a", "b", "c" });
		Observable<String> o2 = Observable.from(asList("a", "b", "c", "d"));
		Observable<String> o3 = Observable.just("One Object");
		Observable<String> o4 = Observable.just("a", "b", "c");
	}
}
