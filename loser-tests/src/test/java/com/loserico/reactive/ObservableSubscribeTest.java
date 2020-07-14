package com.loserico.reactive;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.fluent.Response;
import org.junit.Test;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.Subscription;

public class ObservableSubscribeTest {

	@Test
	public void test1() throws InterruptedException {
		Observable<Integer> observable = Observable.from(new Integer[] { 1, 2, 3, 4, 5 });
		Subscription subscription = observable.subscribe(new Subscriber<Integer>() {

			@Override
			public void onStart() {
				System.out.println("start");
			}

			@Override
			public void onCompleted() {
				System.out.println("Sequence completed!");
			}

			@Override
			public void onError(Throwable e) {
				System.err.println("Exception: " + e.getMessage());
			}

			@Override
			public void onNext(Integer integer) {
				System.out.println("next item is: " + integer);
			}
		});

		Thread.currentThread().join();
	}

	@Test
	public void test2() {
		Observable<String> observable = Observable.create(new OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> observer) {
				observer.onNext("Hello!");
				observer.onNext("world!");
				observer.onCompleted();
			}

		});

		observable.subscribe(System.out::println);
	}

	/*
	 * Suppose now that you want to create an observable that emits a JSON string
	 * resulting from a networking operation. If the response is successful, the
	 * observable will emit the result and terminate. Otherwise, it will raise an
	 * error.
	 * 
	 */
	@Test
	public void testObservableResultingFromNetworking() {
		Observable.create(new OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> observer) {
				HttpServletResponse response = null;// 执行网络请求
				if (observer.isUnsubscribed()) {
					// do not emit the item,
					// observer is not subscribed anymore
					return;
				}
				if (response != null && response.isCommitted()) {
					observer.onNext("get response json from response...");
					observer.onCompleted();
				} else {
					observer.onError(new Exception("network call error"));
				}
			}
		});
	}
}
