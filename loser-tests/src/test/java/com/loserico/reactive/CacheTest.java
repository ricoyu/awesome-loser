package com.loserico.reactive;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class CacheTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CacheTest.class);

	/*
	 * 就是虽然被观察者发送事件速度很快，观察者处理不过来，但是可以选择先缓存一部分，然后慢慢读。
	 * 
	 */
	@Test
	public void testCache() throws InterruptedException {
		Observable.interval(1, MILLISECONDS)
				.observeOn(Schedulers.newThread())
				.buffer(100, MILLISECONDS)
				.subscribe((list) -> {
					try {
						SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(list.size());
				});
		Thread.currentThread().join();
	}

	/**
	 * 对于不支持背压的Observable除了使用上述两类生硬的操作符之外, 还有更好的选择: onBackpressurebuffer,
	 * onBackpressureDrop <ul> 
	 * <li>onBackpressurebuffer: 把observable发送出来的事件做缓存, 当request方法被调用的时候, 
	 * 				  给下层流发送一个item(如果给这个缓存区设置了大小, 那么超过了这个大小就会抛出异常) 
	 * <li>onBackpressureDrop: 将observable发送的事件抛弃掉, 直到subscriber再次调用request(n)方法的时候, 就发送给它这之后的n个事件.
	 * @throws InterruptedException 
	 * @on
	 */
	@Test
	public void testOnBackpressureDrop() throws InterruptedException {
		Observable.interval(1, MILLISECONDS)
			.onBackpressureDrop()
			.observeOn(Schedulers.newThread())
			.subscribe(new Subscriber<Long>() {
				public void onStart() {
			        logger.info("TAG start");
			    }
				
				@Override
				public void onCompleted() {
					logger.info("Complete");
				}

				@Override
				public void onError(Throwable e) {
					logger.error("Error", e);
				}

				@Override
				public void onNext(Long t) {
					logger.info("TAG---->" + t);
					try {
						MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			});
		Thread.currentThread().join();
	}
}
