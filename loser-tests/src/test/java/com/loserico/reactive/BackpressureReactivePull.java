package com.loserico.reactive;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class BackpressureReactivePull {

	/**
	 * 在代码中, 传递事件开始前的onstart()中, 调用了request(1), 通知被观察者先发送一个事件, 然后在onNext()中处理完事件,
	 * 再次调用request(1), 通知被观察者发送下一个事件....
	 * 
	 * 如果你想取消这种backpressure 策略, 调用quest(Long.MAX_VALUE)即可.
	 * 
	 * 实际上, 在上面的代码中, 你也可以不需要调用request(n)方法去拉取数据, 程序依然能完美运行, 这是因为range --> observeOn,
	 * 这一段中间过程本身就是响应式拉取数据, observeOn这个操作符内部有一个缓冲区, Android环境下长度是16,
	 * 它会告诉range最多发送16个事件, 充满缓冲区即可.不过话说回来, 在观察者中使用request(n)这个方法可以使背压的策略表现得更加直观,
	 * 更便于理解.
	 * 
	 * 如果你足够细心, 会发现, 在开头展示异常情况的代码中, 使用的是interval这个操作符, 但是在这里使用了range操作符, 为什么呢?
	 * 
	 * 这是因为interval操作符本身并不支持背压策略, 它并不响应request(n), 也就是说, 它发送事件的速度是不受控制的,
	 * 而range这类操作符是支持背压的, 它发送事件的速度可以被控制.
	 * 
	 * 那么到底什么样的Observable是支持背压的呢?
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		Observable observable = Observable.range(1, 100000);
		class MySubscriber extends Subscriber<Integer> {
			@Override
			public void onStart() {
				// 一定要在onStart中通知被观察者先发送一个事件
				request(1);
			}

			@Override
			public void onCompleted() {
				System.out.println("complete");
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
				System.out.println("error");
			}

			@Override
			public void onNext(Integer n) {
				System.out.println("处理完了, 再来一个 " + n);
				// 处理完毕之后，再通知被观察者发送下一个事件
				request(1);
			}
		}

		observable.observeOn(Schedulers.newThread())
				.subscribe(new MySubscriber());

		Thread.currentThread().join();
	}
}
