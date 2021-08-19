package com.loserico.concurrent.aqs;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * A reentrant mutual exclusion {@link Lock} with the same basic
 * behavior and semantics as the implicit monitor lock accessed using
 * {@code synchronized} methods and statements, but with extended
 * capabilities.
 *
 * <p>A {@code ReentrantLock} is <em>owned</em> by the thread last
 * successfully locking, but not yet unlocking it. A thread invoking
 * {@code lock} will return, successfully acquiring the lock, when
 * the lock is not owned by another thread. The method will return
 * immediately if the current thread already owns the lock. This can
 * be checked using methods {@link #isHeldByCurrentThread}, and {@link
 * #getHoldCount}.
 *
 * <p>The constructor for this class accepts an optional
 * <em>fairness</em> parameter.  When set {@code true}, under
 * contention, locks favor granting access to the longest-waiting
 * thread.  Otherwise this lock does not guarantee any particular
 * access order.  Programs using fair locks accessed by many threads
 * may display lower overall throughput (i.e., are slower; often much
 * slower) than those using the default setting, but have smaller
 * variances in times to obtain locks and guarantee lack of
 * starvation. Note however, that fairness of locks does not guarantee
 * fairness of thread scheduling. Thus, one of many threads using a
 * fair lock may obtain it multiple times in succession while other
 * active threads are not progressing and not currently holding the
 * lock.
 * Also note that the untimed {@link #tryLock()} method does not
 * honor the fairness setting. It will succeed if the lock
 * is available even if other threads are waiting.
 *
 * <p>It is recommended practice to <em>always</em> immediately
 * follow a call to {@code lock} with a {@code try} block, most
 * typically in a before/after construction such as:
 *
 * <pre> {@code
 * class X {
 *   private final ReentrantLock lock = new ReentrantLock();
 *   // ...
 *
 *   public void m() {
 *     lock.lock();  // block until condition holds
 *     try {
 *       // ... method body
 *     } finally {
 *       lock.unlock()
 *     }
 *   }
 * }}</pre>
 *
 * <p>In addition to implementing the {@link Lock} interface, this
 * class defines a number of {@code public} and {@code protected}
 * methods for inspecting the state of the lock.  Some of these
 * methods are only useful for instrumentation and monitoring.
 *
 * <p>Serialization of this class behaves in the same way as built-in
 * locks: a deserialized lock is in the unlocked state, regardless of
 * its state when serialized.
 *
 * <p>This lock supports a maximum of 2147483647 recursive locks by
 * the same thread. Attempts to exceed this limit result in
 * {@link Error} throws from locking methods.
 *
 * @author Doug Lea
 * @since 1.5
 */
public class LoserReentrantLock implements Lock, java.io.Serializable {
	private static final long serialVersionUID = 7373984872572414699L;
	/**
	 * 内部调用AQS的动作，都基于该成员属性实现
	 */
	private final Sync sync;
	
	/**
	 * ReentrantLock锁同步操作的基础类,继承自AQS框架.
	 * 该类有两个继承类，1、NonfairSync 非公平锁，2、FairSync公平锁
	 */
	abstract static class Sync extends LoserAbstractQueuedSynchronizer {
		private static final long serialVersionUID = -5179523762034025860L;
		
		/**
		 * 加锁的具体行为由子类实现
		 */
		abstract void lock();
		
		/**
		 * 尝试获取非公平锁
		 */
		final boolean nonfairTryAcquire(int acquires) {
			//acquires = 1
			final Thread current = Thread.currentThread();
			int c = getState();
			/*
			 * 不需要判断同步队列（CLH）中是否有排队等待线程
			 */
			if (c == 0) {
				//unsafe操作，cas修改state状态
				if (compareAndSetState(0, acquires)) {
					setExclusiveOwnerThread(current);
					return true; //加锁成功
				}
			}
			/*
			 * state状态不为0, 判断锁持有者是否是当前线程, 如果是当前线程则state+1
			 */
			else if (current == getExclusiveOwnerThread()) {
				int nextc = c + acquires;
				if (nextc < 0) // overflow
				{
					throw new Error("Maximum lock count exceeded");
				}
				setState(nextc);
				return true;
			}
			//加锁失败
			return false;
		}
		
		/**
		 * 释放锁
		 */
		protected final boolean tryRelease(int releases) {
			//本次释放锁之后, 剩下的加锁次数
			int c = getState() - releases;
			/*
			 * 如果不是当前线程加锁的, 那么调用tryRelease就会抛出异常
			 * 你都没有获取锁, 怎么解锁呢?
			 */
			if (Thread.currentThread() != getExclusiveOwnerThread()) {
				throw new IllegalMonitorStateException();
			}
			//表示锁是否释放了
			boolean free = false;
			/*
			 * 如果此时c==0, 表示锁完全释放了, 因为这是一个可重入锁, 可能加锁了多次
			 * 只有全部释放了, free才是true, 同时把exclusiveOwnerThread(标记加锁的线程)清除
			 */
			if (c == 0) {
				free = true;
				setExclusiveOwnerThread(null);
			}
			setState(c);
			return free;
		}
		
		/**
		 * 判断持有独占锁的线程是否是当前线程
		 */
		protected final boolean isHeldExclusively() {
			return getExclusiveOwnerThread() == Thread.currentThread();
		}
		
		//返回条件对象
		final ConditionObject newCondition() {
			return new ConditionObject();
		}
		
		
		final Thread getOwner() {
			return getState() == 0 ? null : getExclusiveOwnerThread();
		}
		
		final int getHoldCount() {
			return isHeldExclusively() ? getState() : 0;
		}
		
		final boolean isLocked() {
			return getState() != 0;
		}
		
		/**
		 * Reconstitutes the instance from a stream (that is, deserializes it).
		 */
		private void readObject(java.io.ObjectInputStream s)
				throws java.io.IOException, ClassNotFoundException {
			s.defaultReadObject();
			setState(0); // reset to unlocked state
		}
	}
	
	/**
	 * 非公平锁
	 */
	static final class NonfairSync extends Sync {
		private static final long serialVersionUID = 7316153563782823691L;
		
		/**
		 * 直接尝试加锁 <br/>
		 * 与公平锁实现的加锁行为一个最大的区别在于，此处不会去判断同步队列(CLH队列)中是否有排队等待加锁的节点
		 * 并将独占锁持有者 exclusiveOwnerThread 指向当前线程
		 * <p/>
		 * 如果当前有人占用锁，再尝试去加一次锁, 如果还加锁失败则入队等待
		 */
		final void lock() {
			if (compareAndSetState(0, 1)) {
				//标记当前锁属于这哥线程
				setExclusiveOwnerThread(Thread.currentThread());
			} else {
				acquire(1);
			}
		}
		
		/**
		 * 尝试获取锁, 如果成功则将ExclusiveOwnerThread设为当前线程, 失败不会block
		 * <ul>
		 *     <li/>如果资源还没有被锁定, 尝试加锁并设置state=acquires
		 *     <li/>如果资源已经被锁定且是被当前线程锁定的, 那么state=state+acquires
		 * </ul>
		 *
		 * @param acquires
		 * @return boolean
		 */
		protected final boolean tryAcquire(int acquires) {
			return nonfairTryAcquire(acquires);
		}
	}
	
	/**
	 * 公平锁
	 */
	static final class FairSync extends Sync {
		private static final long serialVersionUID = -3000897897090466540L;
		
		/**
		 * 看队列中是否已经有Worker在排队, 有的话将自己加入CLH队列中等待
		 */
		final void lock() {
			acquire(1);
		}
		
		/**
		 * 尝试加锁, 返回加锁成功与否标识
		 */
		protected final boolean tryAcquire(int acquires) {
			final Thread current = Thread.currentThread();
			//状态(锁)变量
			int c = getState();
			//如果没有线程获得锁
			if (c == 0) {
				/**
				 * 与非公平锁中的区别, 需要先判断队列当中是否有等待的节点(已经有别的线程在等待获取锁)
				 * 如果没有则可以尝试CAS获取锁
				 */
				if (!hasQueuedPredecessors() &&
						compareAndSetState(0, acquires)) {
					//如果抢到了锁, 将独占线程指向当前线程
					setExclusiveOwnerThread(current);
					return true;
				}
			} 
			/*
			 * 当前获取锁的线程就是自己, 那么加锁次数+1(可重入)
			 */
			else if (current == getExclusiveOwnerThread()) {
				int nextc = c + acquires;
				if (nextc < 0) {
					throw new Error("Maximum lock count exceeded");
				}
				setState(nextc);
				return true;
			}
			return false;
		}
	}
	
	/**
	 * 默认构造函数，创建非公平锁对象
	 */
	public LoserReentrantLock() {
		sync = new NonfairSync();
	}
	
	/**
	 * 根据要求创建公平锁或非公平锁
	 */
	public LoserReentrantLock(boolean fair) {
		sync = fair ? new FairSync() : new NonfairSync();
	}
	
	/**
	 * 加锁
	 */
	public void lock() {
		sync.lock();
	}
	
	/**
	 * 尝试获去取锁，获取失败被阻塞，线程被中断直接抛出异常
	 */
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}
	
	/**
	 * 尝试加锁
	 */
	public boolean tryLock() {
		return sync.nonfairTryAcquire(1);
	}
	
	/**
	 * 指定等待时间内尝试加锁
	 */
	public boolean tryLock(long timeout, TimeUnit unit)
			throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(timeout));
	}
	
	/**
	 * 尝试去释放锁
	 */
	public void unlock() {
		sync.release(1);
	}
	
	/**
	 * 返回条件对象
	 */
	public Condition newCondition() {
		return sync.newCondition();
	}
	
	/**
	 * 返回当前线程持有的state状态数量
	 */
	public int getHoldCount() {
		return sync.getHoldCount();
	}
	
	/**
	 * 查询当前线程是否持有锁
	 */
	public boolean isHeldByCurrentThread() {
		return sync.isHeldExclusively();
	}
	
	/**
	 * 状态表示是否被Thread加锁持有
	 */
	public boolean isLocked() {
		return sync.isLocked();
	}
	
	/**
	 * 是否公平锁？是返回true 否则返回 false
	 */
	public final boolean isFair() {
		return sync instanceof FairSync;
	}
	
	/**
	 * Returns the thread that currently owns this lock, or
	 * {@code null} if not owned. When this method is called by a
	 * thread that is not the owner, the return value reflects a
	 * best-effort approximation of current lock status. For example,
	 * the owner may be momentarily {@code null} even if there are
	 * threads trying to acquire the lock but have not yet done so.
	 * This method is designed to facilitate construction of
	 * subclasses that provide more extensive lock monitoring
	 * facilities.
	 *
	 * @return the owner, or {@code null} if not owned
	 */
	protected Thread getOwner() {
		return sync.getOwner();
	}
	
	/**
	 * 判断队列当中是否有在等待获取锁的Thread节点
	 */
	public final boolean hasQueuedThreads() {
		return sync.hasQueuedThreads();
	}
	
	/**
	 * 当前线程是否在同步队列中等待
	 */
	public final boolean hasQueuedThread(Thread thread) {
		return sync.isQueued(thread);
	}
	
	/**
	 * Returns an estimate of the number of threads waiting to
	 * acquire this lock.  The value is only an estimate because the number of
	 * threads may change dynamically while this method traverses
	 * internal data structures.  This method is designed for use in
	 * monitoring of the system state, not for synchronization
	 * control.
	 *
	 * @return the estimated number of threads waiting for this lock
	 */
	public final int getQueueLength() {
		return sync.getQueueLength();
	}
	
	/**
	 * 返回Thread集合，排队中的所有节点Thread会被返回
	 */
	protected Collection<Thread> getQueuedThreads() {
		return sync.getQueuedThreads();
	}
	
	/**
	 * 条件队列当中是否有正在等待的节点
	 */
	public boolean hasWaiters(Condition condition) {
		if (condition == null) {
			throw new NullPointerException();
		}
		if (!(condition instanceof LoserAbstractQueuedSynchronizer.ConditionObject)) {
			throw new IllegalArgumentException("not owner");
		}
		return sync.hasWaiters((LoserAbstractQueuedSynchronizer.ConditionObject) condition);
	}
	
	/**
	 * Returns an estimate of the number of threads waiting on the
	 * given condition associated with this lock. Note that because
	 * timeouts and interrupts may occur at any time, the estimate
	 * serves only as an upper bound on the actual number of waiters.
	 * This method is designed for use in monitoring of the system
	 * state, not for synchronization control.
	 *
	 * @param condition the condition
	 * @return the estimated number of waiting threads
	 * @throws IllegalMonitorStateException if this lock is not held
	 * @throws IllegalArgumentException     if the given condition is
	 *                                      not associated with this lock
	 * @throws NullPointerException         if the condition is null
	 */
	public int getWaitQueueLength(Condition condition) {
		if (condition == null) {
			throw new NullPointerException();
		}
		if (!(condition instanceof LoserAbstractQueuedSynchronizer.ConditionObject)) {
			throw new IllegalArgumentException("not owner");
		}
		return sync.getWaitQueueLength((LoserAbstractQueuedSynchronizer.ConditionObject) condition);
	}
	
	/**
	 * Returns a collection containing those threads that may be
	 * waiting on the given condition associated with this lock.
	 * Because the actual set of threads may change dynamically while
	 * constructing this result, the returned collection is only a
	 * best-effort estimate. The elements of the returned collection
	 * are in no particular order.  This method is designed to
	 * facilitate construction of subclasses that provide more
	 * extensive condition monitoring facilities.
	 *
	 * @param condition the condition
	 * @return the collection of threads
	 * @throws IllegalMonitorStateException if this lock is not held
	 * @throws IllegalArgumentException     if the given condition is
	 *                                      not associated with this lock
	 * @throws NullPointerException         if the condition is null
	 */
	protected Collection<Thread> getWaitingThreads(Condition condition) {
		if (condition == null) {
			throw new NullPointerException();
		}
		if (!(condition instanceof LoserAbstractQueuedSynchronizer.ConditionObject)) {
			throw new IllegalArgumentException("not owner");
		}
		return sync.getWaitingThreads((LoserAbstractQueuedSynchronizer.ConditionObject) condition);
	}
	
	/**
	 * Returns a string identifying this lock, as well as its lock state.
	 * The state, in brackets, includes either the String {@code "Unlocked"}
	 * or the String {@code "Locked by"} followed by the
	 * {@linkplain Thread#getName name} of the owning thread.
	 *
	 * @return a string identifying this lock, as well as its lock state
	 */
	public String toString() {
		Thread o = sync.getOwner();
		return super.toString() + ((o == null) ?
				"[Unlocked]" :
				"[Locked by thread " + o.getName() + "]");
	}
}
