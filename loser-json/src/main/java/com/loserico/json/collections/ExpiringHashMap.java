package com.loserico.json.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * A thread-safe implementation of a HashMap which entries expires after the specified
 * life time. The life-time can be defined on a per-key basis, or using a default one,
 * that is passed to the constructor.
 * 
 * @author Pierantonio Cangianiello
 * @param <K> the Key type
 * @param <V> the Value type
 */
public class ExpiringHashMap<K, V> implements ExpiringMap<K, V> {

	private final Map<K, V> internalMap;

	private final Map<K, ExpiringKey<K>> expiringKeys;

	/**
	 * Holds the map keys using the given life time for expiration.
	 */
	private final DelayQueue<ExpiringKey> delayQueue = new DelayQueue<ExpiringKey>();

	/**
	 * The default max life time in milliseconds.
	 */
	private final long maxLifeTimeMillis;

	public ExpiringHashMap() {
		internalMap = new ConcurrentHashMap<K, V>();
		expiringKeys = new WeakHashMap<K, ExpiringKey<K>>();
		this.maxLifeTimeMillis = Long.MAX_VALUE;
	}

	public ExpiringHashMap(long defaultMaxLifeTimeMillis) {
		internalMap = new ConcurrentHashMap<K, V>();
		expiringKeys = new WeakHashMap<K, ExpiringKey<K>>();
		this.maxLifeTimeMillis = defaultMaxLifeTimeMillis;
	}

	public ExpiringHashMap(long defaultMaxLifeTimeMillis, int initialCapacity) {
		internalMap = new ConcurrentHashMap<K, V>(initialCapacity);
		expiringKeys = new WeakHashMap<K, ExpiringKey<K>>(initialCapacity);
		this.maxLifeTimeMillis = defaultMaxLifeTimeMillis;
	}

	public ExpiringHashMap(long defaultMaxLifeTimeMillis, int initialCapacity, float loadFactor) {
		internalMap = new ConcurrentHashMap<K, V>(initialCapacity, loadFactor);
		expiringKeys = new WeakHashMap<K, ExpiringKey<K>>(initialCapacity, loadFactor);
		this.maxLifeTimeMillis = defaultMaxLifeTimeMillis;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		cleanup();
		return internalMap.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		cleanup();
		return internalMap.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object key) {
		cleanup();
		return internalMap.containsKey((K) key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsValue(Object value) {
		cleanup();
		return internalMap.containsValue((V) value);
	}

	@Override
	public V get(Object key) {
		cleanup();
		renewKey((K) key);
		return internalMap.get((K) key);
	}

	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		V value = get(key);
		if(value == null) {
			value = mappingFunction.apply(key);
			put(key, value);
		}
		return value;
	}

	
	@Override
	public V computeIfAbsent(K key, 
			Function<? super K, ? extends V> mappingFunction, 
			long lifeTimeMillis,
			TimeUnit timeUnit) {
		V value = get(key);
		if(value == null) {
			value = mappingFunction.apply(key);
			put(key, value, lifeTimeMillis, timeUnit);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V put(K key, V value) {
		return this.put(key, value, maxLifeTimeMillis);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V put(K key, V value, long lifeTimeMillis) {
		cleanup();
		ExpiringKey delayedKey = new ExpiringKey(key, lifeTimeMillis);
		ExpiringKey oldKey = expiringKeys.put((K) key, delayedKey);
		if (oldKey != null) {
			expireKey(oldKey);
			expiringKeys.put((K) key, delayedKey);
		}
		delayQueue.offer(delayedKey);
		return internalMap.put(key, value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public V put(K key, V value, long lifeTimeMillis, TimeUnit timeUnit) {
		return put(key, value, timeUnit.toMillis(lifeTimeMillis));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V remove(Object key) {
		V removedValue = internalMap.remove((K) key);
		expireKey(expiringKeys.remove((K) key));
		return removedValue;
	}

	/**
	 * Not supported.
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renewKey(K key) {
		ExpiringKey<K> delayedKey = expiringKeys.get((K) key);
		if (delayedKey != null) {
			delayedKey.renew();
			return true;
		}
		return false;
	}

	private void expireKey(ExpiringKey<K> delayedKey) {
		if (delayedKey != null) {
			delayedKey.expire();
			cleanup();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		delayQueue.clear();
		expiringKeys.clear();
		internalMap.clear();
	}

	/**
	 * Not supported.
	 */
	@Override
	public Set<K> keySet() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not supported.
	 */
	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not supported.
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	private void cleanup() {
		ExpiringKey<K> delayedKey = delayQueue.poll();
		while (delayedKey != null) {
			internalMap.remove(delayedKey.getKey());
			expiringKeys.remove(delayedKey.getKey());
			delayedKey = delayQueue.poll();
		}
	}

	private class ExpiringKey<K> implements Delayed {

		private long startTime = System.currentTimeMillis();
		private final long maxLifeTimeMillis;
		private final K key;

		public ExpiringKey(K key, long maxLifeTimeMillis) {
			this.maxLifeTimeMillis = maxLifeTimeMillis;
			this.key = key;
		}

		public K getKey() {
			return key;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final ExpiringKey<K> other = (ExpiringKey<K>) obj;
			if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
				return false;
			}
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			int hash = 7;
			hash = 31 * hash + (this.key != null ? this.key.hashCode() : 0);
			return hash;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(getDelayMillis(), TimeUnit.MILLISECONDS);
		}

		private long getDelayMillis() {
			return (startTime + maxLifeTimeMillis) - System.currentTimeMillis();
		}

		public void renew() {
			startTime = System.currentTimeMillis();
		}

		public void expire() {
			startTime = System.currentTimeMillis() - maxLifeTimeMillis - 1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(Delayed that) {
			return Long.compare(this.getDelayMillis(), ((ExpiringKey) that).getDelayMillis());
		}
	}

}