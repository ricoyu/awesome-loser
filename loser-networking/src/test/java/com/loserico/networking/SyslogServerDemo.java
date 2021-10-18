package com.loserico.networking;

import org.graylog2.syslog4j.SyslogConstants;
import org.graylog2.syslog4j.server.SyslogServer;
import org.graylog2.syslog4j.server.SyslogServerConfigIF;
import org.graylog2.syslog4j.server.SyslogServerEventIF;
import org.graylog2.syslog4j.server.SyslogServerIF;
import org.graylog2.syslog4j.server.SyslogServerSessionEventHandlerIF;

import java.net.SocketAddress;

/**
 * <p>
 * Copyright: (C), 2021-09-30 15:14
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SyslogServerDemo {
	
	public static void main(String[] args) throws InterruptedException {
		SyslogServerIF server = SyslogServer.getInstance("tcp");
		SyslogServerConfigIF config = server.getConfig();
		config.setHost("172.23.12.66");
		config.setPort(514);
		//config.setHost("10.10.26.14");
		//config.setPort(31514);
		config.addEventHandler(new SyslogServerSessionEventHandlerIF() {
			@Override
			public Object sessionOpened(SyslogServerIF syslogServer, SocketAddress socketAddress) {
				return null;
			}
			
			@Override
			public void event(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
				System.out.println("receive from:" + socketAddress + "\tmessage" + event.getMessage());
			}
			
			@Override
			public void exception(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) {
				
			}
			
			@Override
			public void sessionClosed(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, boolean timeout) {
				
			}
			
			@Override
			public void initialize(SyslogServerIF syslogServer) {
				
			}
			
			@Override
			public void destroy(SyslogServerIF syslogServer) {
				
			}
		});
		org.graylog2.syslog4j.server.SyslogServer.getThreadedInstance(SyslogConstants.UDP);
		Thread.currentThread().join();
	}
}
