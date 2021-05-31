package com.loserico.networking;

import com.loserico.networking.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-05-12 17:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class IPUtilsTest {
	
	private static final InetAddressValidator validator = InetAddressValidator.getInstance();
	
	@Test
	public void testValidIpV4() {
		List<String> validIpV4s = Arrays.asList("0.0.0.0",
				"0.0.0.1",
				"127.0.0.1",
				"1.2.3.4",
				"11.1.1.0",
				"101.1.1.0",
				"201.1.1.0",
				"255.255.255.255",
				"192.168.1.1",
				"192.168.1.255",
				"100.100.100.100",
				"10.0.1.1/24",
				"216.202.192.66/22");
		validIpV4s.forEach((ip) -> {
			boolean valid = IPUtils.isValidIpV4(ip);
			assertTrue(valid);
			log.info("IP {} is {}", ip, (valid ? "valid" : "invalid"));
		});
		/*validIpV4s.forEach((ip) -> {
			boolean valid = validator.isValidInet4Address(ip);
			assertTrue(valid);
			log.info("IP {} is {}", ip, (valid ? "valid" : "invalid"));
		});
		validIpV4s.forEach((ip) -> {
			boolean valid = InetAddresses.isInetAddress(ip);
			assertTrue(valid);
			log.info("IP {} is {}", ip, (valid ? "valid" : "invalid"));
		});*/
	}
	
	@Test
	public void testInvalidIpV4() {
		List<String> invalidIpV4s = Arrays.asList("000.000.000.000",          // leading 0
				"00.00.00.00",              // leading 0
				"1.2.3.04",                 // leading 0
				"1.02.03.4",                // leading 0
				"1.2",                      // 1 dot
				"1.2.3",                    // 2 dots
				"1.2.3.4.5",                // 4 dots
				"192.168.1.1.1",            // 4 dots
				"256.1.1.1",                // 256
				"1.256.1.1",                // 256
				"1.1.256.1",                // 256
				"1.1.1.256",                // 256
				"-100.1.1.1",               // -100
				"1.-100.1.1",               // -100
				"1.1.-100.1",               // -100
				"1.1.1.-100",               // -100
				"1...1",                    // empty between .
				"1..1",                     // empty between .
				"1.1.1.1.",                 // last .
				"1.1.1.1/0",               
				"1.1.1.1/33",               
				"");
		invalidIpV4s.forEach((ip) -> {
			boolean valid = IPUtils.isValidIpV4(ip);
			assertFalse(valid);
			log.info("IP {} is {}", ip, (valid ? "valid" : "invalid"));
		});
	}
	
	@Test
	public void testIpInRange() {
		assertTrue(IPUtils.isIpInRange("192.168.2.1", "192.168.2.1")); // true
		assertFalse(IPUtils.isIpInRange("192.168.2.1", "192.168.2.0/32")); // false
		assertTrue(IPUtils.isIpInRange("192.168.2.5", "192.168.2.0/24")); // true
		assertFalse(IPUtils.isIpInRange("92.168.2.1", "fe80:0:0:0:0:0:c0a8:1/120")); // false
		assertTrue(IPUtils.isIpInRange("fe80:0:0:0:0:0:c0a8:11", "fe80:0:0:0:0:0:c0a8:1/120")); // true
		assertFalse(IPUtils.isIpInRange("fe80:0:0:0:0:0:c0a8:11", "fe80:0:0:0:0:0:c0a8:1/128")); // false
		assertFalse(IPUtils.isIpInRange("fe80:0:0:0:0:0:c0a8:11", "192.168.2.0/32")); // false
		
		assertTrue(IPUtils.isValidIpV6("fe80:0:0:0:0:0:c0a8:11"));
		assertTrue(IPUtils.isValidIpV6("fe80:0:0:0:0:0:c0a8:1/128"));
		assertFalse(IPUtils.isValidIpV6("fe80:0:0:0:0:0:c0a8:1/129"));
		assertTrue(IPUtils.isValidIpV6("fe80:0:0:0:0:0:c0a8:1/120"));
		assertFalse(IPUtils.isValidIpV6("fe80:0:0:0:0:0:c0a8:1/0"));
		assertTrue(IPUtils.isValidIpV4("192.168.2.0/32"));
	}
}
