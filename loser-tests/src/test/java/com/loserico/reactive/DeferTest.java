package com.loserico.reactive;

import org.junit.Test;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

public class DeferTest {

	/*
	 * Now create an instance of Person , two Observables to be notified with age
	 * and name values, and set the values for age and name
	 * 
	 * What happens when you call methods observeName() and observeAge() on an
	 * instance of Person ? What will be the sequence emitted by the observables?
	 * Unfortunately, the output will be 
	 * 		age is: 0 
	 * 		name is: null
	 * 
	 * This is not what you wanted.
	 * 
	 * The problem here is that Observable.just() is evaluated as soon as it's
	 * invoked, so it will create a sequence using the exact value that name and age
	 * reference when the observable is created.
	 * 
	 * In the example, when the observable is created, age is 0 and name is null.
	 * 
	 * You'd like to have a little different behavior here: you want the values to
	 * be evaluted when you subscribe to the observables, and you can do this using
	 * Observable.defer()
	 * @on
	 */
	@Test
	public void testJust() {
		Person person = new Person();

		Observable<String> nameObservable = Observable.just(person.getName());
		Observable<Integer> ageObservable = Observable.just(person.getAge());

		person.setName("俞雪华");
		person.setAge(36);

		ageObservable.subscribe(new Subscriber<Integer>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
			}

			@Override
			public void onNext(Integer age) {
				System.out.println("age is: " + age);
			}

		});

		nameObservable.subscribe(new Subscriber<String>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
			}

			@Override
			public void onNext(String name) {
				System.out.println("name is: " + name);
			}

		});
	}

	/*
	 * Observable.defer() accepts an instance of Func0<Observable<T>> as parameter.
	 * Func0<R> is an interface that exposes just one method that accepts zero
	 * arguments and returns a value of type R . There is also a Func1<T,R>
	 * interface that exposes just one method that accepts one argument of type T
	 * and returns a value of type R . RxJava provides similar interfaces for up to
	 * nine arguments ( Func9<T,R> ) and a varagrs version ( FuncN<R> ).
	 * 
	 * By using these two observables, the output of the previous examples becomes
	 * age is: 35 name is: Bob
	 */
	@Test
	public void testDefer() {
		Person person = new Person();

		Observable<String> nameObservable = Observable.defer(new Func0<Observable<String>>() {

			@Override
			public Observable<String> call() {
				return Observable.just(person.getName());
			}

		});
		Observable<Integer> ageObservable = Observable.defer(new Func0<Observable<Integer>>() {

			@Override
			public Observable<Integer> call() {
				return Observable.just(person.getAge());
			}

		});

		person.setName("俞雪华");
		person.setAge(36);

		ageObservable.subscribe(new Subscriber<Integer>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
			}

			@Override
			public void onNext(Integer age) {
				System.out.println("age is: " + age);
			}

		});

		nameObservable.subscribe(new Subscriber<String>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
			}

			@Override
			public void onNext(String name) {
				System.out.println("name is: " + name);
			}

		});
	}

	class Person {
		private String name;
		private int age;

		public void setAge(int age) {
			this.age = age;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public String getName() {
			return name;
		}
	}
}
