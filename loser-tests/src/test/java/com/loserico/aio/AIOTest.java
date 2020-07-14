package com.loserico.aio;

/**
 * JSR 203: More New I/O APIs for the JavaTM Platform ("NIO.2")
 * 
 * Asynchronous I/O
 * 
 * NIO provides multiplexed I/O (a combination of nonblocking I/O, discussed in
 * Chapter 7, and readiness selection, discussed in Chapter 8) to facilitate the
 * creation of highly scalable servers. Client code registers a socket channel with a
 * selector to be notified when the channel is ready to start I/O.
 * 
 * NIO.2 provides asynchronous I/O, which lets client code initiate an I/O operation
 * and subsequently notifies the client when the operation is complete. Like
 * multiplexed I/O, asynchronous I/O is also commonly used to facilitate the creation
 * of highly scalable servers.
 * 
 * Multiplexed I/O is often used with operating systems that offer highly scalable and
 * performant polling interfaces—Linux and Solaris are examples. Asynchronous I/O is
 * often used with operating systems that provide highly scalable and performant
 * asynchronous I/O facilities—newer Windows operating systems come to mind.
 * 
 * @author Rico Yu
 * @since 2016-12-15 18:54
 * @version 1.0
 *
 */
public class AIOTest {

}
