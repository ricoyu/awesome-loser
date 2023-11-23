package com.loserico.bio;

import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * <p>
 * Copyright: (C), 2023-10-29 21:36
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InetAddressTest {
	
	@Test
	public void testGetIpAddress() throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName("ss.loserico.ltd");
		System.out.println(inetAddress.getHostAddress()); //打印IP地址
		System.out.println(inetAddress); //打印IP地址
	}
	
	@Test
	public void testGetAll() throws UnknownHostException {
		InetAddress[] addresses = InetAddress.getAllByName("www.baidu.com");
		for (int i = 0; i < addresses.length; i++) {
			System.out.println(addresses[i]);
		}
	}
	
	@Test
	public void testNetworkInterface() throws SocketException {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			System.out.println(networkInterface);
		}
	}
}
