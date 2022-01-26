/**
 * 阻塞模式的性能瓶颈:
 * <ol>
 *     <li/>首先，每次来一个连接都开一个新的线程这肯定是不合适的。当活跃连接数在几十几百的时候当然是可以这样做的，
 *          但如果活跃连接数是几万几十万的时候，这么多线程明显就不行了。每个线程都需要一部分内存，内存会被迅速消耗，同时，线程切换的开销非常大。
 *     <li/>其次，阻塞操作在这里也是一个问题。首先，accept() 是一个阻塞操作，当 accept() 返回的时候，代表有一个连接可以使用了，
 *          我们这里是马上就新建线程来处理这个 SocketChannel，但是，但是这里不代表对方就将数据传输过来了。
 *          所以，SocketChannel#read 方法将阻塞，等待数据，明显这个等待是不值得的。同理，write 方法也需要等待通道可写才能执行写入操作，这边的阻塞等待也是不值得的。
 * </ol>
 * <p>
 * Copyright: Copyright (c) 2022-01-25 14:05
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
package com.loserico.nio.blockingdemo;
