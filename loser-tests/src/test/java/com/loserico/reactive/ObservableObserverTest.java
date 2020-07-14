package com.loserico.reactive;

import org.junit.Test;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class ObservableObserverTest {

	/**
	 * 模拟电灯开关打开关闭, 电灯亮起暗下
	 */
	@Test
	public void testLightOnOff() {
		// ----------- 被观察者 --------------
		// 这是最正宗的写法，创建了一个开关类，产生了五个事件，分别是：开，关，开，开，结束。
		Observable<String> switcher = Observable.create(new Observable.OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onNext("On");
				subscriber.onNext("Off");
				subscriber.onNext("On");
				subscriber.onNext("On");
				subscriber.onCompleted();
			}

		});

		// 创建了一个台灯类, 常规写法
		Observer<String> light = new Subscriber<String>() {

			@Override
			public void onCompleted() {
				// 被观察者的onCompleted()事件会走到这里;
				System.out.println("结束观察...");
			}

			@Override
			public void onError(Throwable e) {
				System.err.println("出现错误会调用这个方法");
			}

			@Override
			public void onNext(String s) {
				// 处理传过来的onNext事件
				System.out.println("on next: " + s);
			}

		};

		/*
		 * 订阅
		 * 
		 * 建立被观察者和观察者之间的关系
		 * 为什么是开关订阅了台灯？应该是台灯订阅了开关才对啊
		 * 是这样的，台灯观察开关，逻辑是没错的，而且正常来看就应该是light.subscribe(switcher)才对，之所以“开关订阅台灯”，是为了保证流式API调用风格
		 * @on
		 */
		switcher.subscribe(light);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testLightOnOff2() {

		/*
		 * 偷懒模式是一种简便的写法，实际上也都是被观察者把那些信息"On","Off","On","On"，包装成onNext（"On"）这样的事件依次发给观察者，
		 * 当然，它自己补上了onComplete()事件。
		 */
		Observable switcher = Observable.just("On", "Off", "On", "On");

		/*
		 * 偷懒模式(非正式写法)
		 * 
		 * 之所以说它是非正式写法，是因为Action1是一个单纯的人畜无害的接口, 和Observer没有啥关系, 只不过它可以当做观察者来使,
		 * 专门处理onNext 事件, 这是一种为了简便偷懒的写法.
		 * 
		 * 当然还有Action0, Action2, Action3...,0,1,2,3分别表示call()这个方法能接受几个参数。
		 */
		Action1<String> light = new Action1<String>() {

			@Override
			public void call(String s) {
				// 处理传过来的onNext事件
				System.out.println("on next: " + s);
			}

		};

		switcher.subscribe(light);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testLightOnOff3() {

		// 偷懒模式2
		String[] kk = { "On", "Off", "On", "On" };
		Observable switcher = Observable.from(kk);
		Action1<String> light = new Action1<String>() {

			@Override
			public void call(String s) {
				// 处理传过来的onNext事件
				System.out.println("on next: " + s);
			}

		};

		switcher.subscribe(light);
	}

	/**
	 * 一个RxJava流式API的调用: 同一个调用主体一路调用下来, 一气呵成.
	 * 
	 * 由于被观察者产生事件, 是事件的起点, 那么开头就是用Observable这个主体调用来创建被观察者, 产生事件, 为了保证流式API调用规则,
	 * 就直接让Observable作为唯一的调用主体, 一路调用下去.
	 * 
	 * 一句话, 背后的真实的逻辑依然是台灯订阅了开关, 但是在表面上, 我们让开关"假装"订阅了台灯, 以便于保持流式API调用风格不变.
	 */
	@Test
	public void testFilter() {
		Observable.just("ON", "Off", null, "On")
				.filter(new Func1<String, Boolean>() {

					public Boolean call(String s) {
						return s != null;
					}
				}).subscribe(new Action1<String>() {
					public void call(String t) {
						System.out.println(t);
					}
				});
	}

	/**
	 * 整个过程大概是这样的:
	 * <ul>
	 * <li>创建被观察者，产生事件 设置事件
	 * <li>传递过程中的过滤，合并，变换等加工操作。 
	 * <li>订阅一个观察者对象，实现事件最终的处理。
	 * </ul>
	 * 
	 * Tips: 当调用订阅操作（即调用Observable.subscribe()方法）的时候，被观察者才真正开始发出事件。
	 * @on
	 */
	@Test
	public void testObservableFlow() {
		// 创建被观察者，是事件传递的起点
		Observable.just("On", "Off", "On", "On")
				// 这就是在传递过程中对事件进行过滤操作
				.filter(new Func1<String, Boolean>() {
					@Override
					public Boolean call(String s) {
						return s != null;
					}
				})
				// 实现订阅
				.subscribe(
						// 创建观察者，作为事件传递的终点处理事件
						new Subscriber<String>() {
							@Override
							public void onCompleted() {
								System.out.println("结束观察...\n");
							}

							@Override
							public void onError(Throwable e) {
								// 出现错误会调用这个方法
								System.out.println("出错了");
							}

							@Override
							public void onNext(String s) {
								// 处理事件
								System.out.println("On Next 处理: " + s);
							}
						});
	}
}
