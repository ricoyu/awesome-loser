package com.loserico.reactive;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BackpressureTest {

	/**
	 * RxJava是一个观察者模式的架构, 当这个架构中被观察者(Observable)和观察者(Subscriber)处在不同的线程环境中时,
	 * 由于者各自的工作量不一样, 导致它们产生事件和处理事件的速度不一样, 这就会出现两种情况:
	 * 
	 * 1) 被观察者产生事件慢一些, 观察者处理事件很快. 那么观察者就会等着被观察者发送事件, (好比观察者在等米下锅, 程序等待, 这没有问题)
	 * 2) 被观察者产生事件的速度很快, 而观察者处理很慢. 那就出问题了, 如果不作处理的话, 事件会堆积起来, 最终挤爆你的内存, 导致程序崩溃.
	 * (好比被观察者生产的大米没人吃, 堆积最后就会烂掉)
	 * @throws InterruptedException 
	 * @on
	 */
	public static void main(String[] args) throws InterruptedException {
		// 在下面的代码中，被观察者发送事件的速度是观察者处理速度的1000倍
		Observable.interval(1, TimeUnit.MILLISECONDS)
				.observeOn(Schedulers.newThread())
				// .observeOn(Schedulers.newThread())
				.subscribe(new Action1() {
					@Override
					public void call(Object aLong) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(aLong);
					}
				});
		Thread.currentThread().join();
	}
}
