package com.loserico.concurrent.aqs;

import com.loserico.unsafe.LoserUnsafe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * https://xie.infoq.cn/article/d35c71eb30dee7ee795da2827
 * https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html
 * AQS同步器框架源码
 * <p>
 * Copyright: Copyright (c) 2019-11-19 16:28
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class LoserAbstractQueuedSynchronizer extends LoserAbstractOwnableSynchronizer implements Serializable {
	
	private static final long serialVersionUID = 7373984972572414691L;
	
	/**
	 * Creates a new {@code AbstractQueuedSynchronizer} instance
	 * with initial synchronization state of zero.
	 */
	protected LoserAbstractQueuedSynchronizer() {
	}
	
	/**
	 * Wait queue node class.
	 *
	 * <p>The wait queue is a variant of a "CLH" (Craig, Landin, and
	 * Hagersten) lock queue. CLH locks are normally used for
	 * spinlocks.  We instead use them for blocking synchronizers, but
	 * use the same basic tactic of holding some of the control
	 * information about a thread in the predecessor of its node.  A
	 * "status" field in each node keeps track of whether a thread
	 * should block.  A node is signalled when its predecessor
	 * releases.  Each node of the queue otherwise serves as a
	 * specific-notification-style monitor holding a single waiting
	 * thread. The status field does NOT control whether threads are
	 * granted locks etc though.  A thread may try to acquire if it is
	 * first in the queue. But being first does not guarantee success;
	 * it only gives the right to contend.  So the currently released
	 * contender thread may need to rewait.
	 *
	 * <p>To enqueue into a CLH lock, you atomically splice it in as new
	 * tail. To dequeue, you just set the head field.
	 * <pre>
	 *      +------+  prev +-----+       +-----+
	 * head |      | <---- |     | <---- |     |  tail
	 *      +------+       +-----+       +-----+
	 * </pre>
	 *
	 * <p>Insertion into a CLH queue requires only a single atomic
	 * operation on "tail", so there is a simple atomic point of
	 * demarcation from unqueued to queued. Similarly, dequeuing
	 * involves only updating the "head". However, it takes a bit
	 * more work for nodes to determine who their successors are,
	 * in part to deal with possible cancellation due to timeouts
	 * and interrupts.
	 *
	 * <p>The "prev" links (not used in original CLH locks), are mainly
	 * needed to handle cancellation. If a node is cancelled, its
	 * successor is (normally) relinked to a non-cancelled
	 * predecessor. For explanation of similar mechanics in the case
	 * of spin locks, see the papers by Scott and Scherer at
	 * http://www.cs.rochester.edu/u/scott/synchronization/
	 *
	 * <p>We also use "next" links to implement blocking mechanics.
	 * The thread id for each node is kept in its own node, so a
	 * predecessor signals the next node to wake up by traversing
	 * next link to determine which thread it is.  Determination of
	 * successor must avoid races with newly queued nodes to set
	 * the "next" fields of their predecessors.  This is solved
	 * when necessary by checking backwards from the atomically
	 * updated "tail" when a node's successor appears to be null.
	 * (Or, said differently, the next-links are an optimization
	 * so that we don't usually need a backward scan.)
	 *
	 * <p>Cancellation introduces some conservatism to the basic
	 * algorithms.  Since we must poll for cancellation of other
	 * nodes, we can miss noticing whether a cancelled node is
	 * ahead or behind us. This is dealt with by always unparking
	 * successors upon cancellation, allowing them to stabilize on
	 * a new predecessor, unless we can identify an uncancelled
	 * predecessor who will carry this responsibility.
	 *
	 * <p>CLH queues need a dummy header node to get started. But
	 * we don't create them on construction, because it would be wasted
	 * effort if there is never contention. Instead, the node
	 * is constructed and head and tail pointers are set upon first
	 * contention.
	 *
	 * <p>Threads waiting on Conditions use the same nodes, but
	 * use an additional link. Conditions only need to link nodes
	 * in simple (non-concurrent) linked queues because they are
	 * only accessed when exclusively held.  Upon await, a node is
	 * inserted into a condition queue.  Upon signal, the node is
	 * transferred to the main queue.  A special value of status
	 * field is used to mark which queue a node is on.
	 *
	 * <p>Thanks go to Dave Dice, Mark Moir, Victor Luchangco, Bill
	 * Scherer and Michael Scott, along with members of JSR-166
	 * expert group, for helpful ideas, discussions, and critiques
	 * on the design of this class.
	 * <p>
	 */
	static final class Node {
		/**
		 * 持有的锁类型
		 * 表示共享锁
		 * 在AQS的内部实现中用于区分节点是在共享模式下等待还是在独占模式下等待
		 * Marker to indicate a node is waiting in shared mode
		 * 这个标记告诉AQS的释放方法, 当状态被释放时, 应该考虑唤醒其他在共享模式下等待的线程。
		 *
		 * 如果一个节点是以SHARED为标记的，AQS知道它后面的节点可能也处于共享模式，并且可能需要被同时释放或唤醒。
		 * 这有助于实现诸如Semaphore或CountDownLatch这样的同步器，其中多个线程可能同时等待资源变得可用。
		 */
		static final Node SHARED = new Node();
		/**
		 * 表示排它锁
		 * Marker to indicate a node is waiting in exclusive mode
		 * 在Node类中，EXCLUSIVE被定义为null。这意味着当一个节点是以独占模式入队时,
		 * 其Node类型的nextWaiter字段将被设置为null。
		 * 这是用来区分独占模式和共享模式的一个标志。
		 */
		static final Node EXCLUSIVE = null;
		
		/**
		 * 代表当前节点已经取消了
		 * waitStatus value to indicate thread has cancelled <br/>
		 * 在同步队列中等待的线程等待超时或者被中断<br/>
		 * 线程生命周期已经结束了<br/>
		 * 调用了这个线程的intercept()方法?
		 */
		static final int CANCELLED = 1;
		
		/**
		 * waitStatus value to indicate successor's thread needs unparking <p/>
		 * 代表当前节点的后继节点, 可以挂起线程, 后续我会唤醒我的后继节点
		 * 
		 * 默认的状态0代表这个节点刚刚创建, 什么事都还没做呢
		 */
		static final int SIGNAL = -1;
		
		/**
		 * waitStatus value to indicate thread is waiting on condition
		 * 在使用Condition时, 如 Condition isEmpty = lock.newCondition()
		 * 线程在这个condition上等待时, 也是将当前线程封装进Node对象, 同时设置其waitStatus是CONDITION
		 */
		static final int CONDITION = -2;
		/**
		 * 共享锁的时候用到
		 * <p>
		 * waitStatus value Node.PROPAGATE can only set for head node.
		 * <p>
		 * Just indicate that the succeed node of head node will propagate unpark succeed node behavior.
		 * <p>
		 * Because the succeed node maybe takes share lock successfully when current node take share lock successfully.
		 * <p>
		 * waitStatus value to indicate the next acquireShared should unconditionally propagate
		 */
		static final int PROPAGATE = -3;
		
		/**
		 * 标记当前节点的信号量状态, 就是上面这几个状态 <br/>
		 * The values are arranged numerically to simplify use. Non-negative values mean that a node doesn't need to signal.
		 * 1, 0 两种状态表示node不需要被signal通知?
		 * <ul>
		 *     <li/>CANCELLED 1  表示因为超时或者在这个线程上调用了interrupt()导致这个线程cancelled <br/>
		 *                       This node is cancelled due to timeout or interrupt. Nodes never leave this state.
		 *                       In particular, a thread with cancelled node never again blocks.
		 *                       一旦节点的状态变为CANCELLED, 它将不会被转换到其他任何状态。
		 *                       该节点不会被用于任何后续的同步或调度操作。在实际操作中, 这意味着当AQS的操作（如释放操作）遍历等待队列时, 会忽略任何处于CANCELLED状态的节点。
		 *                       在同步队列的操作过程中, 会定期进行队列的清理工作, 移除那些处于CANCELLED状态的节点, 因为它们不再对同步状态有任何影响。
		 *                       这个清理过程通常发生在其他操作如enq()或dequeue()等方法执行时。
		 *
		 *                       设置节点为CANCELLED状态是AQS中处理线程取消（如线程在获取锁时被中断）的一种方式。
		 *                       这样的设计减少了复杂性，并且提高了系统的整体效率，因为取消的节点不再需要系统资源去监视或管理。
		 *     <li/>0            None of the above 后面可能有节点但是没有挂起, 所以当我释放锁的时候就可以不去唤醒后面的线程
		 *
		 *      				 初始状态：当一个新的节点被创建并加入到AQS的同步队列中时, 它的waitStatus通常被初始化为0。
		 *      				 这意味着节点尚未被赋予具体的等待状态指示, 如SIGNAL, CONDITION, 或PROPAGATE。
		 *
		 *      				 无特定信号状态：waitStatus为0也表示节点目前不在任何特定的等待状态中。
		 *      				 例如, 它不需要唤醒其后继者 (不是SIGNAL状态), 也没有被取消(不是CANCELLED状态), 并且它不是处于条件等待中 (不是CONDITION状态)。
		 *
		 *      				 转变可能：处于0状态的节点是可能在将来被更新为其他状态的。例如, 如果持有锁的线程决定释放锁, 且检测到后继节点的状态为0,
		 *      				 它可能会更新这个后继节点的状态为SIGNAL, 以确保当锁被释放时, 后继节点可以被唤醒。
		 *
		 *      				 活动节点：在某些情况下, waitStatus为0的节点可能正处于活动状态, 正在参与对同步状态的竞争, 但尚未有必要将其标记为需要唤醒后继节点或其他特殊操作。
		 *     <li/>SIGNAL -1    在AQS框架中, SIGNAL状态主要与节点的后继节点有关。
		 *     					 当一个节点的waitStatus被设置为SIGNAL时, 这表明该节点 (我们可以称之为前驱节点)在释放资源或变得不再活跃时, 有责任去唤醒它的后继节点。
		 *     					 这个状态实际上是针对前驱节点而设, 其目的是确保前驱节点在释放锁或资源之后, 会检查并可能唤醒它的后继节点。
		 *     					 操作流程的具体说明
		 *     					 1. 节点设置SIGNAL状态
		 *     					 当一个线程 (A线程) 将自己封装成节点并加入同步队列时, 它会查看其前驱节点的状态。
		 *     					 如果前驱节点的状态不是SIGNAL, A线程可能会尝试将前驱节点的状态设置为SIGNAL。
		 *     					 这样做是为了确保当前驱节点 (即A线程的前驱)释放资源时, 可以唤醒A线程代表的节点。
		 *     					 2. 节点在释放资源时的行为：
		 *     					 当一个节点 (假设为B线程)准备释放它所持有的资源时, 它会检查其后继节点的状态。
		 *     					 如果后继节点的状态是SIGNAL, 这表明后继节点需要被唤醒（因为后继节点已经将当前节点的状态预先设置为了SIGNAL)。
		 *     					 因此, B线程在释放资源之前, 会通过LockSupport.unpark()方法唤醒后继节点的线程。
		 *     <li/>CONDITION -2 表示这个节点在条件队列里面?
		 *                       This node is currently on a condition queue.
		 *                       It will not be used as a sync queue node until transferred, at which time the status will be set to 0.
		 *     <li/>PROPAGATE -3 
		 *                       A releaseShared should be propagated to other nodes. <br/>
		 *                       This is set (for head node only) in doReleaseShared to ensure propagation continues,
		 *                       even if other operations have since intervened.
		 *                       PROPAGATE状态是专门设计用于共享模式的同步器, 例如Semaphore或ReadWriteLock中的读锁。
		 *                       在共享模式中, 多个线程可能同时获得对资源的访问。PROPAGATE状态帮助确保当资源变得可用时, 信号可以传播给多个等待的线程, 而不仅仅是一个。
		 *
		 *                       当一个线程释放资源并且可能允许多个后继线程同时继续执行时, AQS将检查并可能更新后继节点的状态为PROPAGATE, 以确保释放信号可以适当地传递给更多的线程。这通常发生在如下情况:
		 *                       1. 线程释放共享资源: 线程在释放共享资源时 (例如释放一个读锁), 会尝试设置后继节点的状态为PROPAGATE, 以确保后续等待的线程也可以被唤醒并运行。
		 *                       2. 递归传播: 如果后继节点已处于或被设置为PROPAGATE状态, 当前节点在唤醒它的后继节点后, 这个状态可以帮助确保信号继续向下传递, 从而使得连续的多个节点(线程)能够被适当地唤醒。
		 *
		 *                       在ReadWriteLock.ReadLock的实现中, 当一个读锁被释放, 如果有其他线程仍然可以获得读锁, 那么AQS将使用PROPAGATE状态来确保所有合适的等待线程都可以被唤醒, 从而最大化并发读的效率。
		 *
		 *                       PROPAGATE状态在AQS的设计中主要针对共享模式的同步器, 它确保在资源可以被多个线程共享访问的情况下, 状态的变更和线程的唤醒可以适当地传播。
		 *                       这种机制有助于提升并发性能, 并确保资源使用的公平性和效率。在处理共享资源的同步问题时, PROPAGATE状态发挥着关键的作用, 使得系统能够更加高效和响应地管理多线程访问共享资源的场景。
		 * </ul>
		 */
		volatile int waitStatus;
		
		/**
		 * 前驱节点，当前节点加入到同步队列中被设置
		 */
		volatile Node prev;
		
		/**
		 * 后继节点
		 */
		volatile Node next;
		
		/**
		 * 节点包含的线程
		 * 正常有效的node都会维护一个线程
		 */
		volatile Thread thread;
		
		/**
		 * 这里的nextWaiter实际上是用来存储节点模式的字段。它既表示节点在队列中的下一个节点, 也通过特定值（如SHARED或EXCLUSIVE）指明节点的等待类型。
		 * 在Condition上等待时, node加入的是等待队列, 这是一个单向队列, 由nextWaiter指向下一个节点
		 * Link to next node waiting on condition, or the special value SHARED. <br/>
		 * 在new Node时创传进来
		 * <ul>Node对象定义了两个静态属性:
		 *     <li/>static final Node SHARED = new Node();
		 *     <li/>static final Node EXCLUSIVE = null;
		 * </ul>
		 * 分别传入这两个Node来表示共享还是独占模式
		 */
		Node nextWaiter;
		
		/**
		 * Returns true if node is waiting in shared mode. <br/>
		 * 只要判断nextWaiter == SHARED既可以 <br/>
		 * nextWaiter在new Node时初始化, 是构造函数的第二个参数, 传入的值是下面的两个之一
		 * <ul>Node对象定义了两个静态属性:
		 *     <li/>static final Node SHARED = new Node();
		 *     <li/>static final Node EXCLUSIVE = null;
		 * </ul>
		 * 分别闯入这两个Node来表示共享还是独占模式
		 */
		final boolean isShared() {
			return nextWaiter == SHARED;
		}
		
		/**
		 * 返回前驱节点, 如果前驱节点是null则抛 NullPointerException
		 */
		final Node predecessor() throws NullPointerException {
			Node p = prev;
			if (p == null) {
				throw new NullPointerException();
			} else {
				return p;
			}
		}
		
		Node() {    // Used to establish initial head or SHARED marker
		}
		
		/**
		 * Link to next node waiting on condition, or the special value SHARED.
		 * 指向下一个在condition上wait()的节点
		 *
		 * @param thread
		 * @param mode   指向下一个在condition上wait()的节点 <br/>
		 *               Link to next node waiting on condition, or the special value SHARED.
		 */
		Node(Thread thread, Node mode) {     // Used by addWaiter
			this.nextWaiter = mode; // 设置节点的模式（独占或共享）
			this.thread = thread;   // 关联线程与此节点
		}
		
		Node(Thread thread, int waitStatus) { // Used by Condition
			this.waitStatus = waitStatus;
			this.thread = thread;
		}
	}
	
	/**
	 * 指向同步等待队列的头节点 <br/>
	 * Head of the wait queue, lazily initialized.  Except for initialization, it is modified only via method setHead.
	 * Note: If head exists, its waitStatus is guaranteed not to be CANCELLED.
	 */
	private transient volatile Node head;
	
	/**
	 * 指向同步等待队列的尾节点 <br/>
	 * Tail of the wait queue, lazily initialized.  Modified only via
	 * method enq to add new wait node.
	 */
	private transient volatile Node tail;
	
	/**
	 * state字段用于表示同步器的状态, 这个状态的具体含义根据同步器的实现而异。
	 * 例如, 在一个互斥锁的实现中, state可能表示锁的持有情况 (0表示未被持有, 1表示被某个线程持有);
	 * 在一个信号量中, state可能表示当前可用的许可数量。
	 * ReentrantLock和ReentrantReadWriteLock都实现了AQS，所以：
	 * 记录锁被加了多少次(锁的上锁次数) <br/>
	 * 0 未上锁 <br/>
	 * lock.lock() 成功 state == 1;  <br/>
	 * 再次lock.lock()成功 state == 2 <br/>
	 */
	private volatile int state;
	
	/**
	 * Returns the current value of synchronization state.
	 * This operation has memory semantics of a {@code volatile} read.
	 * <p>
	 * 同步资源状态
	 * <p>
	 * 记录锁被加了多少次(锁的上锁次数) <br/>
	 * 0 未上锁 <br/>
	 * lock.lock() 成功 state == 1;  <br/>
	 * 再次lock.lock()成功 state == 2 <br/>
	 *
	 * @return current state value
	 */
	protected final int getState() {
		return state;
	}
	
	/**
	 * 设置同步状态  <br/>
	 * Sets the value of synchronization state.
	 * This operation has memory semantics of a {@code volatile} write.
	 *
	 * @param newState the new state value
	 */
	protected final void setState(int newState) {
		state = newState;
	}
	
	/**
	 * CAS修改state <br/>
	 * Atomically sets synchronization state to the given updated
	 * value if the current state value equals the expected value.
	 * This operation has memory semantics of a {@code volatile} read
	 * and write.
	 *
	 * @param expect the expected value
	 * @param update the new value
	 * @return {@code true} if successful. False return indicates that the actual
	 * value was not equal to the expected value.
	 */
	protected final boolean compareAndSetState(int expect, int update) {
		// See below for intrinsics setup to support this
		return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
	}
	
	// Queuing utilities
	
	/**
	 * The number of nanoseconds for which it is faster to spin
	 * rather than to use timed park. A rough estimate suffices
	 * to improve responsiveness with very short timeouts.
	 * <p>
	 * 自旋多少纳秒, 自旋这么一段时间认为比 park 一个线程来划算
	 * 线程在等待获取资源时保持活跃状态, 不停地检查是否可以执行下一步操作。这种方式消耗CPU资源, 但响应时间快, 适用于预期等待时间非常短的场景。
	 */
	static final long spinForTimeoutThreshold = 1000L;
	
	/**
	 * 如果CLH队列还未初始化, 那么初始化队列并入队, 否则直接入队, 返回前驱节点. 
	 * 入队逻辑:
	 * <ol>
	 *     <li/>如果tail节点为null, 表示队列还没有初始化, 创建一个空节点, head和tail都指向它
	 *     <li/>将node.prev指向tail
	 *     <li/>CAS将tail指向node
	 *     <LI/>原来的尾结点.next指向node
	 * </ol>
	 * Inserts node into queue, initializing if necessary. See picture above.
	 */
	private Node enq(final Node node) {
		/*
		 * 注意这里是个循环, 所以如果队列还没有初始化, 下面if/else两个分支都会走一遍
		 * 第一次循环先初始化队列, head = tail = new Node()
		 * 第二次循环将node入队, 并把tail指向node
		 */
		for (; ; ) {
			Node t = tail;
			//尾结点为空, 表示CLH队列还没有初始化
			if (t == null) { // Must initialize
				//队列为空, 需要初始化，构建一个伪节点作为head和tail
				if (compareAndSetHead(new Node())) {
					tail = head;
				}
			} else {
				//修改node.prev指向, 不是多线程竞争点, 所以直接设置
				node.prev = t;
				/*
				 * set尾节在多线程环境下是竞争点, 因为多个线程都想将tail指向代表自己的节点, 
				 * 所以这里要用CAS来移动tail指针, 当tail指向当前节点
				 */
				if (compareAndSetTail(t, node)) {
					/*
					 * t指向的是原来的tail节点, 进到这里, tail已经指向了当前节点node
					 * 在前一步已经将当前节点的prev指向t
					 * 这里再将t.next指向当前节点即可
					 * 修改t.next指向也是多线程竞争的点, 但是这里实在CAS成功后才会执行, 所以可以直接赋值
					 */
					t.next = node;
					return t;
				}
			}
		}
	}
	
	/**
	 * 将当前线程封装进Node对象然后入队, 返回当前节点
	 * Creates and enqueues node for current thread and given mode.
	 *
	 * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
	 * @return the new node
	 */
	private Node addWaiter(Node mode) {
		// 1. 将当前线程构建成Node类型
		Node node = new Node(Thread.currentThread(), mode);
		/*
		 * 2: Try the fast path of enq; backup to full enq on failure
		 * 拿到尾结点
		 */
		Node pred = tail;
		/*
		 * 3: 尾节点不为null 表示 CLH 队列已经初始化了
		 * 将当前节点的prev指向原来的尾结点
		 * CAS 将node设为尾结点
		 * 原来尾结点的next指向当前节点
		 * 
		 * 这个if逻辑在下面enq(node)里面也包含了, 但是大部分情况是队列已经初始化了, 
		 * 我们只需要入队, 所以把入队这部分逻辑单独提取出来放到这里
		 * 
		 * 在遇到tail == null(队列还未初始化时, 这种情况不常见), 才走enq(node)初始化队列在入队
		 */
		if (pred != null) {
			// 新来一个线程排队, 将这个新来的Node的prev指向尾结点
			node.prev = pred;
			/*
			 * 1: CAS设置当前节点为尾节点
			 * 2: 原来尾结点的next指向当前节点
			 * 
			 * set尾节点在多线程环境下是竞争点, 因为多个线程都想将tail指向代表自己的节点,
			 * 所以这里要用CAS来移动tail指针, 当tail指向当前节点
			 */
			if (compareAndSetTail(pred, node)) {
				/*
				 * pred指向的是原来的tail节点, 进到这里, tail已经指向了当前节点node
				 * 在前一步已经将当前节点的prev指向pred(if上面的语句)
				 * 这里再将pred.next指向当前节点即可
				 * 修改pred.next指向也是多线程竞争的点, 但是这里是在CAS成功后才会执行, 所以可以直接赋值
				 */
				pred.next = node;
				return node;
			}
		}
		//CLH 队列还未初始化时走这里 
		enq(node);
		return node;
	}
	
	/**
	 * Sets head of queue to be node, thus dequeuing. Called only by
	 * acquire methods.  Also nulls out unused fields for sake of GC
	 * and to suppress unnecessary signals and traversals.
	 * <p>
	 * 设置头节点
	 * <p>
	 * 头节点是一个空节点, 如果一个节点排队, 然后它获取锁成功了, 此时会把该节点设为头节点
	 *
	 * @param node the node
	 */
	private void setHead(Node node) {
		head = node;
		//头节点不需要线程信息
		node.thread = null;
		node.prev = null;
	}
	
	/**
	 * 唤醒后面排队的节点
	 */
	private void unparkSuccessor(Node node) {
		//获取头结点wait状态(注意这里传入的node是头结点)
		int ws = node.waitStatus;
		/*
		 * CANCELLED 1     表示因为超时或者在这个线程上调用了interrupt()导致这个线程cancelled <br/>
		 *                       This node is cancelled due to timeout or interrupt. Nodes never leave this state.
		 *                       In particular, a thread with cancelled node never again blocks.
		 *           0     None of the above
		 * SIGNAL    -1    表示当前节点释放锁或者cancel时, 应该unpark后继节点(此时后继节点已经block住了) <br/>
		 *                       The successor of this node is blocked (via park), so the current node must unpark its successor when it releases or cancels.
		 * CONDITION -2    表示这个节点在条件队列里面?
		 *                       This node is currently on a condition queue.
		 *                       It will not be used as a sync queue node until transferred, at which time the status will be set to 0.
		 * PROPAGATE -3
		 *                       A releaseShared should be propagated to other nodes. <br/>
		 *                       This is set (for head node only) in doReleaseShared to ensure propagation continues,
		 *                       even if other operations have since intervened.
		 */
		if (ws < 0) {
			//若为-1先基于CAS将自己的状态改为0
			compareAndSetWaitStatus(node, ws, 0);// 将等待状态waitStatus设置为初始值0
		}
		
		/*
		 * 拿到头结点的后继节点
		 * 若后继结点为空, 或状态为CANCELLED, 则从尾部往前遍历找到最前的一个处于正常阻塞状态的结点进行唤醒
		 */
		Node s = node.next;
		/*
		 * 如果后继节点的状态为1(表示节点取消了), 判断s==null大概率是为了避免后面的空指针错误
		 */
		if (s == null || s.waitStatus > 0) { // > 0 只有一种状态, CANCELLED
			s = null;
			/*
			 * 从尾结点往前找到排在最前面的一个不是CANCELLED状态的节点, 然后唤醒它
			 */
			for (Node t = tail; t != null && t != node; t = t.prev) {
				//找到离head最近的有效节点并赋值给s
				if (t.waitStatus <= 0) {
					s = t;
				}
			}
		}
		//找到了需要被唤醒的节点, 执行unpark
		if (s != null) {
			LockSupport.unpark(s.thread);//唤醒线程
		}
	}
	
	/**
	 * 把当前结点设置为SIGNAL或者PROPAGATE
	 * 唤醒head.next(B节点)，B节点唤醒后可以竞争锁，成功后head->B，然后又会唤醒B.next，一直重复直到共享节点都唤醒
	 * head节点状态为SIGNAL，重置head.waitStatus->0，唤醒head节点线程，唤醒后线程去竞争共享锁
	 * head节点状态为0，将head.waitStatus->Node.PROPAGATE传播状态，表示需要将状态向后继节点传播
	 */
	private void doReleaseShared() {
		for (; ; ) {
			Node h = head;
			if (h != null && h != tail) {
				int ws = h.waitStatus;
				if (ws == Node.SIGNAL) {//head是SIGNAL状态
					/* head状态是SIGNAL，重置head节点waitStatus为0，这里不直接设为Node.PROPAGATE,
					 * 是因为unparkSuccessor(h)中，如果ws < 0会设置为0，所以ws先设置为0，再设置为PROPAGATE
					 * 这里需要控制并发，因为入口有setHeadAndPropagate跟release两个，避免两次unpark
					 */
					if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0)) {
						continue; //设置失败，重新循环
					}
					/* head状态为SIGNAL，且成功设置为0之后,唤醒head.next节点线程
					 * 此时head、head.next的线程都唤醒了，head.next会去竞争锁，成功后head会指向获取锁的节点，
					 * 也就是head发生了变化。看最底下一行代码可知，head发生变化后会重新循环，继续唤醒head的下一个节点
					 */
					unparkSuccessor(h);
					/*
					 * 如果本身头节点的waitStatus是处于重置状态(waitStatus==0)的，将其设置为"传播"状态。
					 * 意味着需要将状态向后一个节点传播
					 */
				} else if (ws == 0 &&
						!compareAndSetWaitStatus(h, 0, Node.PROPAGATE)) {
					continue;                // loop on failed CAS
				}
			}
			if (h == head) //如果head变了，重新循环
			{
				break;
			}
		}
	}
	
	/**
	 * 把node节点设置成head节点，且Node.waitStatus->Node.PROPAGATE
	 */
	private void setHeadAndPropagate(Node node, int propagate) {
		Node h = head; //h用来保存旧的head节点
		setHead(node);//head引用指向node节点
		/* 这里意思有两种情况是需要执行唤醒操作
		 * 1.propagate > 0 表示调用方指明了后继节点需要被唤醒
		 * 2.头节点后面的节点需要被唤醒（waitStatus<0），不论是老的头结点还是新的头结点
		 */
		if (propagate > 0 || h == null || h.waitStatus < 0 ||
				(h = head) == null || h.waitStatus < 0) {
			Node s = node.next;
			if (s == null || s.isShared())//node是最后一个节点或者 node的后继节点是共享节点
				/* 如果head节点状态为SIGNAL，唤醒head节点线程，重置head.waitStatus->0
				 * head节点状态为0(第一次添加时是0)，设置head.waitStatus->Node.PROPAGATE表示状态需要向后继节点传播
				 */ {
				doReleaseShared();
			}
		}
	}
	
	// Utilities for various versions of acquire
	
	/**
	 * 取消在AQS中排队的Node <p/>
	 * 取消节点整体操作流程<ol>
	 * <li/>thread=null
	 * <li/>往前找到有效节点(waitStatus!=1)作为当前节点的prev
	 * <li/>waitStatus=1, 代表取消
	 * <li>脱离整个AQS队列
	 * <pre> 
	 * 4.1 当前节点是tail节点
	 * 4.2 当前节点是head的后继节点
	 * 4.3 不是head的后继也不是tail节点
	 * </pre>
	 * </li>
	 * <li/>sss
	 * <li/>
	 * <li/>
	 * <ol/>
	 * 取消一个节点的话, 要把这个节点的waitStatus设为1, thread设为null, tail指针要指向我前面的节点
	 * Cancels an ongoing attempt to acquire.
	 *
	 * @param node the node
	 */
	private void cancelAcquire(Node node) {
		// Ignore if node doesn't exist
		//当前节点为null直接忽略
		if (node == null) {
			return;
		}
		
		//第一步线程置为null
		node.thread = null;
		
		/*
		 * 往前跳过被取消的节点, 找到一个有效的节点
		 */
		// Skip cancelled predecessors
		Node pred = node.prev;
		while (pred.waitStatus > 0) {
			node.prev = pred = pred.prev;
		}
		
		// predNext is the apparent node to unsplice. CASes below will
		// fail if not, in which case, we lost race vs another cancel
		// or signal, so no further action is necessary.
		Node predNext = pred.next;
		
		// Can use unconditional write instead of CAS here.
		// After this atomic step, other Nodes can skip past us.
		// Before, we are free of interference from other threads.
		// 当前节点状态设为1, 代表节点取消
		node.waitStatus = Node.CANCELLED;
		
		/*
		 * 脱离AQS队列的操作
		 * 如果当前节点是尾节点, CAS将尾结点设为当前节点的上一个节点
		 */
		// If we are the tail, remove ourselves.
		if (node == tail && compareAndSetTail(node, pred)) {
			//pred现在是新的尾节点, CAS将其next设为null
			compareAndSetNext(pred, predNext, null);
		} else {
			/*
			 * 到这里表示当前要cancel的节点不是尾节点或者说CAS替换尾结点失败
			 */
			// If successor needs signal, try to set pred's next-link
			// so it will get one. Otherwise wake it up to propagate.
			int ws;
			if (pred != head &&
					//拿到上一个节点的状态, 并且判断是否为-1, 如果不是-1并且不是取消状态(大于0的只有1, 即CANCEL), 就改为-1
					((ws = pred.waitStatus) == Node.SIGNAL ||
							(ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
					pred.thread != null) {
				/*
				 * 上面的判断都是为了避免后面节点无法被唤醒
				 * 前驱节点是有效节点, 可以唤醒后面的节点
				 */
				Node next = node.next;
				//我的下一个节点是有效节点
				if (next != null && next.waitStatus <= 0) {
					//将上一个节点的next指向我的下一个节点
					compareAndSetNext(pred, predNext, next);
				}
			} else {
				//当前节点是head的后继节点, 直接唤醒后继节点
				unparkSuccessor(node);
			}
			
			node.next = node; // help GC
		}
	}
	
	/**
	 * 当前Node没有拿到锁资源, 或者没有资格竞争锁资源, 看一下能否挂起当前线程<p/>
	 * Checks and updates status for a node that failed to acquire.
	 * Returns true if thread should block. This is the main signal
	 * control in all acquire loops.  Requires that pred == node.prev.
	 * <p>
	 * 检查pred这个前驱节点的waitStatus
	 * <ul>
	 *     <li/>如果是SIGNAL -1, 那么当前节点可以安全的park, 返回true, 剩下的都返回false
	 *     <li/>如果是CANCELLED 1, 那么把当前节点的prev指向前驱节点(pred)的前驱节点, 
	 *          然后把新的前驱节点的next指向当前节点, 这样原来那个处于CANCELLED状态的节点就从CLH队列中移除了
	 *          循环该操作, 直到找到一个waitStatus不是CANCELLED状态的前驱
	 *     <li/>如果是当 0 CONDITION PROPAGATE, 那么前驱节点的状态设置为SIGNAL -1
	 * </ul>
	 *
	 * @param pred node's predecessor holding status
	 * @param node the node
	 * @return {@code true} if thread should block
	 */
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
		int ws = pred.waitStatus;
		/*
		 * 前驱节点知道我要被挂起
		 *
		 * CANCELLED 1     表示因为超时或者在这个线程上调用了interrupt()导致这个线程cancelled <br/>
		 *                       This node is cancelled due to timeout or interrupt. Nodes never leave this state.
		 *                       In particular, a thread with cancelled node never again blocks.
		 *           0     None of the above
		 * SIGNAL    -1    表示当前节点释放锁或者cancel时, 应该unpark后继节点(此时后继节点已经block住了) <br/>
		 *                       The successor of this node is blocked (via park), so the current node must unpark its successor when it releases or cancels.
		 * CONDITION -2    表示这个节点在条件队列里面?
		 *                       This node is currently on a condition queue.
		 *                       It will not be used as a sync queue node until transferred, at which time the status will be set to 0.
		 * PROPAGATE -3
		 *                       A releaseShared should be propagated to other nodes. <br/>
		 *                       This is set (for head node only) in doReleaseShared to ensure propagation continues,
		 *                       even if other operations have since intervened.
		 */
		if (ws == Node.SIGNAL) {
			// 若前驱结点的状态是SIGNAL，意味着当前结点可以被安全地park
			return true;
		}
		if (ws > 0) {
			/*
			 * waitStatus > 0 只有一种状态, 即 CANCELLED 1 表示线程等待超时或者被中断了
			 * 前驱节点状态如果是取消状态，将被移除出队列, 当前节点就需要往前找到一个状态不为1的Node作为他的prev
			 */
			do {
				node.prev = pred = pred.prev;
			} while (pred.waitStatus > 0);
			pred.next = node;
		} else {
			/*
			 * 当前驱节点waitStatus为 0 CONDITION PROPAGATE状态时
			 * 将其设置为SIGNAL状态, 然后当前结点才可以可以被安全地park
			 */
			compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
		}
		return false;
	}
	
	/**
	 * 只是打上一个中断标记, 二不是直接中断这个线程
	 * 中断这个线程 Interrupts this thread. 线程被中断后可以自行决定退出还是继续运行
	 * <p>
	 * 如果这个线程因为调用以下方法的原因进入了阻塞(blocked)状态:
	 * <ul>
	 *     <li/>wait()  wait(long)  wait(long, int)
	 *     <li/>join()  join(long)  join(long, int)
	 *     <li/>sleep(long)  sleep(long, int)
	 * </ul>
	 * <p>
	 *    然后Thread.currentThread().interrupt()被调用, 那么这个线程会立即被唤醒并且收到一个InterruptedException, 同时中断状态被清除
	 * <p>
	 *    如果线程没有进入上述这些可被中断的状态, 那么调用线程的interrupt()方法只是设置一下线程的中断状态位, 然后线程在进入上述可中断状态时, 立刻会抛出InterruptedException
	 * <p>
	 * 如果这个线程是在等待IO的时候被Blocked, 接下来这个线程被interrupt(), 那么如果IO操作的Channel是InterruptibleChannel, 这个channel就会被关闭; 设置该线程的interrupted状态为true; 同时线程收到ClosedByInterruptException
	 * <p>
	 * 如果这个线程是在java.nio.channels.Selector上Blocked, 那么设置该线程的interrupted状态为true; 同时从select()调用中立刻返回
	 * <p>
	 * Convenience method to interrupt current thread.
	 */
	static void selfInterrupt() {
		Thread.currentThread().interrupt();
	}
	
	/**
	 * 阻塞当前节点，返回当前Thread的中断状态
	 * LockSupport.park 底层实现逻辑调用系统内核功能 pthread_mutex_lock 阻塞线程
	 */
	private final boolean parkAndCheckInterrupt() {
		LockSupport.park(this);//阻塞
		//这个方法可以判断, 当前挂起的线程, 是被中断唤醒的还是被正常唤醒的
		return Thread.interrupted();
	}
	
	/**
	 * Various flavors of acquire, varying in exclusive/shared and
	 * control modes.  Each is mostly the same, but annoyingly
	 * different.  Only a little bit of factoring is possible due to
	 * interactions of exception mechanics (including ensuring that we
	 * cancel if tryAcquire throws exception) and other control, at
	 * least not without hurting performance too much.
	 * 
	 * 查看当前节点是否是第一个排队的节点, 如果是可以再次尝试获取锁资源, 如果长时间拿不到就要挂起线程
	 * 如果不是第一个排队的节点, 那么就尝试挂起线程
	 * 如果当前节点的前驱节点是头节点, 那么再次tryAcquire(arg)尝试获取锁, 如果成功则返回false
	 * 
	 */
	final boolean acquireQueued(final Node node, int arg) {
		boolean failed = true;
		try {
			boolean interrupted = false;
			for (; ; ) {
				final Node p = node.predecessor();//找到当前结点的前驱结点
				/*
				 * 如果当前节点的前驱节点是头结点, 那么表示当前节点是第一个排队的节点
				 * 然后尝试获取锁(因为之前获取锁的线程可能已经释放了锁)
				 */
				if (p == head && tryAcquire(arg)) {
					//获取锁资源成功了
					setHead(node);
					/*
					 * 上面一步已经把 node.prev 设为null了
					 * 这里再把原来的头结点的next指针设为null,
					 * 所以旧的头节点既没有节点指向它, 它也没有指针去指向其他节点
					 * 所以旧的头结点可以被垃圾回收了
					 */
					p.next = null; // help GC
					failed = false; //获取锁资源成功了
					return interrupted;
				}
			
				
				if (shouldParkAfterFailedAcquire(p, node) &&
						parkAndCheckInterrupt()) {
					interrupted = true;
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}
	
	/**
	 * 与acquireQueued逻辑相似
	 */
	private void doAcquireInterruptibly(int arg)
			throws InterruptedException {
		//放到AQS队列末尾
		final Node node = addWaiter(Node.EXCLUSIVE);
		boolean failed = true;
		try {
			for (; ; ) {
				final Node p = node.predecessor(); //找到当前结点的前驱结点
				/*
				 * 如果当前节点的前驱节点是头结点, 那么表示只有当前节点在排队
				 * 然后尝试获取锁(因为之前获取锁的线程可能已经释放了锁)
				 */
				if (p == head && tryAcquire(arg)) {
					setHead(node);
					p.next = null; // help GC
					failed = false;
					return;
				}
				if (shouldParkAfterFailedAcquire(p, node) &&
						parkAndCheckInterrupt()) {
					//如果这个线程已经被中断了, 这边要抛出InterruptedException
					throw new InterruptedException();
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}
	
	/**
	 * 独占模式定时获取
	 */
	private boolean doAcquireNanos(int arg, long nanosTimeout)
			throws InterruptedException {
		if (nanosTimeout <= 0L) {
			return false;
		}
		//设置结束时间
		final long deadline = System.nanoTime() + nanosTimeout;
		final Node node = addWaiter(Node.EXCLUSIVE);//加入队列
		boolean failed = true;
		try {
			for (; ; ) {
				final Node p = node.predecessor();
				//如果前驱节点是头结点, 立马抢锁, 意思就是当前节点是第一个排队的节点
				if (p == head && tryAcquire(arg)) {
					//如果抢锁成功, 将当前节点设为头结点, 将原来的头结点从队列中移除
					setHead(node);
					p.next = null; // help GC
					failed = false;
					return true;
				}
				
				//计算剩余的可用时间
				nanosTimeout = deadline - System.nanoTime();
				//判断时间是否用尽了
				if (nanosTimeout <= 0L) {
					return false;//超时直接返回获取失败
				}
				//根据上一个节点状态来判断我是否可以挂起线程
				if (shouldParkAfterFailedAcquire(p, node) &&
						//避免剩余时间太少, 如果剩余时间太少就不用挂起了
						nanosTimeout > spinForTimeoutThreshold)
				//阻塞指定时长，超时则线程自动被唤醒
				{
					//如果剩余时间足够, 将线程挂起剩余时间
					LockSupport.parkNanos(this, nanosTimeout);
				}
				//如果线程醒了, 查看是中断唤醒的, 还是时间到了唤醒的
				if (Thread.interrupted())//当前线程中断状态
				{
					//是中断唤醒的, 直接抛异常
					throw new InterruptedException();
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}
	
	/**
	 * 尝试获取共享锁
	 */
	private void doAcquireShared(int arg) {
		final Node node = addWaiter(Node.SHARED);//入队
		boolean failed = true;
		try {
			boolean interrupted = false;
			for (; ; ) {
				final Node p = node.predecessor();//前驱节点
				if (p == head) {
					int r = tryAcquireShared(arg); //非公平锁实现，再尝试获取锁
					//state==0时tryAcquireShared会返回>=0(CountDownLatch中返回的是1)。
					// state为0说明共享次数已经到了，可以获取锁了
					if (r >= 0) {//r>0表示state==0,前继节点已经释放锁，锁的状态为可被获取
						//这一步设置node为head节点设置node.waitStatus->Node.PROPAGATE，然后唤醒node.thread
						setHeadAndPropagate(node, r);
						p.next = null; // help GC
						if (interrupted) {
							selfInterrupt();
						}
						failed = false;
						return;
					}
				}
				//前继节点非head节点，将前继节点状态设置为SIGNAL，通过park挂起node节点的线程
				if (shouldParkAfterFailedAcquire(p, node) &&
						parkAndCheckInterrupt()) {
					interrupted = true;
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}
	
	/**
	 * Acquires in shared interruptible mode.
	 *
	 * @param arg the acquire argument
	 */
	private void doAcquireSharedInterruptibly(int arg)
			throws InterruptedException {
		final Node node = addWaiter(Node.SHARED);
		boolean failed = true;
		try {
			for (; ; ) {
				final Node p = node.predecessor();
				if (p == head) {
					int r = tryAcquireShared(arg);
					if (r >= 0) {
						setHeadAndPropagate(node, r);
						p.next = null; // help GC
						failed = false;
						return;
					}
				}
				if (shouldParkAfterFailedAcquire(p, node) &&
						parkAndCheckInterrupt()) {
					throw new InterruptedException();
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}
	
	/**
	 * Acquires in shared timed mode.
	 *
	 * @param arg          the acquire argument
	 * @param nanosTimeout max wait time
	 * @return {@code true} if acquired
	 */
	private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
			throws InterruptedException {
		if (nanosTimeout <= 0L) {
			return false;
		}
		final long deadline = System.nanoTime() + nanosTimeout;
		final Node node = addWaiter(Node.SHARED);
		boolean failed = true;
		try {
			for (; ; ) {
				final Node p = node.predecessor();
				if (p == head) {
					int r = tryAcquireShared(arg);
					if (r >= 0) {
						setHeadAndPropagate(node, r);
						p.next = null; // help GC
						failed = false;
						return true;
					}
				}
				nanosTimeout = deadline - System.nanoTime();
				if (nanosTimeout <= 0L) {
					return false;
				}
				if (shouldParkAfterFailedAcquire(p, node) &&
						nanosTimeout > spinForTimeoutThreshold) {
					LockSupport.parkNanos(this, nanosTimeout);
				}
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			}
		} finally {
			if (failed) {
				cancelAcquire(node);
			}
		}
	}
	
	// Main exported methods
	
	/**
	 * 独占方式获取锁, arg为获取锁的次数 <br/>
	 * FairSync 和 NonfairSync 实现实现了这个方法 <br/>
	 * <p>
	 * NonfairSync实现逻辑: 尝试获取锁, 如果成功则将ExclusiveOwnerThread设为当前线程, 失败不会block
	 * <ul>
	 *     <li/>如果资源还没有被锁定, 尝试加锁并设置state=acquires
	 *     <li/>如果资源已经被锁定且是被当前线程锁定的, 那么state=state+acquires
	 * </ul>
	 * <p>
	 * Attempts to acquire in exclusive mode. This method should query
	 * if the state of the object permits it to be acquired in the
	 * exclusive mode, and if so to acquire it.
	 *
	 * <p>This method is always invoked by the thread performing
	 * acquire.  If this method reports failure, the acquire method
	 * may queue the thread, if it is not already queued, until it is
	 * signalled by a release from some other thread. This can be used
	 * to implement method {@link Lock#tryLock()}.
	 *
	 * <p>The default
	 * implementation throws {@link UnsupportedOperationException}.
	 *
	 * @param arg the acquire argument. This value is always the one
	 *            passed to an acquire method, or is the value saved on entry
	 *            to a condition wait.  The value is otherwise uninterpreted
	 *            and can represent anything you like.
	 * @return {@code true} if successful. Upon success, this object has
	 * been acquired.
	 * @throws IllegalMonitorStateException  if acquiring would place this
	 *                                       synchronizer in an illegal state. This exception must be
	 *                                       thrown in a consistent fashion for synchronization to work
	 *                                       correctly.
	 * @throws UnsupportedOperationException if exclusive mode is not supported
	 */
	protected boolean tryAcquire(int arg) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 独占方式, arg为释放锁的次数, 尝试释放资源
	 * Attempts to set the state to reflect a release in exclusive mode.
	 *
	 * <p>This method is always invoked by the thread performing release.
	 *
	 * <p>The default implementation throws
	 * {@link UnsupportedOperationException}.
	 *
	 * @param arg the release argument. This value is always the one
	 *            passed to a release method, or the current state value upon
	 *            entry to a condition wait.  The value is otherwise
	 *            uninterpreted and can represent anything you like.
	 * @return {@code true} if this object is now in a fully released
	 * state, so that any waiting threads may attempt to acquire;
	 * and {@code false} otherwise.
	 * @throws IllegalMonitorStateException  if releasing would place this
	 *                                       synchronizer in an illegal state. This exception must be
	 *                                       thrown in a consistent fashion for synchronization to work
	 *                                       correctly.
	 * @throws UnsupportedOperationException if exclusive mode is not supported
	 */
	protected boolean tryRelease(int arg) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 共享方式, arg为获取锁的次数
	 * <ol>
	 *     <li/>当返回值大于0时, 表示获取同步状态成功, 同时还有剩余同步状态可供其他线程获取
	 *     <li/>当返回值等于0时, 表示获取同步状态成功, 但没有可用同步状态了
	 *     <li/>当返回值小于0时, 表示获取同步状态失败
	 * </ol>
	 * Attempts to acquire in shared mode. This method should query if
	 * the state of the object permits it to be acquired in the shared
	 * mode, and if so to acquire it.
	 *
	 * <p>This method is always invoked by the thread performing
	 * acquire.  If this method reports failure, the acquire method
	 * may queue the thread, if it is not already queued, until it is
	 * signalled by a release from some other thread.
	 *
	 * <p>The default implementation throws {@link
	 * UnsupportedOperationException}.
	 *
	 * @param arg the acquire argument. This value is always the one
	 *            passed to an acquire method, or is the value saved on entry
	 *            to a condition wait.  The value is otherwise uninterpreted
	 *            and can represent anything you like.
	 * @return a negative value on failure; zero if acquisition in shared
	 * mode succeeded but no subsequent shared-mode acquire can
	 * succeed; and a positive value if acquisition in shared
	 * mode succeeded and subsequent shared-mode acquires might
	 * also succeed, in which case a subsequent waiting thread
	 * must check availability. (Support for three different
	 * return values enables this method to be used in contexts
	 * where acquires only sometimes act exclusively.)  Upon
	 * success, this object has been acquired.
	 * @throws IllegalMonitorStateException  if acquiring would place this
	 *                                       synchronizer in an illegal state. This exception must be
	 *                                       thrown in a consistent fashion for synchronization to work
	 *                                       correctly.
	 * @throws UnsupportedOperationException if shared mode is not supported
	 */
	protected int tryAcquireShared(int arg) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 共享方式。arg为释放锁的次数, 如果释放后允许唤醒后续等待结点返回True, 否则返回False。
	 * Attempts to set the state to reflect a release in shared mode.
	 *
	 * <p>This method is always invoked by the thread performing release.
	 *
	 * <p>The default implementation throws
	 * {@link UnsupportedOperationException}.
	 *
	 * @param arg the release argument. This value is always the one
	 *            passed to a release method, or the current state value upon
	 *            entry to a condition wait.  The value is otherwise
	 *            uninterpreted and can represent anything you like.
	 * @return {@code true} if this release of shared mode may permit a
	 * waiting acquire (shared or exclusive) to succeed; and
	 * {@code false} otherwise
	 * @throws IllegalMonitorStateException  if releasing would place this
	 *                                       synchronizer in an illegal state. This exception must be
	 *                                       thrown in a consistent fashion for synchronization to work
	 *                                       correctly.
	 * @throws UnsupportedOperationException if shared mode is not supported
	 */
	protected boolean tryReleaseShared(int arg) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 该线程是否正在独占资源 <br/>
	 * Returns {@code true} if synchronization is held exclusively with
	 * respect to the current (calling) thread.  This method is invoked
	 * upon each call to a non-waiting {@link ConditionObject} method.
	 * (Waiting methods instead invoke {@link #release}.)
	 *
	 * <p>The default implementation throws {@link
	 * UnsupportedOperationException}. This method is invoked
	 * internally only within {@link ConditionObject} methods, so need
	 * not be defined if conditions are not used.
	 *
	 * @return {@code true} if synchronization is held exclusively;
	 * {@code false} otherwise
	 * @throws UnsupportedOperationException if conditions are not supported
	 */
	protected boolean isHeldExclusively() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * acquire是公平锁和非公平锁共用的一套逻辑
	 * <ol>
	 *     <li/>先尝试获取锁
	 *     <li/>如果获取锁失败, 将自己加入CLH队列中等待
	 * </ol>
	 * Acquires in exclusive mode, ignoring interrupts.  Implemented
	 * by invoking at least once {@link #tryAcquire},
	 * returning on success.  Otherwise the thread is queued, possibly
	 * repeatedly blocking and unblocking, invoking {@link
	 * #tryAcquire} until success.  This method can be used
	 * to implement method {@link Lock#lock}.
	 *
	 * @param arg the acquire argument.  This value is conveyed to
	 *            {@link #tryAcquire} but is otherwise uninterpreted and
	 *            can represent anything you like.
	 */
	public final void acquire(int arg) {
		if (!tryAcquire(arg) &&
				//没有拿到锁资源
				acquireQueued(
						//将当前线程包装进Node, 然后入队, Node.EXCLUSIVE表示排它锁
						addWaiter(Node.EXCLUSIVE), arg)) {
			selfInterrupt();
		}
	}
	
	/**
	 * Acquires in exclusive mode, aborting if interrupted.
	 * Implemented by first checking interrupt status, then invoking
	 * at least once {@link #tryAcquire}, returning on
	 * success.  Otherwise the thread is queued, possibly repeatedly
	 * blocking and unblocking, invoking {@link #tryAcquire}
	 * until success or the thread is interrupted.  This method can be
	 * used to implement method {@link Lock#lockInterruptibly}.
	 *
	 * @param arg the acquire argument.  This value is conveyed to
	 *            {@link #tryAcquire} but is otherwise uninterpreted and
	 *            can represent anything you like.
	 * @throws InterruptedException if the current thread is interrupted
	 */
	public final void acquireInterruptibly(int arg)
			throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		if (!tryAcquire(arg)) {
			doAcquireInterruptibly(arg);
		}
	}
	
	/**
	 * Attempts to acquire in exclusive mode, aborting if interrupted,
	 * and failing if the given timeout elapses.  Implemented by first
	 * checking interrupt status, then invoking at least once {@link
	 * #tryAcquire}, returning on success.  Otherwise, the thread is
	 * queued, possibly repeatedly blocking and unblocking, invoking
	 * {@link #tryAcquire} until success or the thread is interrupted
	 * or the timeout elapses.  This method can be used to implement
	 * method {@link Lock#tryLock(long, TimeUnit)}.
	 *
	 * @param arg          the acquire argument.  This value is conveyed to
	 *                     {@link #tryAcquire} but is otherwise uninterpreted and
	 *                     can represent anything you like.
	 * @param nanosTimeout the maximum number of nanoseconds to wait
	 * @return {@code true} if acquired; {@code false} if timed out
	 * @throws InterruptedException if the current thread is interrupted
	 */
	public final boolean tryAcquireNanos(int arg, long nanosTimeout)
			throws InterruptedException {
		//线程的中断标记位, 是不是从false被改为了true, 如果是, 直接抛异常
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		return tryAcquire(arg) ||
				//如果拿锁失败, 在这等待指定时间
				doAcquireNanos(arg, nanosTimeout);
	}
	
	/**
	 * 释放独占模式持有的锁
	 */
	public final boolean release(int arg) {
		if (tryRelease(arg)) {//释放锁
			//如果锁成功释放掉, 走这个逻辑
			Node h = head;
			/*
			 * CANCELLED 1     表示因为超时或者在这个线程上调用了interrupt()导致这个线程cancelled <br/>
			 *                       This node is cancelled due to timeout or interrupt. Nodes never leave this state.
			 *                       In particular, a thread with cancelled node never again blocks.
			 *           0     None of the above
			 * SIGNAL    -1    表示当前节点释放锁或者cancel时, 应该unpark后继节点(此时后继节点已经block住了) <br/>
			 *                       The successor of this node is blocked (via park), so the current node must unpark its successor when it releases or cancels.
			 * CONDITION -2    表示这个节点在条件队列里面?
			 *                       This node is currently on a condition queue.
			 *                       It will not be used as a sync queue node until transferred, at which time the status will be set to 0.
			 * PROPAGATE -3
			 *                       A releaseShared should be propagated to other nodes. <br/>
			 *                       This is set (for head node only) in doReleaseShared to ensure propagation continues,
			 *                       even if other operations have since intervened.
			 */
			//头结点没有线程信息, 不会有cancel状态, 所以不等于0的话只能是-1
			if (h != null && h.waitStatus != 0) {
				unparkSuccessor(h);//唤醒后继结点
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 请求获取共享锁
	 */
	public final void acquireShared(int arg) {
		if (tryAcquireShared(arg) < 0)//返回值小于0，获取同步状态失败，排队去；获取同步状态成功，直接返回去干自己的事儿。
		{
			doAcquireShared(arg);
		}
	}
	
	/**
	 * Acquires in shared mode, aborting if interrupted.  Implemented
	 * by first checking interrupt status, then invoking at least once
	 * {@link #tryAcquireShared}, returning on success.  Otherwise the
	 * thread is queued, possibly repeatedly blocking and unblocking,
	 * invoking {@link #tryAcquireShared} until success or the thread
	 * is interrupted.
	 *
	 * @param arg the acquire argument.
	 *            This value is conveyed to {@link #tryAcquireShared} but is
	 *            otherwise uninterpreted and can represent anything
	 *            you like.
	 * @throws InterruptedException if the current thread is interrupted
	 */
	public final void acquireSharedInterruptibly(int arg)
			throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		if (tryAcquireShared(arg) < 0) {
			doAcquireSharedInterruptibly(arg);
		}
	}
	
	/**
	 * Attempts to acquire in shared mode, aborting if interrupted, and
	 * failing if the given timeout elapses.  Implemented by first
	 * checking interrupt status, then invoking at least once {@link
	 * #tryAcquireShared}, returning on success.  Otherwise, the
	 * thread is queued, possibly repeatedly blocking and unblocking,
	 * invoking {@link #tryAcquireShared} until success or the thread
	 * is interrupted or the timeout elapses.
	 *
	 * @param arg          the acquire argument.  This value is conveyed to
	 *                     {@link #tryAcquireShared} but is otherwise uninterpreted
	 *                     and can represent anything you like.
	 * @param nanosTimeout the maximum number of nanoseconds to wait
	 * @return {@code true} if acquired; {@code false} if timed out
	 * @throws InterruptedException if the current thread is interrupted
	 */
	public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
			throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		return tryAcquireShared(arg) >= 0 ||
				doAcquireSharedNanos(arg, nanosTimeout);
	}
	
	/**
	 * Releases in shared mode.  Implemented by unblocking one or more
	 * threads if {@link #tryReleaseShared} returns true.
	 *
	 * @param arg the release argument.  This value is conveyed to
	 *            {@link #tryReleaseShared} but is otherwise uninterpreted
	 *            and can represent anything you like.
	 * @return the value returned from {@link #tryReleaseShared}
	 */
	public final boolean releaseShared(int arg) {
		if (tryReleaseShared(arg)) {
			doReleaseShared();
			return true;
		}
		return false;
	}
	
	// Queue inspection methods
	
	/**
	 * Queries whether any threads are waiting to acquire. Note that
	 * because cancellations due to interrupts and timeouts may occur
	 * at any time, a {@code true} return does not guarantee that any
	 * other thread will ever acquire.
	 *
	 * <p>In this implementation, this operation returns in
	 * constant time.
	 *
	 * @return {@code true} if there may be other threads waiting to acquire
	 */
	public final boolean hasQueuedThreads() {
		return head != tail;
	}
	
	/**
	 * Queries whether any threads have ever contended to acquire this
	 * synchronizer; that is if an acquire method has ever blocked.
	 *
	 * <p>In this implementation, this operation returns in
	 * constant time.
	 *
	 * @return {@code true} if there has ever been contention
	 */
	public final boolean hasContended() {
		return head != null;
	}
	
	/**
	 * Returns the first (longest-waiting) thread in the queue, or
	 * {@code null} if no threads are currently queued.
	 *
	 * <p>In this implementation, this operation normally returns in
	 * constant time, but may iterate upon contention if other threads are
	 * concurrently modifying the queue.
	 *
	 * @return the first (longest-waiting) thread in the queue, or
	 * {@code null} if no threads are currently queued
	 */
	public final Thread getFirstQueuedThread() {
		// handle only fast path, else relay
		return (head == tail) ? null : fullGetFirstQueuedThread();
	}
	
	/**
	 * Version of getFirstQueuedThread called when fastpath fails
	 */
	private Thread fullGetFirstQueuedThread() {
		/*
		 * The first node is normally head.next. Try to get its
		 * thread field, ensuring consistent reads: If thread
		 * field is nulled out or s.prev is no longer head, then
		 * some other thread(s) concurrently performed setHead in
		 * between some of our reads. We try this twice before
		 * resorting to traversal.
		 */
		Node h, s;
		Thread st;
		if (((h = head) != null && (s = h.next) != null &&
				s.prev == head && (st = s.thread) != null) ||
				((h = head) != null && (s = h.next) != null &&
						s.prev == head && (st = s.thread) != null)) {
			return st;
		}
		
		/*
		 * Head's next field might not have been set yet, or may have
		 * been unset after setHead. So we must check to see if tail
		 * is actually first node. If not, we continue on, safely
		 * traversing from tail back to head to find first,
		 * guaranteeing termination.
		 */
		
		Node t = tail;
		Thread firstThread = null;
		while (t != null && t != head) {
			Thread tt = t.thread;
			if (tt != null) {
				firstThread = tt;
			}
			t = t.prev;
		}
		return firstThread;
	}
	
	/**
	 * 判断当前线程是否在队列当中
	 */
	public final boolean isQueued(Thread thread) {
		if (thread == null) {
			throw new NullPointerException();
		}
		for (Node p = tail; p != null; p = p.prev) {
			if (p.thread == thread) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns {@code true} if the apparent first queued thread, if one
	 * exists, is waiting in exclusive mode.  If this method returns
	 * {@code true}, and the current thread is attempting to acquire in
	 * shared mode (that is, this method is invoked from {@link
	 * #tryAcquireShared}) then it is guaranteed that the current thread
	 * is not the first queued thread.  Used only as a heuristic in
	 * ReentrantReadWriteLock.
	 */
	final boolean apparentlyFirstQueuedIsExclusive() {
		Node h, s;
		return (h = head) != null &&
				(s = h.next) != null &&
				!s.isShared() &&
				s.thread != null;
	}
	
	/**
	 * 没人排队当前线程可以抢锁
	 * 有人排队且排队第一位就是当前线程可以抢锁
	 * 即判断是否已经有线程在AQS的双向队列中排队, 返回false代表没人排队
	 */
	public final boolean hasQueuedPredecessors() {
		Node t = tail; // Read fields in reverse initialization order
		Node h = head;
		// s会指向头结点的next节点
		Node s;
		/*
		 * <ul>
		 *     <li/>h == t 表示头尾指针指向同一个节点, 这个是队列刚初始化的形态, 此时队列为空, 没有线程排队, 直接抢占锁资源.
		 *     <li/>(s = h.next) == null ?
		 *     <li/>s.thread != Thread.currentThread()
		 * </ul>
		 */
		return h != t &&
				//判断s==null只是为了后面s.thread可以null safe, 即如果s不等于null且s.thread不是当前线程, 那么就有线程在排队获取锁了
				//反过来如果当前线程排在第一名, 那么就可以抢锁
				((s = h.next) == null || s.thread != Thread.currentThread());
	}
	
	// Instrumentation and monitoring methods
	
	/**
	 * 同步队列长度
	 */
	public final int getQueueLength() {
		int n = 0;
		for (Node p = tail; p != null; p = p.prev) {
			if (p.thread != null) {
				++n;
			}
		}
		return n;
	}
	
	/**
	 * 获取队列等待thread集合
	 */
	public final Collection<Thread> getQueuedThreads() {
		ArrayList<Thread> list = new ArrayList<Thread>();
		for (Node p = tail; p != null; p = p.prev) {
			Thread t = p.thread;
			if (t != null) {
				list.add(t);
			}
		}
		return list;
	}
	
	/**
	 * 获取独占模式等待thread线程集合
	 */
	public final Collection<Thread> getExclusiveQueuedThreads() {
		ArrayList<Thread> list = new ArrayList<Thread>();
		for (Node p = tail; p != null; p = p.prev) {
			if (!p.isShared()) {
				Thread t = p.thread;
				if (t != null) {
					list.add(t);
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取共享模式等待thread集合
	 */
	public final Collection<Thread> getSharedQueuedThreads() {
		ArrayList<Thread> list = new ArrayList<Thread>();
		for (Node p = tail; p != null; p = p.prev) {
			if (p.isShared()) {
				Thread t = p.thread;
				if (t != null) {
					list.add(t);
				}
			}
		}
		return list;
	}
	
	/**
	 * Returns a string identifying this synchronizer, as well as its state.
	 * The state, in brackets, includes the String {@code "State ="}
	 * followed by the current value of {@link #getState}, and either
	 * {@code "nonempty"} or {@code "empty"} depending on whether the
	 * queue is empty.
	 *
	 * @return a string identifying this synchronizer, as well as its state
	 */
	public String toString() {
		int s = getState();
		String q = hasQueuedThreads() ? "non" : "";
		return super.toString() +
				"[State = " + s + ", " + q + "empty queue]";
	}
	
	
	// Internal support methods for Conditions
	
	/**
	 * 判断节点是否在同步队列中
	 */
	final boolean isOnSyncQueue(Node node) {
		//快速判断1：节点状态或者节点没有前置节点
		//注：同步队列是有头节点的, 而条件队列没有
		if (node.waitStatus == Node.CONDITION || node.prev == null) {
			return false;
		}
		//快速判断2：next字段只有同步队列才会使用, 条件队列中使用的是nextWaiter字段
		if (node.next != null) // If has successor, it must be on queue
		{
			return true;
		}
		return findNodeFromTail(node);
	}
	
	/**
	 * 从CLH队列的尾结点开始, 挨个与node比较, 如果 t == node, 说明node在同步队列中 <br/>
	 * Returns true if node is on sync queue by searching backwards from tail.
	 * Called only when needed by isOnSyncQueue.
	 *
	 * @return true if present
	 */
	private boolean findNodeFromTail(Node node) {
		//先从尾节点开始
		Node t = tail;
		for (; ; ) {
			if (t == node) {
				return true;
			}
			if (t == null) {
				return false;
			}
			t = t.prev;
		}
	}
	
	/**
	 * 将节点从条件队列移动到同步队列
	 * 如果转移到同步队列后, 
	 * 1: 前驱节点的状态是CANCELLED,
	 * 2: 将前驱节点的状态设为SIGNAL失败
	 * 则直接唤醒当前节点, 节点被唤醒后就可以继续竞争锁了
	 */
	final boolean transferForSignal(Node node) {
		/*
		 * 将waitStatus从-2设置为0
		 * 如果失败, 立刻返回false, 表示转移失败
		 */
		if (!compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
			return false;
		}
		
		/*
		 * 加入同步队列尾部当中, 返回前驱节点
		 */
		Node p = enq(node);
		/*
		 * CANCELLED 1     表示因为超时或者在这个线程上调用了interrupt()导致这个线程cancelled <br/>
		 *                       This node is cancelled due to timeout or interrupt. Nodes never leave this state.
		 *                       In particular, a thread with cancelled node never again blocks.
		 *           0     None of the above
		 * SIGNAL    -1    表示当前节点释放锁或者cancel时, 应该unpark后继节点(此时后继节点已经block住了) <br/>
		 *                       The successor of this node is blocked (via park), so the current node must unpark its successor when it releases or cancels.
		 * CONDITION -2    表示这个节点在条件队列里面?
		 *                       This node is currently on a condition queue.
		 *                       It will not be used as a sync queue node until transferred, at which time the status will be set to 0.
		 * PROPAGATE -3
		 *                       A releaseShared should be propagated to other nodes. <br/>
		 *                       This is set (for head node only) in doReleaseShared to ensure propagation continues,
		 *                       even if other operations have since intervened.
		 */
		int ws = p.waitStatus;
		/*
		 * ws > 0 表示前驱节点已经被cancelled
		 * CAS将前驱节点的状态改为SIGNAL失败
		 * 
		 * 出现以上两种情况之一就把当前线程唤醒
		 */
		if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL)) {
			LockSupport.unpark(node.thread); //唤醒当前节点
		}
		return true;
	}
	
	/**
	 * Transfers node, if necessary, to sync queue after a cancelled wait.
	 * Returns true if thread was cancelled before being signalled.
	 *
	 * @param node the node
	 * @return true if cancelled before the node was signalled
	 */
	final boolean transferAfterCancelledWait(Node node) {
		if (compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
			enq(node);
			return true;
		}
		/*
		 * If we lost out to a signal(), then we can't proceed
		 * until it finishes its enq().  Cancelling during an
		 * incomplete transfer is both rare and transient, so just
		 * spin.
		 */
		while (!isOnSyncQueue(node)) {
			Thread.yield();
		}
		return false;
	}
	
	/**
	 * 入参就是新创建的节点，即当前节点
	 */
	final int fullyRelease(Node node) {
		boolean failed = true;
		try {
			//这里这个取值要注意，获取当前的state并释放，这从另一个角度说明必须是独占锁
			//可以考虑下这个逻辑放在共享锁下面会发生什么？
			int savedState = getState();
			if (release(savedState)) {
				failed = false;
				return savedState;
			} else {
				//如果这里释放失败，则抛出异常
				throw new IllegalMonitorStateException();
			}
		} finally {
			/**
			 * 如果释放锁失败，则把节点取消，由这里就能看出来上面添加节点的逻辑中
			 * 只需要判断最后一个节点是否被取消就可以了
			 */
			if (failed) {
				node.waitStatus = Node.CANCELLED;
			}
		}
	}
	
	// Instrumentation methods for conditions
	
	/**
	 * Queries whether the given ConditionObject
	 * uses this synchronizer as its lock.
	 *
	 * @param condition the condition
	 * @return {@code true} if owned
	 * @throws NullPointerException if the condition is null
	 */
	public final boolean owns(ConditionObject condition) {
		return condition.isOwnedBy(this);
	}
	
	/**
	 * Queries whether any threads are waiting on the given condition
	 * associated with this synchronizer. Note that because timeouts
	 * and interrupts may occur at any time, a {@code true} return
	 * does not guarantee that a future {@code signal} will awaken
	 * any threads.  This method is designed primarily for use in
	 * monitoring of the system state.
	 *
	 * @param condition the condition
	 * @return {@code true} if there are any waiting threads
	 * @throws IllegalMonitorStateException if exclusive synchronization
	 *                                      is not held
	 * @throws IllegalArgumentException     if the given condition is
	 *                                      not associated with this synchronizer
	 * @throws NullPointerException         if the condition is null
	 */
	public final boolean hasWaiters(ConditionObject condition) {
		if (!owns(condition)) {
			throw new IllegalArgumentException("Not owner");
		}
		return condition.hasWaiters();
	}
	
	/**
	 * 获取条件队列长度
	 */
	public final int getWaitQueueLength(ConditionObject condition) {
		if (!owns(condition)) {
			throw new IllegalArgumentException("Not owner");
		}
		return condition.getWaitQueueLength();
	}
	
	/**
	 * 获取条件队列当中所有等待的thread集合
	 */
	public final Collection<Thread> getWaitingThreads(ConditionObject condition) {
		if (!owns(condition)) {
			throw new IllegalArgumentException("Not owner");
		}
		return condition.getWaitingThreads();
	}
	
	public class ConditionObject implements Condition, Serializable {
		private static final long serialVersionUID = 1173984872572414699L;
		
		/**
		 * 条件队列的头结点
		 * First node of condition queue.
		 */
		private transient Node firstWaiter;
		
		/**
		 * 条件队列的尾结点
		 * Last node of condition queue.
		 */
		private transient Node lastWaiter;
		
		/**
		 * Creates a new {@code ConditionObject} instance.
		 */
		public ConditionObject() {
		}
		
		// Internal methods
		
		/**
		 * 1.与CLH队列不同, 条件队列头尾指针是firstWaiter跟lastWaiter
		 * 2.条件队列是在获取锁之后, 也就是临界区进行操作, 因此很多地方不用考虑并发
		 * 3.条件队列是单向队列
		 */
		private Node addConditionWaiter() {
			Node t = lastWaiter;
			/*
			 * waitStatus 的几种状态
			 * 
			 * CANCELLED 1     表示因为超时或者在这个线程上调用了interrupt()导致这个线程cancelled <br/>
			 *                       This node is cancelled due to timeout or interrupt. Nodes never leave this state.
			 *                       In particular, a thread with cancelled node never again blocks.
			 *           0     None of the above
			 * SIGNAL    -1    表示当前节点释放锁或者cancel时, 应该unpark后继节点(此时后继节点已经block住了) <br/>
			 *                       The successor of this node is blocked (via park), so the current node must unpark its successor when it releases or cancels.
			 * CONDITION -2    表示这个节点在条件队列里面?
			 *                       This node is currently on a condition queue.
			 *                       It will not be used as a sync queue node until transferred, at which time the status will be set to 0.
			 * PROPAGATE -3
			 *                       A releaseShared should be propagated to other nodes. <br/>
			 *                       This is set (for head node only) in doReleaseShared to ensure propagation continues,
			 *                       even if other operations have since intervened.
			 */
			if (t != null && t.waitStatus != Node.CONDITION) {
				//删除所有被取消的节点
				unlinkCancelledWaiters();
				t = lastWaiter;
			}
			//创建一个类型为CONDITION的节点并加入队列
			Node node = new Node(Thread.currentThread(), Node.CONDITION);
			/*
			 * t 指向尾结点, 如果尾结点为null, 表示条件队列还没有初始化
			 */
			if (t == null) {
				firstWaiter = node;
			} else {
				t.nextWaiter = node;
			}
			lastWaiter = node;
			return node;
		}
		
		/**
		 * 移除条件队列的头结点, 将其转移到同步队列中, 如果转移到同步队列失败, 继续将头结点的nextWaiter转移过去
		 */
		private void doSignal(Node first) {
			do {
				/*
				 * 如果first.nextWaiter是null, 将头尾节点都设为null, 此时相当于条件队列是未初始化状态
				 */
				if ((firstWaiter = first.nextWaiter) == null) {
					lastWaiter = null;
				}
				/*
				 * 然后将这个节点的nextWaiter清掉, 也就是从条件队列中删掉了这个节点
				 */
				first.nextWaiter = null;
			} while (!transferForSignal(first) && //转移到同步队列中
					(first = firstWaiter) != null);
		}
		
		/**
		 * 通知所有节点移动到同步队列当中，并将节点从条件队列删除
		 */
		private void doSignalAll(Node first) {
			//先清空一下条件队列, 头尾节点都设为null
			lastWaiter = firstWaiter = null;
			do {
				/*
				 * 因为条件队列是单向队列, 所以把第一个节点的nextWaiter设为null, 就把这个节点给移除了
				 */
				Node next = first.nextWaiter;
				first.nextWaiter = null;
				transferForSignal(first);
				first = next;
			} while (first != null);
		}
		
		/**
		 * 删除条件队列当中被取消的节点
		 * 从第一个节点开始, 查看每个节点的状态是不是CONDITION, 如果不是, 清除掉这个节点
		 */
		private void unlinkCancelledWaiters() {
			Node t = firstWaiter; //t初始指向条件队列头结点
			Node trail = null;
			while (t != null) {
				Node next = t.nextWaiter; //next指向当前节点的下一个节点
				if (t.waitStatus != Node.CONDITION) {
					t.nextWaiter = null;
					if (trail == null) {
						firstWaiter = next; //头节点指针指向第二个节点
					} else {
						trail.nextWaiter = next;
					}
					if (next == null) {
						lastWaiter = trail;
					}
				} else {
					trail = t;
				}
				t = next;
			}
		}
		
		// public methods
		
		/**
		 * 发新号，通知条件队列当中节点到同步队列当中去排队
		 */
		public final void signal() {
			if (!isHeldExclusively())//节点不能已经持有独占锁
			{
				throw new IllegalMonitorStateException();
			}
			Node first = firstWaiter;
			if (first != null)
			/**
			 * 发信号通知条件队列的节点准备到同步队列当中去排队
			 */ {
				doSignal(first);
			}
		}
		
		/**
		 * 唤醒所有条件队列的节点转移到同步队列当中
		 */
		public final void signalAll() {
			if (!isHeldExclusively()) {
				throw new IllegalMonitorStateException();
			}
			Node first = firstWaiter;
			if (first != null) {
				doSignalAll(first);
			}
		}
		
		/**
		 * Implements uninterruptible condition wait.
		 * <ol>
		 * <li> Save lock state returned by {@link #getState}.
		 * <li> Invoke {@link #release} with saved state as argument,
		 *      throwing IllegalMonitorStateException if it fails.
		 * <li> Block until signalled.
		 * <li> Reacquire by invoking specialized version of
		 *      {@link #acquire} with saved state as argument.
		 * </ol>
		 */
		public final void awaitUninterruptibly() {
			Node node = addConditionWaiter();
			int savedState = fullyRelease(node);
			boolean interrupted = false;
			while (!isOnSyncQueue(node)) {
				LockSupport.park(this);
				if (Thread.interrupted()) {
					interrupted = true;
				}
			}
			if (acquireQueued(node, savedState) || interrupted) {
				selfInterrupt();
			}
		}
		
		/*
		 * For interruptible waits, we need to track whether to throw
		 * InterruptedException, if interrupted while blocked on
		 * condition, versus reinterrupt current thread, if
		 * interrupted while blocked waiting to re-acquire.
		 */
		
		/**
		 * 该模式表示在退出等待时重新中断
		 */
		private static final int REINTERRUPT = 1;
		/**
		 * 异常中断
		 */
		private static final int THROW_IE = -1;
		
		/**
		 * 这里的判断逻辑是：
		 * 1.如果现在不是中断的，即正常被signal唤醒则返回0
		 * 2.如果节点由中断加入同步队列则返回THROW_IE，由signal加入同步队列则返回REINTERRUPT
		 */
		private int checkInterruptWhileWaiting(Node node) {
			return Thread.interrupted() ?
					(transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) :
					0;
		}
		
		/**
		 * 根据中断时机选择抛出异常或者设置线程中断状态
		 */
		private void reportInterruptAfterWait(int interruptMode)
				throws InterruptedException {
			if (interruptMode == THROW_IE) {
				throw new InterruptedException();
			} else if (interruptMode == REINTERRUPT) {
				selfInterrupt();
			}
		}
		
		/**
		 * 某个线程调用condition.await(), 就是把该线程加入条件队列尾部等待，条件队列入口
		 */
		public final void await() throws InterruptedException {
			//如果当前线程被中断则直接抛出异常
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			//把当前节点加入条件队列
			Node node = addConditionWaiter();
			//释放掉已经获取的独占锁资源
			int savedState = fullyRelease(node);
			int interruptMode = 0;
			//如果不在同步队列中则不断挂起
			while (!isOnSyncQueue(node)) {
				LockSupport.park(this);
				//这里被唤醒可能是正常的signal操作也可能是中断
				if ((interruptMode = checkInterruptWhileWaiting(node)) != 0) {
					break;
				}
			}
			/**
			 * 走到这里说明节点已经条件满足被加入到了同步队列中或者中断了
			 * 这个方法很熟悉吧？就跟独占锁调用同样的获取锁方法，从这里可以看出条件队列只能用于独占锁
			 * 在处理中断之前首先要做的是从同步队列中成功获取锁资源
			 */
			if (acquireQueued(node, savedState) && interruptMode != THROW_IE) {
				interruptMode = REINTERRUPT;
			}
			//走到这里说明已经成功获取到了独占锁，接下来就做些收尾工作
			//删除条件队列中被取消的节点
			if (node.nextWaiter != null) // clean up if cancelled
			{
				unlinkCancelledWaiters();
			}
			//根据不同模式处理中断
			if (interruptMode != 0) {
				reportInterruptAfterWait(interruptMode);
			}
		}
		
		/**
		 * Implements timed condition wait.
		 * <ol>
		 * <li> If current thread is interrupted, throw InterruptedException.
		 * <li> Save lock state returned by {@link #getState}.
		 * <li> Invoke {@link #release} with saved state as argument,
		 *      throwing IllegalMonitorStateException if it fails.
		 * <li> Block until signalled, interrupted, or timed out.
		 * <li> Reacquire by invoking specialized version of
		 *      {@link #acquire} with saved state as argument.
		 * <li> If interrupted while blocked in step 4, throw InterruptedException.
		 * </ol>
		 */
		public final long awaitNanos(long nanosTimeout)
				throws InterruptedException {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = addConditionWaiter();
			int savedState = fullyRelease(node);
			final long deadline = System.nanoTime() + nanosTimeout;
			int interruptMode = 0;
			while (!isOnSyncQueue(node)) {
				if (nanosTimeout <= 0L) {
					transferAfterCancelledWait(node);
					break;
				}
				if (nanosTimeout >= spinForTimeoutThreshold) {
					LockSupport.parkNanos(this, nanosTimeout);
				}
				if ((interruptMode = checkInterruptWhileWaiting(node)) != 0) {
					break;
				}
				nanosTimeout = deadline - System.nanoTime();
			}
			if (acquireQueued(node, savedState) && interruptMode != THROW_IE) {
				interruptMode = REINTERRUPT;
			}
			if (node.nextWaiter != null) {
				unlinkCancelledWaiters();
			}
			if (interruptMode != 0) {
				reportInterruptAfterWait(interruptMode);
			}
			return deadline - System.nanoTime();
		}
		
		/**
		 * Implements absolute timed condition wait.
		 * <ol>
		 * <li> If current thread is interrupted, throw InterruptedException.
		 * <li> Save lock state returned by {@link #getState}.
		 * <li> Invoke {@link #release} with saved state as argument,
		 *      throwing IllegalMonitorStateException if it fails.
		 * <li> Block until signalled, interrupted, or timed out.
		 * <li> Reacquire by invoking specialized version of
		 *      {@link #acquire} with saved state as argument.
		 * <li> If interrupted while blocked in step 4, throw InterruptedException.
		 * <li> If timed out while blocked in step 4, return false, else true.
		 * </ol>
		 */
		public final boolean awaitUntil(Date deadline)
				throws InterruptedException {
			long abstime = deadline.getTime();
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = addConditionWaiter();
			int savedState = fullyRelease(node);
			boolean timedout = false;
			int interruptMode = 0;
			while (!isOnSyncQueue(node)) {
				if (System.currentTimeMillis() > abstime) {
					timedout = transferAfterCancelledWait(node);
					break;
				}
				LockSupport.parkUntil(this, abstime);
				if ((interruptMode = checkInterruptWhileWaiting(node)) != 0) {
					break;
				}
			}
			if (acquireQueued(node, savedState) && interruptMode != THROW_IE) {
				interruptMode = REINTERRUPT;
			}
			if (node.nextWaiter != null) {
				unlinkCancelledWaiters();
			}
			if (interruptMode != 0) {
				reportInterruptAfterWait(interruptMode);
			}
			return !timedout;
		}
		
		/**
		 * Implements timed condition wait.
		 * <ol>
		 * <li> If current thread is interrupted, throw InterruptedException.
		 * <li> Save lock state returned by {@link #getState}.
		 * <li> Invoke {@link #release} with saved state as argument,
		 *      throwing IllegalMonitorStateException if it fails.
		 * <li> Block until signalled, interrupted, or timed out.
		 * <li> Reacquire by invoking specialized version of
		 *      {@link #acquire} with saved state as argument.
		 * <li> If interrupted while blocked in step 4, throw InterruptedException.
		 * <li> If timed out while blocked in step 4, return false, else true.
		 * </ol>
		 */
		public final boolean await(long time, TimeUnit unit)
				throws InterruptedException {
			long nanosTimeout = unit.toNanos(time);
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			Node node = addConditionWaiter();
			int savedState = fullyRelease(node);
			final long deadline = System.nanoTime() + nanosTimeout;
			boolean timedout = false;
			int interruptMode = 0;
			while (!isOnSyncQueue(node)) {
				if (nanosTimeout <= 0L) {
					timedout = transferAfterCancelledWait(node);
					break;
				}
				if (nanosTimeout >= spinForTimeoutThreshold) {
					LockSupport.parkNanos(this, nanosTimeout);
				}
				if ((interruptMode = checkInterruptWhileWaiting(node)) != 0) {
					break;
				}
				nanosTimeout = deadline - System.nanoTime();
			}
			if (acquireQueued(node, savedState) && interruptMode != THROW_IE) {
				interruptMode = REINTERRUPT;
			}
			if (node.nextWaiter != null) {
				unlinkCancelledWaiters();
			}
			if (interruptMode != 0) {
				reportInterruptAfterWait(interruptMode);
			}
			return !timedout;
		}
		
		//  support for instrumentation
		
		/**
		 * Returns true if this condition was created by the given
		 * synchronization object.
		 *
		 * @return {@code true} if owned
		 */
		final boolean isOwnedBy(LoserAbstractQueuedSynchronizer sync) {
			return sync == LoserAbstractQueuedSynchronizer.this;
		}
		
		/**
		 * Queries whether any threads are waiting on this condition.
		 * Implements {@link LoserAbstractQueuedSynchronizer#hasWaiters(ConditionObject)}.
		 *
		 * @return {@code true} if there are any waiting threads
		 * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
		 *                                      returns {@code false}
		 */
		protected final boolean hasWaiters() {
			if (!isHeldExclusively()) {
				throw new IllegalMonitorStateException();
			}
			for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
				if (w.waitStatus == Node.CONDITION) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * 在条件队列上等待的线程数量
		 * Returns an estimate of the number of threads waiting on
		 * this condition.
		 * Implements {@link LoserAbstractQueuedSynchronizer#getWaitQueueLength(ConditionObject)}.
		 *
		 * @return the estimated number of waiting threads
		 * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
		 *                                      returns {@code false}
		 */
		protected final int getWaitQueueLength() {
			if (!isHeldExclusively()) {
				throw new IllegalMonitorStateException();
			}
			int n = 0;
			for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
				if (w.waitStatus == Node.CONDITION) {
					++n;
				}
			}
			return n;
		}
		
		/**
		 * 拿到条件队列中waitStatus是CONDITION的线程的集合
		 */
		protected final Collection<Thread> getWaitingThreads() {
			if (!isHeldExclusively()) {
				throw new IllegalMonitorStateException();
			}
			ArrayList<Thread> list = new ArrayList<Thread>();
			for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
				if (w.waitStatus == Node.CONDITION) {
					Thread t = w.thread;
					if (t != null) {
						list.add(t);
					}
				}
			}
			return list;
		}
	}
	
	/**
	 * Setup to support compareAndSet. We need to natively implement
	 * this here: For the sake of permitting future enhancements, we
	 * cannot explicitly subclass AtomicInteger, which would be
	 * efficient and useful otherwise. So, as the lesser of evils, we
	 * natively implement using hotspot intrinsics API. And while we
	 * are at it, we do the same for other CASable fields (which could
	 * otherwise be done with atomic field updaters).
	 * <p>
	 * unsafe魔法类，直接绕过虚拟机内存管理机制，修改内存
	 */
	private static final LoserUnsafe unsafe = LoserUnsafe.getUnsafe();
	
	/**
	 * 返回 state 内存地址相对于此对象(AbstractQueuedSynchronizer)的内存地址的偏移量
	 */
	private static final long stateOffset;
	/**
	 * 返回 head 内存地址相对于此对象(AbstractQueuedSynchronizer)的内存地址的偏移量
	 */
	private static final long headOffset;
	/**
	 * 返回 tail 内存地址相对于此对象(AbstractQueuedSynchronizer)的内存地址的偏移量
	 * <p>
	 * 指向同步等待队列的尾节点
	 */
	private static final long tailOffset;
	/**
	 * 返回 waitStatus 内存地址相对于此对象(Node)的内存地址的偏移量
	 * <p>
	 * waitStatus: 标记当前节点的信号量状态
	 */
	private static final long waitStatusOffset;
	/**
	 * 返回 next 内存地址相对于此对象(Node)的内存地址的偏移量
	 * <p>
	 * next: 当前节点的后继节点
	 */
	private static final long nextOffset;
	
	static {
		try {
			stateOffset = unsafe.objectFieldOffset
					(LoserAbstractQueuedSynchronizer.class.getDeclaredField("state"));
			headOffset = unsafe.objectFieldOffset
					(LoserAbstractQueuedSynchronizer.class.getDeclaredField("head"));
			tailOffset = unsafe.objectFieldOffset
					(LoserAbstractQueuedSynchronizer.class.getDeclaredField("tail"));
			waitStatusOffset = unsafe.objectFieldOffset
					(Node.class.getDeclaredField("waitStatus"));
			nextOffset = unsafe.objectFieldOffset
					(Node.class.getDeclaredField("next"));
			
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}
	
	/**
	 * CAS 设置头节点
	 */
	private final boolean compareAndSetHead(Node update) {
		return unsafe.compareAndSwapObject(this, headOffset, null, update);
	}
	
	/**
	 * CAS 修改尾节点, 修改成功后tail指向update
	 */
	private final boolean compareAndSetTail(Node expect, Node update) {
		return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
	}
	
	/**
	 * CAS 修改信号量状态.
	 */
	private static final boolean compareAndSetWaitStatus(Node node,
	                                                     int expect,
	                                                     int update) {
		return unsafe.compareAndSwapInt(node, waitStatusOffset,
				expect, update);
	}
	
	/**
	 * CAS 修改CLH队列的后继节点
	 */
	private static final boolean compareAndSetNext(Node node,
	                                               Node expect,
	                                               Node update) {
		return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
	}
}
