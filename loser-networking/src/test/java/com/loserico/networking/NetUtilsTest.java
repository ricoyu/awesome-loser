package com.loserico.networking;

import com.loserico.networking.utils.NetUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-02-02 11:09
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class NetUtilsTest {
	
	@Test
	public void testIsValidIpV4() {
		/*boolean validIpV4 = NetUtils.isValidIpV4("10.10.16.8");
		assertTrue(validIpV4);
		assertFalse(NetUtils.isValidIpV4("10.10.16"));
		assertFalse(NetUtils.isValidIpV4("10.10.16.j"));
		assertFalse(NetUtils.isValidIpV4("10.10.16.256"));*/
		
		System.out.println(NetUtils.isValidIpV4("10.10.10.0/24"));
		System.out.println(NetUtils.isValidIpV4("10.10.10.1/24"));
		System.out.println(NetUtils.isValidIpV4("10.10.10.0"));
	}
	
	@Test
	public void testIsValidIpV6() {
		assertTrue(NetUtils.isValidIpV6("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
		assertTrue(NetUtils.isValidIpV6("1050:0000:0000:0000:0005:0600:300c:326b"));
		assertTrue(NetUtils.isValidIpV6("2001:0db8:3c4d:0015::1"));
	}
	
	@Test
	public void testSubnetMask() {
		//System.out.println(NetUtils.subnetMask("xx.xx.xx.2/24"));
		//System.out.println(NetUtils.subnetMask("xx.xx.xx.2"));
		System.out.println("0010000000000001000011011011100000111100010011010000000000010101".length());
		System.out.println("00100000000000010000110110111000001111000100110100000000000101010000000000000000000000000000000000000000000000000000000000000000".length());
		System.out.println(NetUtils.isValidIpV6("2001:0db8:3c4d:0015::/64"));
	}
	
	
}
