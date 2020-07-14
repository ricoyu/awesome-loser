package com.loserico.zookeeper.leader2;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;

/**
 * 在很多场景中,我们需要"leader选举";比如在分布式有很多task,这些task的执行时机需要一个"Leader"去调度.任何时候,同一个leaderPath节点下,只会有一个"leader"..如下展示如何简单的使用LeaderSelector.
 * 
 * 开发者使用LeaderSelector时,需要关注takeLeadership方法的内部逻辑,一旦takeLeadership方法被调用,那么此selector已经是leader角色了,你可以在此方法中增加"事件通知"等来执行一些异步的操作.
 * isLeader()方法只能返回当前的状态,有可能返回true之后不久,这个selector实例将不不再是leader,那么就需要我们在listener中更多的关注stateChanged过程.
 * 
 * @author Rico Yu
 * @since 2016-12-25 20:32
 * @version 1.0
 *
 */
public class LeaderSelectorClient {

	private LeaderSelector selector;

	private final Object lock = new Object();
	private boolean isLeader = false;

	public LeaderSelectorClient(CuratorFramework client, String leaderPath) throws Exception {
		LeaderSelectorListener selectorListener = new LeaderSelectorListener() {
			//此方法将会在Selector的线程池中的线程调用  
			@Override
			public void takeLeadership(CuratorFramework client) throws Exception {
				System.out.println("I am leader...");
				//如果takeLeadership方法被调用,说明此selector实例已经为leader  
				//此方法需要阻塞,直到selector放弃leader角色  
				isLeader = true;
				while (isLeader) {
					synchronized (lock) {
						lock.wait();
					}
				}
			}

			//这个方法将会在Zookeeper主线程中调用---watcher响应时  
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				System.out.println("Connection state changed...");
				//对于LeaderSelector,底层实现为对leaderPath节点使用了"排他锁",  
				//"排他锁"的本质,就是一个"临时节点"  
				//如果接收到LOST,说明此selector实例已经丢失了leader信息.  
				if (newState == ConnectionState.LOST) {
					isLeader = false;
					synchronized (lock) {
						lock.notifyAll();
					}
				}
			}
		};
		selector = new LeaderSelector(client, leaderPath, selectorListener);
		//一旦leader释放角色之后,是否继续参与leader的选举  
		//此处需要关注CuratorFrameworker.RetryPolicy策略.  
		//1) 如果leader是耐久性的,selector实例需要一致关注leader的状态,可以autoRequeue  
		//2) 如果leader再行使完任务之后,释放,然后在此后的某个时刻再次选举(比如定时任务),此处可以保持默认值false  
		selector.autoRequeue();
	}

	public void start() {
		selector.start();
	}

	public void release() {
		//释放leader角色  
		isLeader = false;
		//takeLeadership方法将会中断并返回.  
		selector.interruptLeadership();
		synchronized (lock) {
			lock.notifyAll();//  
		}
	}

	//重新获取leader角色--选举  
	public void take() {
		selector.requeue();
	}

	public void close() {
		isLeader = false;
		selector.close();
		synchronized (lock) {
			lock.notifyAll();//  
		}
	}

	public boolean isLeader() {
		return selector.hasLeadership();
	}

}