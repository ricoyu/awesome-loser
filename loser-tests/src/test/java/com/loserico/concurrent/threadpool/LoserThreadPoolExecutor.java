package com.loserico.concurrent.threadpool;

import com.loserico.concurrent.aqs.AbstractQueuedSynchronizer;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重点是addWorker方法
 * <p>
 * Copyright: Copyright (c) 2019-12-01 17:17
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LoserThreadPoolExecutor extends AbstractExecutorService {
	/**
	 * The main pool control state, ctl, is an atomic integer packing
	 * two conceptual fields
	 * workerCount, indicating the effective number of threads
	 * runState,    indicating whether running, shutting down etc
	 * <p>
	 * In order to pack them into one int, we limit workerCount to
	 * (2^29)-1 (about 500 million) threads rather than (2^31)-1 (2
	 * billion) otherwise representable. If this is ever an issue in
	 * the future, the variable can be changed to be an AtomicLong,
	 * and the shift/mask constants below adjusted. But until the need
	 * arises, this code is a bit faster and simpler using an int.
	 * <p>
	 * The workerCount is the number of workers that have been
	 * permitted to start and not permitted to stop.  The value may be
	 * transiently different from the actual number of live threads,
	 * for example when a ThreadFactory fails to create a thread when
	 * asked, and when exiting threads are still performing
	 * bookkeeping before terminating. The user-visible pool size is
	 * reported as the current size of the workers set.
	 * <p>
	 * The runState provides the main lifecycle control, taking on values:
	 * <p>
	 * RUNNING:  Accept new tasks and process queued tasks
	 * SHUTDOWN: Don't accept new tasks, but process queued tasks
	 * STOP:     Don't accept new tasks, don't process queued tasks,
	 * and interrupt in-progress tasks
	 * TIDYING:  All tasks have terminated, workerCount is zero,
	 * the thread transitioning to state TIDYING
	 * will run the terminated() hook method
	 * TERMINATED: terminated() has completed
	 * <p>
	 * The numerical order among these values matters, to allow
	 * ordered comparisons. The runState monotonically increases over
	 * time, but need not hit each state. The transitions are:
	 * <p>
	 * RUNNING -> SHUTDOWN
	 * On invocation of shutdown(), perhaps implicitly in finalize()
	 * (RUNNING or SHUTDOWN) -> STOP
	 * On invocation of shutdownNow()
	 * SHUTDOWN -> TIDYING
	 * When both queue and pool are empty
	 * STOP -> TIDYING
	 * When pool is empty
	 * TIDYING -> TERMINATED
	 * When the terminated() hook method has completed
	 * <p>
	 * Threads waiting in awaitTermination() will return when the
	 * state reaches TERMINATED.
	 * <p>
	 * Detecting the transition from SHUTDOWN to TIDYING is less
	 * straightforward than you'd like because the queue may become
	 * empty after non-empty and vice versa during SHUTDOWN state, but
	 * we can only terminate if, after seeing that it is empty, we see
	 * that workerCount is 0 (which sometimes entails a recheck -- see
	 * below).
	 * <p>
	 * 是对线程池的运行状态和线程池中有效线程的数量进行控制的一个字段,
	 * 它包含两部分的信息: 线程池的运行状态(runState) 和 线程池内有效线程的数量(workerCount)
	 * 这里可以看到, 使用了Integer类型来保存, 高3位保存runState, 低29位保存workerCount.
	 * COUNT_BITS 就是29, CAPACITY就是1左移29位减1(29个1), 这个常量表示workerCount的上限值, 大约是5亿.
	 * <p>
	 * 高3位是线程运行状态 runState
	 * 低29位保存的是线程池的数量 workerCount
	 * 默认是RUNNING状态, 线程池的数量为0
	 */
	private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
	private static final int COUNT_BITS = Integer.SIZE - 3;    //29
	private static final int CAPACITY = (1 << COUNT_BITS) - 1; //00011111111111111111111111111111, 整型值:536870911
	
	/*
	 * 线程池的状态 runState is stored in the high-order bits
	 *
	 * 注意数字在计算机中的二进制都是补码形式
	 * -1的二进制形式是: 11111111 11111111 11111111 11111111
	 * 左移29位得到:     11100000 00000000 00000000 00000000
	 *
	 * 1的二进制形式是:  00000000 00000000 00000000 00000001
	 * 左移29位得到:     001
	 *
	 * 2的二进制形式是:  00000000 00000000 00000000 00000010
	 * 左移29位得到:     010
	 *
	 * 2的二进制形式是:  00000000 00000000 00000000 00000011
	 * 左移29位得到:     011
	 *
	 * 线程池处在RUNNING状态时，能够接收新任务，以及对已添加的任务进行处理。
	 * 状态切换: 线程池的初始化状态是RUNNING. 换句话说, 线程池被一旦被创建, 就处于RUNNING状态, 并且线程池中的任务数为0!
	 */
	private static final int RUNNING = -1 << COUNT_BITS;  //高3位为111, 整型值为:-536870912
	/*
	 * 线程池处在SHUTDOWN状态时, 不接收新任务, 但能处理已添加的任务
	 * 状态切换: 调用线程池的shutdown()接口时, 线程池由RUNNING -> SHUTDOWN
	 */
	private static final int SHUTDOWN = 0 << COUNT_BITS;  //高3位为000, 整型值为:0
	/*
	 * 线程池处在STOP状态时, 不接收新任务, 不处理已添加的任务, 并且会中断正在处理的任务。
	 * 状态切换: 调用线程池的shutdownNow()接口时, 线程池由(RUNNING or SHUTDOWN) -> STOP
	 */
	private static final int STOP = 1 << COUNT_BITS;  //高3位为001, 整型值为:536870912
	/*
	 * 当所有的任务已终止, ctl记录的 "任务数量" 为0, 线程池会变为TIDYING状态.
	 * 当线程池变为TIDYING状态时, 会执行钩子函数terminated(). terminated()在ThreadPoolExecutor类中是空的,
	 * 若用户想在线程池变为TIDYING时, 进行相应的处理, 可以通过重载terminated()函数来实现.
	 *
	 * 状态切换: 当线程池在SHUTDOWN状态下, 阻塞队列为空并且线程池中执行的任务也为空时, 就会由 SHUTDOWN -> TIDYING.
	 * 当线程池在STOP状态下, 线程池中执行的任务为空时, 就会由STOP -> TIDYING
	 */
	private static final int TIDYING = 2 << COUNT_BITS;  //高3位为010, 整型值为:1073741824
	/*
	 * 线程池彻底终止, 就变成TERMINATED状态
	 * 状态切换: 线程池处在TIDYING状态时, 执行完terminated()之后, 就会由 TIDYING -> TERMINATED
	 *
	 * 进入TERMINATED的条件如下:
	 * 1: 线程池不是RUNNING状态
	 * 2: 线程池状态不是TIDYING状态或TERMINATED状态
	 * 3: 如果线程池状态是SHUTDOWN并且workerQueue为空
	 * 4: workerCount为0
	 * 5: 设置TIDYING状态成功
	 */
	private static final int TERMINATED = 3 << COUNT_BITS;  //高3位为011, 整型值为:1610612736
	
	/*
	 * Packing and unpacking ctlOf
	 * 获取运行状态
	 *
	 * ~ 是按位取反(连符号位一起取反)
	 * 比如字长为8, 那么 2的二进制形式为 00000010  ~2 -> 11111101
	 *                 -2的二进制形式为 11111110 ~-2 -> 00000001
	 *
	 * CAPACITY 是 00011111111111111111111111111111
	 * ~CAPACITY是 11100000000000000000000000000000
	 *
	 * c位与~CAPACITY就是取高3位(不管c的低29位是什么, 位与后都是0)
	 */
	private static int runStateOf(int c) {
		return c & ~CAPACITY;
	}
	
	/*
	 * 获取活动线程数
	 * CAPACITY 是 00011111111111111111111111111111
	 * 1 << 29 - 1 --> 00100000000000000000000000000000 - 1 -> 00011111111111111111111111111111
	 * 任意数字c按位与上CAPACITY不就是取了c的低29吗? 所以这里是取c的低29位
	 */
	private static int workerCountOf(int c) {
		return c & CAPACITY;
	}
	
	/*
	 * 组合ctl变量，rs=runStatue代表的是线程池的状态，wc=workCount代表的是线程池线程的数量
	 */
	private static int ctlOf(int rs, int wc) {
		return rs | wc;
	}
	
	/*
	 * Bit field accessors that don't require unpacking ctl.
	 * These depend on the bit layout and on workerCount being never negative.
	 */
	
	private static boolean runStateLessThan(int c, int s) {
		return c < s;
	}
	
	private static boolean runStateAtLeast(int c, int s) {
		return c >= s;
	}
	
	/*
	 * RUNNING 是 -1, 只有这个状态是负数
	 * SHUTDOWN是 0
	 * 所以判断线程池是否处在RUNNING状态时, 直接判断ctl是否小于0就可以了
	 */
	private static boolean isRunning(int c) {
		return c < SHUTDOWN;
	}
	
	/**
	 * Attempts to CAS-increment the workerCount field of ctl.
	 */
	private boolean compareAndIncrementWorkerCount(int expect) {
		return ctl.compareAndSet(expect, expect + 1);
	}
	
	/**
	 * Attempts to CAS-decrement the workerCount field of ctl.
	 */
	private boolean compareAndDecrementWorkerCount(int expect) {
		return ctl.compareAndSet(expect, expect - 1);
	}
	
	/**
	 * Decrements the workerCount field of ctl. This is called only on
	 * abrupt termination of a thread (see processWorkerExit). Other
	 * decrements are performed within getTask.
	 */
	private void decrementWorkerCount() {
		do {
		} while (!compareAndDecrementWorkerCount(ctl.get()));
	}
	
	/**
	 * The queue used for holding tasks and handing off to worker
	 * threads.  We do not require that workQueue.poll() returning
	 * null necessarily means that workQueue.isEmpty(), so rely
	 * solely on isEmpty to see if the queue is empty (which we must
	 * do for example when deciding whether to transition from
	 * SHUTDOWN to TIDYING).  This accommodates special-purpose
	 * queues such as DelayQueues for which poll() is allowed to
	 * return null even if it may later return non-null when delays
	 * expire.
	 * <p>
	 * 用来保存等待被执行的任务的阻塞队列, 且任务必须实现Runable接口, 在JDK中提供了如下阻塞队列:
	 * 1: ArrayBlockingQueue  基于数组结构的有界阻塞队列, 按FIFO排序任务
	 * 2: LinkedBlockingQuene 基于链表结构的阻塞队列, 按FIFO排序任务, 吞吐量通常要高于ArrayBlockingQuene
	 * 3: SynchronousQueue    一个不存储元素的阻塞队列, 每个插入操作必须等到另一个线程调用移除操作,
	 *                        否则插入操作一直处于阻塞状态, 吞吐量通常要高于LinkedBlockingQuene
	 * 4: PriorityBlockingQueue 具有优先级的无界阻塞队列
	 */
	private final BlockingQueue<Runnable> workQueue;
	
	/**
	 * Lock held on access to workers set and related bookkeeping.
	 * While we could use a concurrent set of some sort, it turns out
	 * to be generally preferable to use a lock. Among the reasons is
	 * that this serializes interruptIdleWorkers, which avoids
	 * unnecessary interrupt storms, especially during shutdown.
	 * Otherwise exiting threads would concurrently interrupt those
	 * that have not yet interrupted. It also simplifies some of the
	 * associated statistics bookkeeping of largestPoolSize etc. We
	 * also hold mainLock on shutdown and shutdownNow, for the sake of
	 * ensuring workers set is stable while separately checking
	 * permission to interrupt and actually interrupting.
	 */
	private final ReentrantLock mainLock = new ReentrantLock();
	
	/**
	 * Set containing all worker threads in pool. Accessed only when
	 * holding mainLock.
	 */
	private final HashSet<Worker> workers = new HashSet<Worker>();
	
	/**
	 * Wait condition to support awaitTermination
	 */
	private final Condition termination = mainLock.newCondition();
	
	/**
	 * Tracks largest attained pool size. Accessed only under
	 * mainLock.
	 */
	private int largestPoolSize;
	
	/**
	 * Counter for completed tasks. Updated only on termination of
	 * worker threads. Accessed only under mainLock.
	 */
	private long completedTaskCount;
	
	/*
	 * All user control parameters are declared as volatiles so that
	 * ongoing actions are based on freshest values, but without need
	 * for locking, since no internal invariants depend on them
	 * changing synchronously with respect to other actions.
	 */
	
	/**
	 * Factory for new threads. All threads are created using this
	 * factory (via method addWorker).  All callers must be prepared
	 * for addWorker to fail, which may reflect a system or user's
	 * policy limiting the number of threads.  Even though it is not
	 * treated as an error, failure to create threads may result in
	 * new tasks being rejected or existing ones remaining stuck in
	 * the queue.
	 * <p>
	 * We go further and preserve pool invariants even in the face of
	 * errors such as OutOfMemoryError, that might be thrown while
	 * trying to create threads.  Such errors are rather common due to
	 * the need to allocate a native stack in Thread.start, and users
	 * will want to perform clean pool shutdown to clean up.  There
	 * will likely be enough memory available for the cleanup code to
	 * complete without encountering yet another OutOfMemoryError.
	 * <p>
	 * 用来创建新线程. 默认使用Executors.defaultThreadFactory() 来创建线程.
	 * 使用默认的ThreadFactory来创建线程时, 会使新创建的线程具有相同的NORM_PRIORITY优先级并且是非守护线程,
	 * 同时也设置了线程的名称.
	 */
	private volatile ThreadFactory threadFactory;
	
	/**
	 * Handler called when saturated or shutdown in execute.
	 * 线程池的饱和策略, 当阻塞队列满了, 且没有空闲的工作线程, 如果继续提交任务, 必须采取一种策略处理该任务, 线程池提供了4种策略:
	 * 1: AbortPolicy         直接抛出异常, 默认策略
	 * 2: CallerRunsPolicy    用调用者所在的线程来执行任务
	 * 3: DiscardOldestPolicy 丢弃阻塞队列中靠最前的任务, 并执行当前任务
	 * 4: DiscardPolicy       直接丢弃任务；
	 * <p>
	 * 当然也可以根据应用场景实现RejectedExecutionHandler接口, 自定义饱和策略, 如记录日志或持久化存储不能处理的任务
	 */
	private volatile RejectedExecutionHandler handler;
	
	/**
	 * Timeout in nanoseconds for idle threads waiting for work.
	 * Threads use this timeout when there are more than corePoolSize
	 * present or if allowCoreThreadTimeOut. Otherwise they wait
	 * forever for new work.
	 * 线程池维护线程所允许的空闲时间.
	 * 当线程池中的线程数量大于corePoolSize的时候, 如果这时没有新的任务提交,
	 * 核心线程外的线程不会立即销毁, 而是会等待, 直到等待的时间超过了keepAliveTime
	 */
	private volatile long keepAliveTime;
	
	/**
	 * If false (default), core threads stay alive even when idle.
	 * If true, core threads use keepAliveTime to time out waiting
	 * for work.
	 * 是否允许核心线程超时
	 */
	private volatile boolean allowCoreThreadTimeOut;
	
	/**
	 * Core pool size is the minimum number of workers to keep alive
	 * (and not allow to time out etc) unless allowCoreThreadTimeOut
	 * is set, in which case the minimum is zero.
	 * <p>
	 * 线程池中的核心线程数, 当提交一个任务时, 线程池创建一个新线程执行任务, 直到当前线程数等于corePoolSize;
	 * 如果当前线程数为corePoolSize, 继续提交的任务被保存到阻塞队列中, 等待被执行;
	 * 如果执行了线程池的prestartAllCoreThreads()方法, 线程池会提前创建并启动所有核心线程。
	 */
	private volatile int corePoolSize;
	
	/**
	 * Maximum pool size. Note that the actual maximum is internally bounded by CAPACITY.
	 * 线程池中允许的最大线程数.
	 * 如果当前阻塞队列满了, 且继续提交任务, 则创建新的线程执行任务, 前提是当前线程数小于maximumPoolSize
	 */
	private volatile int maximumPoolSize;
	
	/**
	 * The default rejected execution handler
	 */
	private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();
	
	/**
	 * Permission required for callers of shutdown and shutdownNow.
	 * We additionally require (see checkShutdownAccess) that callers
	 * have permission to actually interrupt threads in the worker set
	 * (as governed by Thread.interrupt, which relies on
	 * ThreadGroup.checkAccess, which in turn relies on
	 * SecurityManager.checkAccess). Shutdowns are attempted only if
	 * these checks pass.
	 * <p>
	 * All actual invocations of Thread.interrupt (see
	 * interruptIdleWorkers and interruptWorkers) ignore
	 * SecurityExceptions, meaning that the attempted interrupts
	 * silently fail. In the case of shutdown, they should not fail
	 * unless the SecurityManager has inconsistent policies, sometimes
	 * allowing access to a thread and sometimes not. In such cases,
	 * failure to actually interrupt threads may disable or delay full
	 * termination. Other uses of interruptIdleWorkers are advisory,
	 * and failure to actually interrupt will merely delay response to
	 * configuration changes so is not handled exceptionally.
	 */
	private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
	
	/* The context to be used when executing the finalizer, or null. */
	private final AccessControlContext acc;
	
	/**
	 * Class Worker mainly maintains interrupt control state for
	 * threads running tasks, along with other minor bookkeeping.
	 * This class opportunistically extends AbstractQueuedSynchronizer
	 * to simplify acquiring and releasing a lock surrounding each
	 * task execution.  This protects against interrupts that are
	 * intended to wake up a worker thread waiting for a task from
	 * instead interrupting a task being run.  We implement a simple
	 * non-reentrant mutual exclusion lock rather than use
	 * ReentrantLock because we do not want worker tasks to be able to
	 * reacquire the lock when they invoke pool control methods like
	 * setCorePoolSize.  Additionally, to suppress interrupts until
	 * the thread actually starts running tasks, we initialize lock
	 * state to a negative value, and clear it upon start (in
	 * runWorker).
	 *
	 * 线程池中的每一个线程被封装成一个Worker对象，ThreadPool维护的其实就是一组Worker对象
	 *
	 * Worker类继承了AQS, 并实现了Runnable接口, 注意其中的firstTask和thread属性: 
	 *   firstTask用它来保存传入的任务
	 *   thread是在调用构造方法时通过ThreadFactory来创建的线程, 是用来处理任务的线程
	 *
	 * Worker使用AQS来实现独占锁的功能. 为什么不使用ReentrantLock来实现呢？可以看到tryAcquire方法, 
	 * 它是不允许重入的, 而ReentrantLock是允许重入的: 
	 * 1: lock方法一旦获取了独占锁, 表示当前线程正在执行任务中
	 * 2: 如果正在执行任务, 则不应该中断线程
	 * 3: 如果该线程现在不是独占锁的状态, 也就是空闲的状态, 说明它没有在处理任务, 这时可以对该线程进行中断
	 * 4: 线程池在执行shutdown方法或tryTerminate方法时会调用interruptIdleWorkers方法来中断空闲的线程, 
	 *    interruptIdleWorkers方法会使用tryLock方法来判断线程池中的线程是否是空闲状态
	 * 5: 之所以设置为不可重入, 是因为我们不希望任务在调用像setCorePoolSize这样的线程池控制方法时重新获取锁. 
	 *    如果使用ReentrantLock, 它是可重入的, 这样如果在任务中调用了如setCorePoolSize这类线程池控制的方法, 会中断正在运行的线程
	 *
	 * 所以, Worker继承自AQS, 用于判断线程是否空闲以及是否可以被中断
	 */
	private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
		/**
		 * This class will never be serialized, but we provide a
		 * serialVersionUID to suppress a javac warning.
		 */
		private static final long serialVersionUID = 6138294804551838833L;
		
		/**
		 * Thread this worker is running in.  Null if factory fails.
		 */
		final Thread thread;
		/**
		 * Initial task to run.  Possibly null.
		 */
		Runnable firstTask;
		/**
		 * Per-thread task counter
		 */
		volatile long completedTasks;
		
		/**
		 * Creates with given first task and thread from ThreadFactory.
		 *
		 * 在调用构造方法时, 需要把任务传入, 这里通过 getThreadFactory().newThread(this); 来新建一个线程. 
		 * newThread方法传入的参数是this, 因为Worker本身继承了Runnable接口, 也就是一个线程, 
		 * 所以一个Worker对象在启动的时候会调用Worker类中的run方法。
		 *
		 * 正因为如此, 在runWorker方法中会先调用Worker对象的unlock方法将state设置为0
		 *
		 * @param firstTask the first task (null if none)
		 */
		Worker(Runnable firstTask) {
			/*
			 * 把state变量设置为-1, 为什么这么做呢? 是因为AQS中默认的state是0, 如果刚创建了一个Worker对象,
			 * 还没有执行任务时, 这时就不应该被中断, 看一下tryAquire方法.
			 * ryAcquire方法是根据state是否是0来判断的, 所以, setState(-1);
			 * 将state设置为-1是为了禁止在执行任务前对线程进行中断
			 *
			 * 其实就表示这个Worker还没有执行过任何任务, 有点还不完全初始化完成得意思
			 */
			setState(-1); // inhibit interrupts until runWorker
			this.firstTask = firstTask;
			this.thread = getThreadFactory().newThread(this);
		}
		
		/**
		 * Delegates main run loop to outer runWorker
		 * 将调用run()方法的任务交给外部类ThreadPoolExecutor去执行
		 */
		public void run() {
			runWorker(this);
		}
		
		// Lock methods
		//
		// The value 0 represents the unlocked state.
		// The value 1 represents the locked state.
		
		protected boolean isHeldExclusively() {
			return getState() != 0;
		}
		
		@Override
		protected boolean tryAcquire(int unused) {
			if (compareAndSetState(0, 1)) {
				setExclusiveOwnerThread(Thread.currentThread());
				return true;
			}
			return false;
		}
		
		protected boolean tryRelease(int unused) {
			setExclusiveOwnerThread(null);
			/*
			 * 在interruptIdleWorkers()方法会去调用Worker.tryLock()
			 * 里面会用compareAndSetState(0, 1)获取锁, 如果这里不设为0的话, 那边加锁是不可能成功的
			 */
			setState(0);
			return true;
		}
		
		public void lock() {
			acquire(1);
		}
		
		public boolean tryLock() {
			return tryAcquire(1);
		}
		
		public void unlock() {
			release(1);
		}
		
		public boolean isLocked() {
			return isHeldExclusively();
		}
		
		void interruptIfStarted() {
			Thread t;
			if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
				try {
					t.interrupt();
				} catch (SecurityException ignore) {
				}
			}
		}
	}
	
	/*
	 * Methods for setting control state
	 */
	
	/**
	 * Transitions runState to given target, or leaves it alone if
	 * already at least the given target.
	 *
	 * @param targetState the desired state, either SHUTDOWN or STOP
	 *                    (but not TIDYING or TERMINATED -- use tryTerminate for that)
	 */
	private void advanceRunState(int targetState) {
		for (; ; ) {
			int c = ctl.get();
			if (runStateAtLeast(c, targetState) ||
					ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c)))) {
				break;
			}
		}
	}
	
	/**
	 * Transitions to TERMINATED state if either (SHUTDOWN and pool
	 * and queue empty) or (STOP and pool empty).  If otherwise
	 * eligible to terminate but workerCount is nonzero, interrupts an
	 * idle worker to ensure that shutdown signals propagate. This
	 * method must be called following any action that might make
	 * termination possible -- reducing worker count or removing tasks
	 * from the queue during shutdown. The method is non-private to
	 * allow access from ScheduledThreadPoolExecutor.
	 */
	final void tryTerminate() {
		for (; ; ) {
			int c = ctl.get();
			if (isRunning(c) ||
					runStateAtLeast(c, TIDYING) ||
					(runStateOf(c) == SHUTDOWN && !workQueue.isEmpty())) {
				return;
			}
			if (workerCountOf(c) != 0) { // Eligible to terminate
				interruptIdleWorkers(ONLY_ONE);
				return;
			}
			
			final ReentrantLock mainLock = this.mainLock;
			mainLock.lock();
			try {
				if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
					try {
						terminated();
					} finally {
						ctl.set(ctlOf(TERMINATED, 0));
						termination.signalAll();
					}
					return;
				}
			} finally {
				mainLock.unlock();
			}
			// else retry on failed CAS
		}
	}
	
	/*
	 * Methods for controlling interrupts to worker threads.
	 */
	
	/**
	 * If there is a security manager, makes sure caller has
	 * permission to shut down threads in general (see shutdownPerm).
	 * If this passes, additionally makes sure the caller is allowed
	 * to interrupt each worker thread. This might not be true even if
	 * first check passed, if the SecurityManager treats some threads
	 * specially.
	 */
	private void checkShutdownAccess() {
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
			security.checkPermission(shutdownPerm);
			final ReentrantLock mainLock = this.mainLock;
			mainLock.lock();
			try {
				for (Worker w : workers) {
					security.checkAccess(w.thread);
				}
			} finally {
				mainLock.unlock();
			}
		}
	}
	
	/**
	 * Interrupts all threads, even if active. Ignores SecurityExceptions
	 * (in which case some threads may remain uninterrupted).
	 */
	private void interruptWorkers() {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			for (Worker w : workers) {
				w.interruptIfStarted();
			}
		} finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * Interrupts threads that might be waiting for tasks (as
	 * indicated by not being locked) so they can check for
	 * termination or configuration changes. Ignores
	 * SecurityExceptions (in which case some threads may remain
	 * uninterrupted).
	 *
	 * @param onlyOne If true, interrupt at most one worker. This is
	 *                called only from tryTerminate when termination is otherwise
	 *                enabled but there are still other workers.  In this case, at
	 *                most one waiting worker is interrupted to propagate shutdown
	 *                signals in case all threads are currently waiting.
	 *                Interrupting any arbitrary thread ensures that newly arriving
	 *                workers since shutdown began will also eventually exit.
	 *                To guarantee eventual termination, it suffices to always
	 *                interrupt only one idle worker, but shutdown() interrupts all
	 *                idle workers so that redundant workers exit promptly, not
	 *                waiting for a straggler task to finish.
	 */
	private void interruptIdleWorkers(boolean onlyOne) {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			for (Worker w : workers) {
				Thread t = w.thread;
				if (!t.isInterrupted() && w.tryLock()) {
					try {
						t.interrupt();
					} catch (SecurityException ignore) {
					} finally {
						w.unlock();
					}
				}
				if (onlyOne) {
					break;
				}
			}
		} finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * Common form of interruptIdleWorkers, to avoid having to
	 * remember what the boolean argument means.
	 */
	private void interruptIdleWorkers() {
		interruptIdleWorkers(false);
	}
	
	private static final boolean ONLY_ONE = true;
	
	/*
	 * Misc utilities, most of which are also exported to
	 * ScheduledThreadPoolExecutor
	 */
	
	/**
	 * Invokes the rejected execution handler for the given command.
	 * Package-protected for use by ScheduledThreadPoolExecutor.
	 */
	final void reject(Runnable command) {
		handler.rejectedExecution(command, this);
	}
	
	/**
	 * Performs any further cleanup following run state transition on
	 * invocation of shutdown.  A no-op here, but used by
	 * ScheduledThreadPoolExecutor to cancel delayed tasks.
	 */
	void onShutdown() {
	}
	
	/**
	 * State check needed by ScheduledThreadPoolExecutor to
	 * enable running tasks during shutdown.
	 *
	 * @param shutdownOK true if should return true if SHUTDOWN
	 */
	final boolean isRunningOrShutdown(boolean shutdownOK) {
		int rs = runStateOf(ctl.get());
		return rs == RUNNING || (rs == SHUTDOWN && shutdownOK);
	}
	
	/**
	 * Drains the task queue into a new list, normally using
	 * drainTo. But if the queue is a DelayQueue or any other kind of
	 * queue for which poll or drainTo may fail to remove some
	 * elements, it deletes them one by one.
	 */
	private List<Runnable> drainQueue() {
		BlockingQueue<Runnable> q = workQueue;
		ArrayList<Runnable> taskList = new ArrayList<Runnable>();
		q.drainTo(taskList);
		if (!q.isEmpty()) {
			for (Runnable r : q.toArray(new Runnable[0])) {
				if (q.remove(r)) {
					taskList.add(r);
				}
			}
		}
		return taskList;
	}
	
	/*
	 * Methods for creating, running and cleaning up after workers
	 */
	
	/**
	 * addWorker 方法的主要工作是在线程池中创建一个新的线程并执行
	 * firstTask 用于指定新增的线程执行的第一个任务
	 * core      true 表示在新增线程时会判断当前活动线程数是否少于corePoolSize
	 *           false表示新增线程前需要判断当前活动线程数是否少于maximumPoolSize
	 *
	 * Checks if a new worker can be added with respect to current
	 * pool state and the given bound (either core or maximum). 
	 *
	 * If so, the worker count is adjusted accordingly, and, if possible, 
	 * a new worker is created and started, running firstTask as its first task. 
	 * This method returns false if the pool is stopped or eligible to shut down. 
	 * It also returns false if the thread factory fails to create a thread when asked.  
	 * If the thread creation fails, either due to the thread factory returning null, 
	 * or due to an exception (typically OutOfMemoryError in Thread.start()), we roll back cleanly.
	 *
	 * @param firstTask the task the new thread should run first (or null if none). 
	 *                  Workers are created with an initial first task  (in method execute()) 
	 *                  to bypass queuing when there are fewer than corePoolSize threads (in which case we always start one),
	 *                  or when the queue is full (in which case we must bypass queue).
	 *                  Initially idle threads are usually created via
	 *                  prestartCoreThread or to replace other dying workers.
	 * @param core      if true use corePoolSize as bound, else
	 *                  maximumPoolSize. (A boolean indicator is used here rather than a
	 *                  value to ensure reads of fresh values after checking other pool
	 *                  state).
	 * @return true if successful
	 */
	private boolean addWorker(Runnable firstTask, boolean core) {
		retry:
		for (; ; ) {
			int c = ctl.get();
			int rs = runStateOf(c);
			
			/*
			 * Check if queue empty only if necessary.
			 *
			 * RUNNING   -1 << 29 取高3位 111(后面是29个0) 对应整型值:-536870912
			 * SHUTDOWN   0 << 29         000(后面是29个0) 对应整型值:0
			 * STOP       1 << 29         001(后面是29个0) 对应整型值:536870912
			 * TIDYING    2 << 29         010(后面是29个0) 对应整型值:1073741824
			 * TERMINATED 3 << 29         011(后面是29个0) 对应整型值:1610612736
			 *
			 * 如果 rs >= SHUTDOWN, 则表示此时不再接收新任务
			 *
			 * 接着判断以下3个条件, 只要有1个不满足, 就返回false:
			 * 1: rs == SHUTDOWN, 这时表示关闭状态, 不再接受新提交的任务, 但却可以继续处理阻塞队列中已保存的任务
			 * 2: firsTask为空
			 * 3: 阻塞队列不为空
			 *
			 * 首先考虑rs == SHUTDOWN的情况, 这种情况下不会接受新提交的任务, 所以在firstTask不为空的时候会返回false
			 * 然后, 如果firstTask为空, 并且workQueue也为空, 则返回false. 因为队列中已经没有任务了, 不需要再添加线程了
			 */
			if (rs >= SHUTDOWN &&
					!(rs == SHUTDOWN &&
							firstTask == null &&
							!workQueue.isEmpty())) {
				return false;
			}
			
			for (; ; ) {
				// 获取线程数
				int wc = workerCountOf(c);
				/*
				 * 如果wc超过CAPACITY, 也就是ctl的低29位的最大值(二进制是29个1), 返回false
				 *
				 * 这里的core是addWorker方法的第二个参数
				 * 如果为true表示根据corePoolSize来比较, 如果为false则根据maximumPoolSize来比较
				 */
				if (wc >= CAPACITY ||
						wc >= (core ? corePoolSize : maximumPoolSize)) {
					return false;
				}
				// 尝试增加workerCount, 如果成功, 则跳出第一个for循环
				if (compareAndIncrementWorkerCount(c)) {
					break retry; //直接跳转到第一行定义的retry:处
				}
				
				// 如果增加workerCount失败, 则重新获取ctl的值
				c = ctl.get();  // Re-read ctl
				// 如果当前的运行状态不等于rs, 说明状态已被改变, 返回第一个for循环继续执行
				if (runStateOf(c) != rs) {
					continue retry;
				}
				// else CAS failed due to workerCount change; retry inner loop
			}
		}
		
		boolean workerStarted = false;
		boolean workerAdded = false;
		Worker w = null;
		try {
			// 根据firstTask来创建Worker对象
			w = new Worker(firstTask);
			// 每一个Worker对象都会创建一个线程
			final Thread t = w.thread;
			if (t != null) {
				final ReentrantLock mainLock = this.mainLock;
				mainLock.lock();
				try {
					// Recheck while holding lock.
					// Back out on ThreadFactory failure or if
					// shut down before lock acquired.
					int rs = runStateOf(ctl.get());
					/*
					 * rs < SHUTDOWN 表示是RUNNING状态
					 * 如果rs是RUNNING状态或者rs是SHUTDOWN状态并且firstTask为null, 向线程池中添加线程,
					 * 因为在SHUTDOWN时不会在添加新的任务, 但还是会执行workQueue中的任务
					 */
					if (rs < SHUTDOWN ||
							(rs == SHUTDOWN && firstTask == null)) {
						if (t.isAlive()) // precheck that t is startable
						{
							throw new IllegalThreadStateException();
						}
						workers.add(w);
						int s = workers.size();
						// largestPoolSize记录着线程池中出现过的最大线程数量
						if (s > largestPoolSize) {
							largestPoolSize = s;
						}
						workerAdded = true;
					}
				} finally {
					mainLock.unlock();
				}
				if (workerAdded) {
					// 启动线程
					t.start();
					workerStarted = true;
				}
			}
		} finally {
			if (!workerStarted) {
				addWorkerFailed(w);
			}
		}
		return workerStarted;
	}
	
	/**
	 * Rolls back the worker thread creation.
	 * - removes worker from workers, if present
	 * - decrements worker count
	 * - rechecks for termination, in case the existence of this
	 * worker was holding up termination
	 */
	private void addWorkerFailed(Worker w) {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			if (w != null) {
				workers.remove(w);
			}
			decrementWorkerCount();
			tryTerminate();
		} finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * Performs cleanup and bookkeeping for a dying worker. Called
	 * only from worker threads. Unless completedAbruptly is set,
	 * assumes that workerCount has already been adjusted to account
	 * for exit.  This method removes thread from worker set, and
	 * possibly terminates the pool or replaces the worker if either
	 * it exited due to user task exception or if fewer than
	 * corePoolSize workers are running or queue is non-empty but
	 * there are no workers.
	 *
	 * @param w                 the worker
	 * @param completedAbruptly if the worker died due to user exception
	 */
	private void processWorkerExit(Worker w, boolean completedAbruptly) {
		if (completedAbruptly) // If abrupt, then workerCount wasn't adjusted
		{
			decrementWorkerCount();
		}
		
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			/*
			 * 线程终结时, 收集线程一共处理了多少任务
			 */
			completedTaskCount += w.completedTasks;
			workers.remove(w);
		} finally {
			mainLock.unlock();
		}
		
		tryTerminate();
		
		int c = ctl.get();
		if (runStateLessThan(c, STOP)) {//如果是RUNNING或者SHUTDOWN状态
			if (!completedAbruptly) {
				int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
				if (min == 0 && !workQueue.isEmpty()) { //workQueue中还有任务没有做完
					min = 1;
				}
				if (workerCountOf(c) >= min) {
					return; // replacement not needed
				}
			}
			addWorker(null, false); //添加一个Worker把任务做完
		}
	}
	
	/**
	 * Performs blocking or timed wait for a task, depending on
	 * current configuration settings, or returns null if this worker
	 * must exit because of any of:
	 * 1. There are more than maximumPoolSize workers (due to
	 * a call to setMaximumPoolSize).
	 * 2. The pool is stopped.
	 * 3. The pool is shutdown and the queue is empty.
	 * 4. This worker timed out waiting for a task, and timed-out
	 * workers are subject to termination (that is,
	 * {@code allowCoreThreadTimeOut || workerCount > corePoolSize})
	 * both before and after the timed wait, and if the queue is
	 * non-empty, this worker is not the last thread in the pool.
	 *
	 * getTask方法用来从阻塞队列中取任务
	 *
	 * @return task, or null if the worker must exit, in which case
	 * workerCount is decremented
	 */
	private Runnable getTask() {
		// 表示上次从阻塞队列中取任务时是否超时
		boolean timedOut = false; // Did the last poll() time out?
		
		for (; ; ) {
			int c = ctl.get();
			int rs = runStateOf(c);
			
			/*
			 * Check if queue empty only if necessary.
			 * 如果线程池状态 rs >= SHUTDOWN, 也就是非RUNNING状态, 再进行以下判断:
			 * 1: rs >= STOP, 线程池是否正在stop
			 * 2: 阻塞队列是否为空
			 *
			 * 如果以上条件满足, 则将workerCount减1并返回null
			 * 因为如果当前线程池状态的值是SHUTDOWN或以上时, 不允许再向阻塞队列中添加任务
			 */
			if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
				decrementWorkerCount();
				return null;
			}
			
			int wc = workerCountOf(c);
			
			/*
			 * Are workers subject to culling?
			 * timed变量用于判断是否需要进行超时控制
			 *
			 * allowCoreThreadTimeOut
			 * 默认是false, 也就是核心线程不允许进行超时; 如果允许超时, 一旦超时后, 核心线程与非核心线程都会结束生命周期
			 * wc > corePoolSize, 表示当前线程池中的线程数量大于核心线程数量; 对于超过核心线程数量的这些线程, 需要进行超时控制
			 *
			 * 在创建ThreadPoolExecutor时候, 会传一个参数keepAliveTime, 如果这个参数是0并且允许核心线程超时, 那么一旦阻塞队列
			 * 是空的, 那么核心线程和非核心线程都会停掉
			 */
			boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;
			/*
			 * wc > maximumPoolSize的情况是因为可能在此方法执行阶段同时执行了setMaximumPoolSize方法
			 * timed && timedOut 如果为true, 表示当前操作需要进行超时控制, 并且上次从阻塞队列中获取任务发生了超时
			 * 接下来判断, 如果有效线程数量大于1, 或者阻塞队列是空的, 那么尝试将workerCount减1
			 * 如果减1失败, 则返回重试
			 * 如果wc == 1时, 也就说明当前线程是线程池中唯一的一个线程了
			 */
			if ((wc > maximumPoolSize || (timed && timedOut))
					&& (wc > 1 || workQueue.isEmpty())) {
				if (compareAndDecrementWorkerCount(c)) { // 线程数减1
					return null;
				}
				continue;
			}
			
			try {
				/*
				 * 根据timed来判断, 如果为true, 则通过阻塞队列的poll方法进行超时控制, 如果在keepAliveTime时间内没有获取到任务, 则返回null;
				 * 否则通过take方法, 如果这时队列为空, 则take方法会阻塞直到队列不为空。
				 *
				 * 这里就是实现线程超时控制的代码
				 * workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) 会阻塞keepAliveTime, 如果超时了没用元素则返回null
				 * 如果拿到的Runnable是null, 那么timedOut就是true;
				 * 然后在下一轮循环的时候就会执行compareAndDecrementWorkerCount(c), 并且返回null
				 * 然后在runWorker()调用getTask()的地方, 会判断 getTask() != null, 否则就跳出while循环, 循环结束了线程池中这个线程也就停掉了
				 */
				Runnable r = timed ?
						workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
						workQueue.take();
				if (r != null) {
					return r;
				}
				// 如果 r == null, 说明已经超时, timedOut设置为true
				timedOut = true;
			} catch (InterruptedException retry) {
				// 如果获取任务时当前线程发生了中断，则设置timedOut为false并返回循环重试
				timedOut = false;
			}
		}
	}
	
	/**
	 * Main worker run loop.  Repeatedly gets tasks from queue and
	 * executes them, while coping with a number of issues:
	 * <p>
	 * 1. We may start out with an initial task, in which case we
	 * don't need to get the first one. Otherwise, as long as pool is
	 * running, we get tasks from getTask. If it returns null then the
	 * worker exits due to changed pool state or configuration
	 * parameters.  Other exits result from exception throws in
	 * external code, in which case completedAbruptly holds, which
	 * usually leads processWorkerExit to replace this thread.
	 * <p>
	 * 2. Before running any task, the lock is acquired to prevent
	 * other pool interrupts while the task is executing, and then we
	 * ensure that unless pool is stopping, this thread does not have
	 * its interrupt set.
	 * <p>
	 * 3. Each task run is preceded by a call to beforeExecute, which
	 * might throw an exception, in which case we cause thread to die
	 * (breaking loop with completedAbruptly true) without processing
	 * the task.
	 * <p>
	 * 4. Assuming beforeExecute completes normally, we run the task,
	 * gathering any of its thrown exceptions to send to afterExecute.
	 * We separately handle RuntimeException, Error (both of which the
	 * specs guarantee that we trap) and arbitrary Throwables.
	 * Because we cannot rethrow Throwables within Runnable.run, we
	 * wrap them within Errors on the way out (to the thread's
	 * UncaughtExceptionHandler).  Any thrown exception also
	 * conservatively causes thread to die.
	 * <p>
	 * 5. After task.run completes, we call afterExecute, which may
	 * also throw an exception, which will also cause thread to
	 * die. According to JLS Sec 14.20, this exception is the one that
	 * will be in effect even if task.run throws.
	 * <p>
	 * The net effect of the exception mechanics is that afterExecute
	 * and the thread's UncaughtExceptionHandler have as accurate
	 * information as we can provide about any problems encountered by
	 * user code.
	 *
	 * 在Worker类中的run方法调用了runWorker方法来执行任务
	 * 总结一下runWorker方法的执行过程:
	 * 1: while循环不断地通过getTask()方法获取任务
	 * 2: getTask()方法从阻塞队列中取任务
	 * 3: 如果线程池正在停止, 那么要保证当前线程是中断状态, 否则要保证当前线程不是中断状态
	 * 4: 调用task.run()执行任务
	 * 5: 如果task为null则跳出循环, 执行processWorkerExit()方法
	 * 6: runWorker方法执行完毕, 也代表着Worker中的run方法执行完毕, 销毁线程
	 * @param w the worker
	 */
	final void runWorker(Worker w) {
		Thread wt = Thread.currentThread();
		// 获取第一个任务
		Runnable task = w.firstTask;
		w.firstTask = null;
		/*
		 * 允许中断
		 * 在创建Worker的时候将AQS.state设为-1
		 * 这里调用worker.unlock()又将state设为0; exclusiveOwnerThread设为null
		 */
		w.unlock(); // allow interrupts
		// 是否因为异常退出循环
		boolean completedAbruptly = true;
		try {
			/*
			 * 如果task为空，则通过getTask来获取任务
			 *
			 * w.firstTask是在ThreadPoolExecutor.execute方法中添加的,
			 * 如: addWorker(command, true) 添加Worker的时候就带初始任务
			 *
			 * 如果是第一次循环, task != null就表示Worker带初始任务的, 先执行;
			 * 如果不带初始任务, 就从阻塞队列中取
			 *
			 * 从第二次循环开始都是从阻塞队列取任务, 如果没有任务的话线程阻塞,
			 * 线程池中的线程为什么可以一直存活的原因就在这里
			 *
			 * 当然, 如果运行线程超时, 那么getTask()会阻塞keepAliveTime, 然后返回null, 这时候这里就跳出while循环
			 * 线程结束
			 */
			while (task != null || (task = getTask()) != null) {
				/*
				 * 这又lock一遍, 将state设为1, exclusiveOwnerThread设为当前线程
				 */
				w.lock();
				/*
				 * If pool is stopping, ensure thread is interrupted;
				 * if not, ensure thread is not interrupted.
				 * This requires a recheck in second case to deal with shutdownNow race while clearing interrupt
				 * 这个if的目的是
				 * 1: 如果线程池正在停止, 那么要保证当前线程是中断状态
				 * 2: 如果不是的话, 则要保证当前线程不是中断状态
				 *
				 * 这里要考虑在执行该if语句期间可能也执行了shutdownNow方法, shutdownNow方法会把状态设置为STOP, 回顾一下STOP状态:
				 * 不能接受新任务, 也不处理队列中的任务, 会中断正在处理任务的线程.
				 * 在线程池处于RUNNING 或 SHUTDOWN 状态时, 调用 shutdownNow() 方法会使线程池进入到该状态
				 *
				 * STOP状态要中断线程池中的所有线程, 而这里使用Thread.interrupted()来判断是否中断是为了确保
				 * 在RUNNING 或者SHUTDOWN 状态时线程是非中断状态的, 因为Thread.interrupted()方法会复位中断的状态。
				 */
				if ((runStateAtLeast(ctl.get(), STOP) ||
						(Thread.interrupted() &&
								runStateAtLeast(ctl.get(), STOP))) &&
						!wt.isInterrupted()) {
					wt.interrupt();
				}
				try {
					beforeExecute(wt, task);
					Throwable thrown = null;
					try {
						task.run();
					} catch (RuntimeException x) {
						thrown = x;
						throw x;
					} catch (Error x) {
						thrown = x;
						throw x;
					} catch (Throwable x) {
						thrown = x;
						throw new Error(x);
					} finally {
						afterExecute(task, thrown);
					}
				} finally {
					task = null;
					w.completedTasks++;
					w.unlock();
				}
			}
			completedAbruptly = false;
		} finally {
			processWorkerExit(w, completedAbruptly);
		}
	}
	
	// Public constructors and methods
	
	/**
	 * Creates a new {@code ThreadPoolExecutor} with the given initial
	 * parameters and default thread factory and rejected execution handler.
	 * It may be more convenient to use one of the {@link Executors} factory
	 * methods instead of this general purpose constructor.
	 *
	 * @param corePoolSize    the number of threads to keep in the pool, even if they are idle,
	 *                        unless {@code allowCoreThreadTimeOut} is set
	 *                        1: 默认情况下, 线程池在初始的时候, 线程数为0, 除非调用了prestartCoreThread()
	 *                        或者prestartAllCoreThreads()方法来预创建线程; 前者是初始一个, 后者是初始全部.
	 *                        当接收到一个任务时, 如果线程池中存活的线程数小于corePoolSize核心线程, 则新建一个线程。
	 *                        2: 如果所有运行的核心线程都都在忙, 超出核心线程数的任务, 执行器更多地选择把任务放进队列, 而不是新建一个线程。
	 *                        3: 如果一个任务不能再提交到队列, 在不超出最大线程数量情况下, 会新建线程; 超出了就会报错.
	 * @param maximumPoolSize the maximum number of threads to allow in the pool
	 *                        线程池中线程最大数量
	 *                        线程池中的当前线程数目不会超过该值。如果队列中任务已满, 并且当前线程个数小于maximumPoolSize,
	 *                        那么会创建新的线程来执行任务. 这里值得一提的是largestPoolSize, 该变量记录了线程池在整个生命
	 *                        周期中曾经出现的最大线程个数, 因为线程池创建之后, 可以调用setMaximumPoolSize()改变运行的最大线程的数目。
	 * @param keepAliveTime   when the number of threads is greater than the core, this is the maximum time
	 *                        that excess idle threads will wait for new tasks before terminating.
	 *                        当线程池中的线程数大于corePoolSize时, 如果有线程闲置时间超过keepAliveTime, 那么该线程会被终止
	 * @param unit            the time unit for the {@code keepAliveTime} argument
	 * @param workQueue       the queue to use for holding tasks before they are executed.
	 *                        This queue will hold only the {@code Runnable} tasks submitted by the {@code execute} method.
	 *                        工作队列, 如果线程池中所有线程都在执行任务, 然后又有Runnable任务通过execute()提交进来, 则进入该队列排队
	 * @throws IllegalArgumentException if one of the following holds:<br>
	 *                                  {@code corePoolSize < 0}<br>
	 *                                  {@code keepAliveTime < 0}<br>
	 *                                  {@code maximumPoolSize <= 0}<br>
	 *                                  {@code maximumPoolSize < corePoolSize}
	 * @throws NullPointerException     if {@code workQueue} is null
	 */
	public LoserThreadPoolExecutor(int corePoolSize,
	                               int maximumPoolSize,
	                               long keepAliveTime,
	                               TimeUnit unit,
	                               BlockingQueue<Runnable> workQueue) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
				Executors.defaultThreadFactory(), defaultHandler);
	}
	
	/**
	 * Creates a new {@code ThreadPoolExecutor} with the given initial
	 * parameters and default rejected execution handler.
	 *
	 * @param corePoolSize    the number of threads to keep in the pool, even
	 *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
	 * @param maximumPoolSize the maximum number of threads to allow in the
	 *                        pool
	 * @param keepAliveTime   when the number of threads is greater than
	 *                        the core, this is the maximum time that excess idle threads
	 *                        will wait for new tasks before terminating.
	 * @param unit            the time unit for the {@code keepAliveTime} argument
	 * @param workQueue       the queue to use for holding tasks before they are
	 *                        executed.  This queue will hold only the {@code Runnable}
	 *                        tasks submitted by the {@code execute} method.
	 * @param threadFactory   the factory to use when the executor
	 *                        creates a new thread
	 * @throws IllegalArgumentException if one of the following holds:<br>
	 *                                  {@code corePoolSize < 0}<br>
	 *                                  {@code keepAliveTime < 0}<br>
	 *                                  {@code maximumPoolSize <= 0}<br>
	 *                                  {@code maximumPoolSize < corePoolSize}
	 * @throws NullPointerException     if {@code workQueue}
	 *                                  or {@code threadFactory} is null
	 */
	public LoserThreadPoolExecutor(int corePoolSize,
	                               int maximumPoolSize,
	                               long keepAliveTime,
	                               TimeUnit unit,
	                               BlockingQueue<Runnable> workQueue,
	                               ThreadFactory threadFactory) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
				threadFactory, defaultHandler);
	}
	
	/**
	 * Creates a new {@code ThreadPoolExecutor} with the given initial
	 * parameters and default thread factory.
	 *
	 * @param corePoolSize    the number of threads to keep in the pool, even
	 *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
	 * @param maximumPoolSize the maximum number of threads to allow in the
	 *                        pool
	 * @param keepAliveTime   when the number of threads is greater than
	 *                        the core, this is the maximum time that excess idle threads
	 *                        will wait for new tasks before terminating.
	 * @param unit            the time unit for the {@code keepAliveTime} argument
	 * @param workQueue       the queue to use for holding tasks before they are
	 *                        executed.  This queue will hold only the {@code Runnable}
	 *                        tasks submitted by the {@code execute} method.
	 * @param handler         the handler to use when execution is blocked
	 *                        because the thread bounds and queue capacities are reached
	 * @throws IllegalArgumentException if one of the following holds:<br>
	 *                                  {@code corePoolSize < 0}<br>
	 *                                  {@code keepAliveTime < 0}<br>
	 *                                  {@code maximumPoolSize <= 0}<br>
	 *                                  {@code maximumPoolSize < corePoolSize}
	 * @throws NullPointerException     if {@code workQueue}
	 *                                  or {@code handler} is null
	 */
	public LoserThreadPoolExecutor(int corePoolSize,
	                               int maximumPoolSize,
	                               long keepAliveTime,
	                               TimeUnit unit,
	                               BlockingQueue<Runnable> workQueue,
	                               RejectedExecutionHandler handler) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
				Executors.defaultThreadFactory(), handler);
	}
	
	/**
	 * Creates a new {@code ThreadPoolExecutor} with the given initial
	 * parameters.
	 *
	 * @param corePoolSize    the number of threads to keep in the pool, even
	 *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
	 * @param maximumPoolSize the maximum number of threads to allow in the
	 *                        pool
	 * @param keepAliveTime   when the number of threads is greater than
	 *                        the core, this is the maximum time that excess idle threads
	 *                        will wait for new tasks before terminating.
	 * @param unit            the time unit for the {@code keepAliveTime} argument
	 * @param workQueue       the queue to use for holding tasks before they are
	 *                        executed.  This queue will hold only the {@code Runnable}
	 *                        tasks submitted by the {@code execute} method.
	 * @param threadFactory   the factory to use when the executor
	 *                        creates a new thread
	 * @param handler         the handler to use when execution is blocked
	 *                        because the thread bounds and queue capacities are reached
	 * @throws IllegalArgumentException if one of the following holds:<br>
	 *                                  {@code corePoolSize < 0}<br>
	 *                                  {@code keepAliveTime < 0}<br>
	 *                                  {@code maximumPoolSize <= 0}<br>
	 *                                  {@code maximumPoolSize < corePoolSize}
	 * @throws NullPointerException     if {@code workQueue}
	 *                                  or {@code threadFactory} or {@code handler} is null
	 */
	public LoserThreadPoolExecutor(int corePoolSize,
	                               int maximumPoolSize,
	                               long keepAliveTime,
	                               TimeUnit unit,
	                               BlockingQueue<Runnable> workQueue,
	                               ThreadFactory threadFactory,
	                               RejectedExecutionHandler handler) {
		if (corePoolSize < 0 ||
				maximumPoolSize <= 0 ||
				maximumPoolSize < corePoolSize ||
				keepAliveTime < 0) {
			throw new IllegalArgumentException();
		}
		if (workQueue == null || threadFactory == null || handler == null) {
			throw new NullPointerException();
		}
		this.acc = System.getSecurityManager() == null ?
				null :
				AccessController.getContext();
		this.corePoolSize = corePoolSize;
		this.maximumPoolSize = maximumPoolSize;
		this.workQueue = workQueue;
		this.keepAliveTime = unit.toNanos(keepAliveTime);
		this.threadFactory = threadFactory;
		this.handler = handler;
	}
	
	/**
	 * Executes the given task sometime in the future.  The task
	 * may execute in a new thread or in an existing pooled thread.
	 * <p>
	 * If the task cannot be submitted for execution, either because this
	 * executor has been shutdown or because its capacity has been reached,
	 * the task is handled by the current {@code RejectedExecutionHandler}.
	 *
	 * @param command the task to execute
	 * @throws RejectedExecutionException at discretion of
	 *                                    {@code RejectedExecutionHandler}, if the task
	 *                                    cannot be accepted for execution
	 * @throws NullPointerException       if {@code command} is null
	 */
	public void execute(Runnable command) {
		if (command == null) {
			throw new NullPointerException();
		}
		/*
		 * Proceed in 3 steps:
		 *
		 * 1. If fewer than corePoolSize threads are running, try to
		 * start a new thread with the given command as its first
		 * task.  The call to addWorker atomically checks runState and
		 * workerCount, and so prevents false alarms that would add
		 * threads when it shouldn't, by returning false.
		 *
		 * 2. If a task can be successfully queued, then we still need
		 * to double-check whether we should have added a thread
		 * (because existing ones died since last checking) or that
		 * the pool shut down since entry into this method. So we
		 * recheck state and if necessary roll back the enqueuing if
		 * stopped, or start a new thread if there are none.
		 *
		 * 3. If we cannot queue task, then we try to add a new
		 * thread.  If it fails, we know we are shut down or saturated
		 * and so reject the task.
		 *
		 * clt记录着runState(高3位)和workerCount(低29位)
		 */
		int c = ctl.get();
		/*
		 * workerCountOf 方法取出低29位的值, 表示当前活动的线程数;
		 * 如果当前活动线程数小于corePoolSize, 则新建一个线程放入线程池中, 并把任务添加到该线程中
		 */
		if (workerCountOf(c) < corePoolSize) {
			/*
			 * core参数表示创建的是否是核心线程
			 * 同时可以判断添加线程的数量是根据corePoolSize来判断还是maximumPoolSize来判断
			 * 如果为true,  根据corePoolSize来判断
			 * 如果为false, 则根据maximumPoolSize来判断
			 *
			 * command就是实现了Runnable接口的类, 也就是你要交给线程池去执行的任务
			 */
			if (addWorker(command, true)) { //这里的command就是Worker的初始任务, 即Worker类的Runnable firstTask
				return;
			}
			//如果添加失败，则重新获取ctl值
			c = ctl.get();
		}
		/*
		 * 如果当前线程池是运行状态并且任务添加到队列成功
		 */
		if (isRunning(c) && workQueue.offer(command)) {
			//重新获取ctl值
			int recheck = ctl.get();
			/*
			 * 再次判断线程池的运行状态, 如果不是运行状态, 由于之前已经把command添加到workQueue中了, 这时需要移除该command.
			 * 执行过后通过handler使用拒绝策略对该任务进行处理, 整个方法返回
			 */
			if (!isRunning(recheck) && remove(command)) {
				reject(command);
			}
			/*
			 * 获取线程池中的有效线程数, 如果数量是0, 则执行addWorker方法
			 * 这里传入的参数表示：
			 * 1. 第一个参数为null, 表示在线程池中创建一个线程, 但不去启动;
			 * 2. 第二个参数为false, 将线程池的有限线程数量的上限设置为maximumPoolSize, 添加线程时根据maximumPoolSize来判断;
			 *
			 * 如果判断workerCount大于0, 则直接返回, 在workQueue中新增的command会在将来的某个时刻被执行。
			 */
			else if (workerCountOf(recheck) == 0) {
				addWorker(null, false);
			}
		}
		/*
		 * 如果执行到这里, 有两种情况:
		 * 1. 线程池已经不是RUNNING状态
		 * 2. 线程池是RUNNING状态, 但 workerCount >= corePoolSize 并且workQueue已满
		 *
		 * 这时, 再次调用 addWorker方法, 但第二个参数传入为false, 将线程池的有限线程数量的上限设置为maximumPoolSize, 如果失败则拒绝该任务
		 */
		else if (!addWorker(command, false)) {
			reject(command);
		}
	}
	
	/**
	 * Initiates an orderly shutdown in which previously submitted
	 * tasks are executed, but no new tasks will be accepted.
	 * Invocation has no additional effect if already shut down.
	 *
	 * <p>This method does not wait for previously submitted tasks to
	 * complete execution.  Use {@link #awaitTermination awaitTermination}
	 * to do that.
	 *
	 * @throws SecurityException {@inheritDoc}
	 */
	public void shutdown() {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			checkShutdownAccess();
			advanceRunState(SHUTDOWN);
			interruptIdleWorkers();
			onShutdown(); // hook for ScheduledThreadPoolExecutor
		} finally {
			mainLock.unlock();
		}
		tryTerminate();
	}
	
	/**
	 * Attempts to stop all actively executing tasks, halts the
	 * processing of waiting tasks, and returns a list of the tasks
	 * that were awaiting execution. These tasks are drained (removed)
	 * from the task queue upon return from this method.
	 *
	 * <p>This method does not wait for actively executing tasks to
	 * terminate.  Use {@link #awaitTermination awaitTermination} to
	 * do that.
	 *
	 * <p>There are no guarantees beyond best-effort attempts to stop
	 * processing actively executing tasks.  This implementation
	 * cancels tasks via {@link Thread#interrupt}, so any task that
	 * fails to respond to interrupts may never terminate.
	 *
	 * @throws SecurityException {@inheritDoc}
	 */
	public List<Runnable> shutdownNow() {
		List<Runnable> tasks;
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			checkShutdownAccess();
			advanceRunState(STOP);
			interruptWorkers();
			tasks = drainQueue();
		} finally {
			mainLock.unlock();
		}
		tryTerminate();
		return tasks;
	}
	
	public boolean isShutdown() {
		return !isRunning(ctl.get());
	}
	
	/**
	 * Returns true if this executor is in the process of terminating
	 * after {@link #shutdown} or {@link #shutdownNow} but has not
	 * completely terminated.  This method may be useful for
	 * debugging. A return of {@code true} reported a sufficient
	 * period after shutdown may indicate that submitted tasks have
	 * ignored or suppressed interruption, causing this executor not
	 * to properly terminate.
	 *
	 * @return {@code true} if terminating but not yet terminated
	 */
	public boolean isTerminating() {
		int c = ctl.get();
		return !isRunning(c) && runStateLessThan(c, TERMINATED);
	}
	
	public boolean isTerminated() {
		return runStateAtLeast(ctl.get(), TERMINATED);
	}
	
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			for (; ; ) {
				if (runStateAtLeast(ctl.get(), TERMINATED)) {
					return true;
				}
				if (nanos <= 0) {
					return false;
				}
				nanos = termination.awaitNanos(nanos);
			}
		} finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * Invokes {@code shutdown} when this executor is no longer
	 * referenced and it has no threads.
	 */
	protected void finalize() {
		SecurityManager sm = System.getSecurityManager();
		if (sm == null || acc == null) {
			shutdown();
		} else {
			PrivilegedAction<Void> pa = () -> {
				shutdown();
				return null;
			};
			AccessController.doPrivileged(pa, acc);
		}
	}
	
	/**
	 * Sets the thread factory used to create new threads.
	 *
	 * @param threadFactory the new thread factory
	 * @throws NullPointerException if threadFactory is null
	 * @see #getThreadFactory
	 */
	public void setThreadFactory(ThreadFactory threadFactory) {
		if (threadFactory == null) {
			throw new NullPointerException();
		}
		this.threadFactory = threadFactory;
	}
	
	/**
	 * Returns the thread factory used to create new threads.
	 *
	 * @return the current thread factory
	 * @see #setThreadFactory(ThreadFactory)
	 */
	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}
	
	/**
	 * Sets a new handler for unexecutable tasks.
	 *
	 * @param handler the new handler
	 * @throws NullPointerException if handler is null
	 * @see #getRejectedExecutionHandler
	 */
	public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
		if (handler == null) {
			throw new NullPointerException();
		}
		this.handler = handler;
	}
	
	/**
	 * Returns the current handler for unexecutable tasks.
	 *
	 * @return the current handler
	 * @see #setRejectedExecutionHandler(RejectedExecutionHandler)
	 */
	public RejectedExecutionHandler getRejectedExecutionHandler() {
		return handler;
	}
	
	/**
	 * Sets the core number of threads.  This overrides any value set
	 * in the constructor.  If the new value is smaller than the
	 * current value, excess existing threads will be terminated when
	 * they next become idle.  If larger, new threads will, if needed,
	 * be started to execute any queued tasks.
	 *
	 * @param corePoolSize the new core size
	 * @throws IllegalArgumentException if {@code corePoolSize < 0}
	 * @see #getCorePoolSize
	 */
	public void setCorePoolSize(int corePoolSize) {
		if (corePoolSize < 0) {
			throw new IllegalArgumentException();
		}
		int delta = corePoolSize - this.corePoolSize;
		this.corePoolSize = corePoolSize;
		if (workerCountOf(ctl.get()) > corePoolSize) {
			interruptIdleWorkers();
		} else if (delta > 0) {
			// We don't really know how many new threads are "needed".
			// As a heuristic, prestart enough new workers (up to new
			// core size) to handle the current number of tasks in
			// queue, but stop if queue becomes empty while doing so.
			int k = Math.min(delta, workQueue.size());
			while (k-- > 0 && addWorker(null, true)) {
				if (workQueue.isEmpty()) {
					break;
				}
			}
		}
	}
	
	/**
	 * Returns the core number of threads.
	 *
	 * @return the core number of threads
	 * @see #setCorePoolSize
	 */
	public int getCorePoolSize() {
		return corePoolSize;
	}
	
	/**
	 * Starts a core thread, causing it to idly wait for work. This
	 * overrides the default policy of starting core threads only when
	 * new tasks are executed. This method will return {@code false}
	 * if all core threads have already been started.
	 *
	 * @return {@code true} if a thread was started
	 */
	public boolean prestartCoreThread() {
		return workerCountOf(ctl.get()) < corePoolSize &&
				addWorker(null, true);
	}
	
	/**
	 * Same as prestartCoreThread except arranges that at least one
	 * thread is started even if corePoolSize is 0.
	 */
	void ensurePrestart() {
		int wc = workerCountOf(ctl.get());
		if (wc < corePoolSize) {
			addWorker(null, true);
		} else if (wc == 0) {
			addWorker(null, false);
		}
	}
	
	/**
	 * Starts all core threads, causing them to idly wait for work. This
	 * overrides the default policy of starting core threads only when
	 * new tasks are executed.
	 *
	 * @return the number of threads started
	 */
	public int prestartAllCoreThreads() {
		int n = 0;
		while (addWorker(null, true)) {
			++n;
		}
		return n;
	}
	
	/**
	 * Returns true if this pool allows core threads to time out and
	 * terminate if no tasks arrive within the keepAlive time, being
	 * replaced if needed when new tasks arrive. When true, the same
	 * keep-alive policy applying to non-core threads applies also to
	 * core threads. When false (the default), core threads are never
	 * terminated due to lack of incoming tasks.
	 *
	 * @return {@code true} if core threads are allowed to time out,
	 * else {@code false}
	 * @since 1.6
	 */
	public boolean allowsCoreThreadTimeOut() {
		return allowCoreThreadTimeOut;
	}
	
	/**
	 * Sets the policy governing whether core threads may time out and
	 * terminate if no tasks arrive within the keep-alive time, being
	 * replaced if needed when new tasks arrive. When false, core
	 * threads are never terminated due to lack of incoming
	 * tasks. When true, the same keep-alive policy applying to
	 * non-core threads applies also to core threads. To avoid
	 * continual thread replacement, the keep-alive time must be
	 * greater than zero when setting {@code true}. This method
	 * should in general be called before the pool is actively used.
	 *
	 * @param value {@code true} if should time out, else {@code false}
	 * @throws IllegalArgumentException if value is {@code true}
	 *                                  and the current keep-alive time is not greater than zero
	 * @since 1.6
	 */
	public void allowCoreThreadTimeOut(boolean value) {
		if (value && keepAliveTime <= 0) {
			throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
		}
		if (value != allowCoreThreadTimeOut) {
			allowCoreThreadTimeOut = value;
			if (value) {
				interruptIdleWorkers();
			}
		}
	}
	
	/**
	 * Sets the maximum allowed number of threads. This overrides any
	 * value set in the constructor. If the new value is smaller than
	 * the current value, excess existing threads will be
	 * terminated when they next become idle.
	 *
	 * @param maximumPoolSize the new maximum
	 * @throws IllegalArgumentException if the new maximum is
	 *                                  less than or equal to zero, or
	 *                                  less than the {@linkplain #getCorePoolSize core pool size}
	 * @see #getMaximumPoolSize
	 */
	public void setMaximumPoolSize(int maximumPoolSize) {
		if (maximumPoolSize <= 0 || maximumPoolSize < corePoolSize) {
			throw new IllegalArgumentException();
		}
		this.maximumPoolSize = maximumPoolSize;
		if (workerCountOf(ctl.get()) > maximumPoolSize) {
			interruptIdleWorkers();
		}
	}
	
	/**
	 * Returns the maximum allowed number of threads.
	 *
	 * @return the maximum allowed number of threads
	 * @see #setMaximumPoolSize
	 */
	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}
	
	/**
	 * Sets the time limit for which threads may remain idle before
	 * being terminated.  If there are more than the core number of
	 * threads currently in the pool, after waiting this amount of
	 * time without processing a task, excess threads will be
	 * terminated.  This overrides any value set in the constructor.
	 *
	 * @param time the time to wait.  A time value of zero will cause
	 *             excess threads to terminate immediately after executing tasks.
	 * @param unit the time unit of the {@code time} argument
	 * @throws IllegalArgumentException if {@code time} less than zero or
	 *                                  if {@code time} is zero and {@code allowsCoreThreadTimeOut}
	 * @see #getKeepAliveTime(TimeUnit)
	 */
	public void setKeepAliveTime(long time, TimeUnit unit) {
		if (time < 0) {
			throw new IllegalArgumentException();
		}
		if (time == 0 && allowsCoreThreadTimeOut()) {
			throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
		}
		long keepAliveTime = unit.toNanos(time);
		long delta = keepAliveTime - this.keepAliveTime;
		this.keepAliveTime = keepAliveTime;
		if (delta < 0) {
			interruptIdleWorkers();
		}
	}
	
	/**
	 * Returns the thread keep-alive time, which is the amount of time
	 * that threads in excess of the core pool size may remain
	 * idle before being terminated.
	 *
	 * @param unit the desired time unit of the result
	 * @return the time limit
	 * @see #setKeepAliveTime(long, TimeUnit)
	 */
	public long getKeepAliveTime(TimeUnit unit) {
		return unit.convert(keepAliveTime, TimeUnit.NANOSECONDS);
	}
	
	/* User-level queue utilities */
	
	/**
	 * Returns the task queue used by this executor. Access to the
	 * task queue is intended primarily for debugging and monitoring.
	 * This queue may be in active use.  Retrieving the task queue
	 * does not prevent queued tasks from executing.
	 *
	 * @return the task queue
	 */
	public BlockingQueue<Runnable> getQueue() {
		return workQueue;
	}
	
	/**
	 * Removes this task from the executor's internal queue if it is
	 * present, thus causing it not to be run if it has not already
	 * started.
	 *
	 * <p>This method may be useful as one part of a cancellation
	 * scheme.  It may fail to remove tasks that have been converted
	 * into other forms before being placed on the internal queue. For
	 * example, a task entered using {@code submit} might be
	 * converted into a form that maintains {@code Future} status.
	 * However, in such cases, method {@link #purge} may be used to
	 * remove those Futures that have been cancelled.
	 *
	 * @param task the task to remove
	 * @return {@code true} if the task was removed
	 */
	public boolean remove(Runnable task) {
		boolean removed = workQueue.remove(task);
		tryTerminate(); // In case SHUTDOWN and now empty
		return removed;
	}
	
	/**
	 * Tries to remove from the work queue all {@link Future}
	 * tasks that have been cancelled. This method can be useful as a
	 * storage reclamation operation, that has no other impact on
	 * functionality. Cancelled tasks are never executed, but may
	 * accumulate in work queues until worker threads can actively
	 * remove them. Invoking this method instead tries to remove them now.
	 * However, this method may fail to remove tasks in
	 * the presence of interference by other threads.
	 */
	public void purge() {
		final BlockingQueue<Runnable> q = workQueue;
		try {
			Iterator<Runnable> it = q.iterator();
			while (it.hasNext()) {
				Runnable r = it.next();
				if (r instanceof Future<?> && ((Future<?>) r).isCancelled()) {
					it.remove();
				}
			}
		} catch (ConcurrentModificationException fallThrough) {
			// Take slow path if we encounter interference during traversal.
			// Make copy for traversal and call remove for cancelled entries.
			// The slow path is more likely to be O(N*N).
			for (Object r : q.toArray()) {
				if (r instanceof Future<?> && ((Future<?>) r).isCancelled()) {
					q.remove(r);
				}
			}
		}
		
		tryTerminate(); // In case SHUTDOWN and now empty
	}
	
	/* Statistics */
	
	/**
	 * 线程池当前的线程数
	 * Returns the current number of threads in the pool.
	 *
	 * @return the number of threads
	 */
	public int getPoolSize() {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			// Remove rare and surprising possibility of
			// isTerminated() && getPoolSize() > 0
			return runStateAtLeast(ctl.get(), TIDYING) ? 0
					: workers.size();
		} finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * 线程池中正在执行任务的线程数量
	 * Returns the approximate number of threads that are actively
	 * executing tasks.
	 *
	 * @return the number of threads
	 */
	public int getActiveCount() {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			int n = 0;
			for (Worker w : workers) {
				if (w.isLocked()) {
					++n;
				}
			}
			return n;
		} finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * Returns the largest number of threads that have ever
	 * simultaneously been in the pool.
	 *
	 * @return the number of threads
	 */
	public int getLargestPoolSize() {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			return largestPoolSize;
		} finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * 线程池已执行与未执行的任务总数
	 * Returns the approximate total number of tasks that have ever been
	 * scheduled for execution. Because the states of tasks and
	 * threads may change dynamically during computation, the returned
	 * value is only an approximation.
	 *
	 * @return the number of tasks
	 */
	public long getTaskCount() {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			long n = completedTaskCount;
			for (Worker w : workers) {
				n += w.completedTasks;
				if (w.isLocked()) {
					++n;
				}
			}
			return n + workQueue.size();
		} finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * 已完成的任务数
	 * Returns the approximate total number of tasks that have
	 * completed execution. Because the states of tasks and threads
	 * may change dynamically during computation, the returned value
	 * is only an approximation, but one that does not ever decrease
	 * across successive calls.
	 *
	 * @return the number of tasks
	 */
	public long getCompletedTaskCount() {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			long n = completedTaskCount;
			for (Worker w : workers) {
				n += w.completedTasks;
			}
			return n;
		} finally {
			mainLock.unlock();
		}
	}
	
	/**
	 * Returns a string identifying this pool, as well as its state,
	 * including indications of run state and estimated worker and
	 * task counts.
	 *
	 * @return a string identifying this pool, as well as its state
	 */
	public String toString() {
		long ncompleted;
		int nworkers, nactive;
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			ncompleted = completedTaskCount;
			nactive = 0;
			nworkers = workers.size();
			for (Worker w : workers) {
				ncompleted += w.completedTasks;
				if (w.isLocked()) {
					++nactive;
				}
			}
		} finally {
			mainLock.unlock();
		}
		int c = ctl.get();
		String rs = (runStateLessThan(c, SHUTDOWN) ? "Running" :
				(runStateAtLeast(c, TERMINATED) ? "Terminated" :
						"Shutting down"));
		return super.toString() +
				"[" + rs +
				", pool size = " + nworkers +
				", active threads = " + nactive +
				", queued tasks = " + workQueue.size() +
				", completed tasks = " + ncompleted +
				"]";
	}
	
	/* Extension hooks */
	
	/**
	 * Method invoked prior to executing the given Runnable in the
	 * given thread.  This method is invoked by thread {@code t} that
	 * will execute task {@code r}, and may be used to re-initialize
	 * ThreadLocals, or to perform logging.
	 *
	 * <p>This implementation does nothing, but may be customized in
	 * subclasses. Note: To properly nest multiple overridings, subclasses
	 * should generally invoke {@code super.beforeExecute} at the end of
	 * this method.
	 *
	 * @param t the thread that will run task {@code r}
	 * @param r the task that will be executed
	 */
	protected void beforeExecute(Thread t, Runnable r) {
	}
	
	/**
	 * Method invoked upon completion of execution of the given Runnable.
	 * This method is invoked by the thread that executed the task. If
	 * non-null, the Throwable is the uncaught {@code RuntimeException}
	 * or {@code Error} that caused execution to terminate abruptly.
	 *
	 * <p>This implementation does nothing, but may be customized in
	 * subclasses. Note: To properly nest multiple overridings, subclasses
	 * should generally invoke {@code super.afterExecute} at the
	 * beginning of this method.
	 *
	 * <p><b>Note:</b> When actions are enclosed in tasks (such as
	 * {@link FutureTask}) either explicitly or via methods such as
	 * {@code submit}, these task objects catch and maintain
	 * computational exceptions, and so they do not cause abrupt
	 * termination, and the internal exceptions are <em>not</em>
	 * passed to this method. If you would like to trap both kinds of
	 * failures in this method, you can further probe for such cases,
	 * as in this sample subclass that prints either the direct cause
	 * or the underlying exception if a task has been aborted:
	 *
	 * <pre> {@code
	 * class ExtendedExecutor extends ThreadPoolExecutor {
	 *   // ...
	 *   protected void afterExecute(Runnable r, Throwable t) {
	 *     super.afterExecute(r, t);
	 *     if (t == null && r instanceof Future<?>) {
	 *       try {
	 *         Object result = ((Future<?>) r).get();
	 *       } catch (CancellationException ce) {
	 *           t = ce;
	 *       } catch (ExecutionException ee) {
	 *           t = ee.getCause();
	 *       } catch (InterruptedException ie) {
	 *           Thread.currentThread().interrupt(); // ignore/reset
	 *       }
	 *     }
	 *     if (t != null)
	 *       System.out.println(t);
	 *   }
	 * }}</pre>
	 *
	 * @param r the runnable that has completed
	 * @param t the exception that caused termination, or null if
	 *          execution completed normally
	 */
	protected void afterExecute(Runnable r, Throwable t) {
	}
	
	/**
	 * Method invoked when the Executor has terminated.  Default
	 * implementation does nothing. Note: To properly nest multiple
	 * overridings, subclasses should generally invoke
	 * {@code super.terminated} within this method.
	 */
	protected void terminated() {
	}
	
	/* Predefined RejectedExecutionHandlers */
	
	/**
	 * A handler for rejected tasks that runs the rejected task
	 * directly in the calling thread of the {@code execute} method,
	 * unless the executor has been shut down, in which case the task
	 * is discarded.
	 */
	public static class CallerRunsPolicy implements RejectedExecutionHandler {
		/**
		 * Creates a {@code CallerRunsPolicy}.
		 */
		public CallerRunsPolicy() {
		}
		
		/**
		 * Executes task r in the caller's thread, unless the executor
		 * has been shut down, in which case the task is discarded.
		 *
		 * @param r the runnable task requested to be executed
		 * @param e the executor attempting to execute this task
		 */
		public void rejectedExecution(Runnable r, LoserThreadPoolExecutor e) {
			if (!e.isShutdown()) {
				r.run();
			}
		}
	}
	
	/**
	 * A handler for rejected tasks that throws a
	 * {@code RejectedExecutionException}.
	 */
	public static class AbortPolicy implements RejectedExecutionHandler {
		/**
		 * Creates an {@code AbortPolicy}.
		 */
		public AbortPolicy() {
		}
		
		/**
		 * Always throws RejectedExecutionException.
		 *
		 * @param r the runnable task requested to be executed
		 * @param e the executor attempting to execute this task
		 * @throws RejectedExecutionException always
		 */
		public void rejectedExecution(Runnable r, LoserThreadPoolExecutor e) {
			throw new RejectedExecutionException("Task " + r.toString() +
					" rejected from " +
					e.toString());
		}
	}
	
	/**
	 * A handler for rejected tasks that silently discards the
	 * rejected task.
	 */
	public static class DiscardPolicy implements RejectedExecutionHandler {
		/**
		 * Creates a {@code DiscardPolicy}.
		 */
		public DiscardPolicy() {
		}
		
		/**
		 * Does nothing, which has the effect of discarding task r.
		 *
		 * @param r the runnable task requested to be executed
		 * @param e the executor attempting to execute this task
		 */
		public void rejectedExecution(Runnable r, LoserThreadPoolExecutor e) {
		}
	}
	
	/**
	 * A handler for rejected tasks that discards the oldest unhandled
	 * request and then retries {@code execute}, unless the executor
	 * is shut down, in which case the task is discarded.
	 */
	public static class DiscardOldestPolicy implements RejectedExecutionHandler {
		/**
		 * Creates a {@code DiscardOldestPolicy} for the given executor.
		 */
		public DiscardOldestPolicy() {
		}
		
		/**
		 * Obtains and ignores the next task that the executor
		 * would otherwise execute, if one is immediately available,
		 * and then retries execution of task r, unless the executor
		 * is shut down, in which case task r is instead discarded.
		 *
		 * @param r the runnable task requested to be executed
		 * @param e the executor attempting to execute this task
		 */
		public void rejectedExecution(Runnable r, LoserThreadPoolExecutor e) {
			if (!e.isShutdown()) {
				e.getQueue().poll();
				e.execute(r);
			}
		}
	}
}
