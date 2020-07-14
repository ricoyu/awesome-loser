package com.loserico.aio;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The abstract java.nio.channels.AsynchronousChannelGroup class describes a grouping
 * of asynchronous channels for the purpose of resource sharing.
 * 
 * A group has an associated thread pool to which tasks are submitted, to handle I/O
 * events and to dispatch to completion handlers that consume the results of
 * asynchronous operations performed on the group’s channels.
 * 
 * AsynchronousServerSocketChannels and AsynchronousSocketChannels belong to groups.
 * When you create an AsynchronousServerSocketChannel or an AsynchronousSocketChannel
 * via the noargument open() method, the channel is bound to the default group, which
 * is the system-wide channel group that’s automatically constructed and maintained by
 * the Java virtual machine (JVM). The default group has an associated thread pool
 * that creates new threads as needed.
 * 
 * @author Rico Yu
 * @since 2016-12-18 21:37
 * @version 1.0
 *
 */
public class AsynchronousChannelGroupsTest {

	private static final Logger logger = LoggerFactory.getLogger(AsynchronousChannelGroupsTest.class);

	@Test
	public void testDefaultAsynChannelGroup() {
		try {
			AsynchronousChannelGroup group = AsynchronousChannelGroup.withFixedThreadPool(20,
					Executors.defaultThreadFactory());
			AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(group);
			serverSocketChannel.bind(new InetSocketAddress(9999));

			serverSocketChannel.close();
			System.out.println("Is terminated: " + group.isTerminated());
			System.out.println("Is shutdown: " + group.isShutdown());
			group.shutdown();
			System.out.println("Is shutdown: " + group.isShutdown());
			// SECONDS.sleep(2);
			System.out.println("Is terminated: " + group.isTerminated());
			group.awaitTermination(6, SECONDS);
			System.out.println("Is terminated: " + group.isTerminated());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testForceShutdown() {
		try {
			AsynchronousChannelGroup group = AsynchronousChannelGroup
					.withCachedThreadPool(Executors.newCachedThreadPool(), 20);
			AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open(group);
			channel.bind(new InetSocketAddress(9999));
			channel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

				@Override
				public void completed(AsynchronousSocketChannel asynchronousSocketChannel, Object attachment) {
					System.out.println("complete");
				}

				@Override
				public void failed(Throwable e, Object attachment) {
					System.out.println("failed");
				}

			});
			System.out.println("isShutdown: " + group.isShutdown());
			if (!group.isShutdown()) {
				// After the group is shut down, no more channels can be bound to it.
				group.shutdown();
			}
			System.out.println("isShutdown: " + group.isShutdown());
			System.out.println("isTerminated: " + group.isTerminated());
			if (!group.isTerminated()) {
				// Forcibly shut down the group. The channel is closed and the
				// accept operation aborts.
				group.shutdownNow();
			}
			System.out.println("isTerminated: " + group.isTerminated());
			// The group should be able to terminate; wait for 10 seconds maximum.
			group.awaitTermination(10, SECONDS);
			System.out.println("isTerminated: " + group.isTerminated());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
