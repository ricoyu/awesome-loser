package com.loserico.redis;

import redis.clients.jedis.Jedis;

public class LockTest {

	/**
	 * Acquire lock.
	 * 
	 * @param jedis
	 * @return true if lock is acquired, false acquire timeouted
	 * @throws InterruptedException
	 *             in case of thread interruption
	 */
	public synchronized boolean acquire(Jedis jedis, String lockKey, int timeout) throws InterruptedException {
		boolean locked = false;
		while (timeout >= 0) {
			long expires = System.currentTimeMillis() + timeout + 1;
			String expiresStr = String.valueOf(expires); //锁到期时间

			if (jedis.setnx(lockKey, expiresStr) == 1) {
				// lock acquired
				locked = true;
				return true;
			}

			String currentValueStr = jedis.get(lockKey); //redis里的时间
			if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
				//判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
				// lock is expired

				String oldValueStr = jedis.getSet(lockKey, expiresStr);
				//获取上一个锁到期时间，并设置现在的锁到期时间，
				//只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
				if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
					//如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
					// lock acquired
					locked = true;
					return true;
				}
			}
			timeout -= 100;
			Thread.sleep(100);
		}
		return false;
	}
}
