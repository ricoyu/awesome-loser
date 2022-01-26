/**
 * 非阻塞 IO 的核心在于使用一个 Selector 来管理多个通道，可以是 SocketChannel，也可以是 ServerSocketChannel，将各个通道注册到 Selector 上，指定监听的事件 <p/>
 * 之后可以只用一个线程来轮询这个 Selector，看看上面是否有通道是准备好的，当通道准备好可读或可写，然后才去开始真正的读写，这样速度就很快了。
 * <p/>
 * NIO 中 Selector 是对底层操作系统实现的一个抽象，管理通道状态其实都是底层系统实现的，这里简单介绍下在不同系统下的实现。
 * 
 * <ul>
 *     <li/>select  上世纪 80 年代就实现了，它支持注册 FD_SETSIZE(1024) 个 socket，在那个年代肯定是够用的，不过现在嘛，肯定是不行了
 *     <li/>poll    1997 年，出现了 poll 作为 select 的替代者，最大的区别就是，poll 不再限制 socket 数量。
 *     <li/>epoll   2002 年随 Linux 内核 2.5.44 发布，epoll 能直接返回具体的准备好的通道，时间复杂度 O(1)。
 * </ul>
 * 
 * select 和 poll 都有一个共同的问题，那就是它们都只会告诉你有几个通道准备好了，但是不会告诉你具体是哪几个通道。
 * 所以，一旦知道有通道准备好以后，自己还是需要进行一次扫描，显然这个不太好，通道少的时候还行，一旦通道的数量是几十万个以上的时候，
 * 扫描一次的时间就很可观了，时间复杂度 O(n)。所以，后来才催生了epoll实现。 <p/>
 * 
 * 我们回到 Selector，毕竟 JVM 就是这么一个屏蔽底层实现的平台，我们面向 Selector 编程就可以了
 * 
 * <p>
 * Copyright: Copyright (c) 2022-01-25 14:11
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
package com.loserico.nio.nonblockingdemo;
