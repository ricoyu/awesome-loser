package com.loserico.networking.matcher;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Matches a request based on IP Address or subnet mask matching against the remote
 * address.
 * <p>
 * Both IPv6 and IPv4 addresses are supported, but a matcher which is configured with an
 * IPv4 address will never match a request which returns an IPv6 address, and vice-versa.
 *
 * @author Luke Taylor
 * @since 3.0.2
 */
public final class IpAddressMatcher implements RequestMatcher {
	private final int nMaskBits;
	private final InetAddress requiredAddress;

	/**
	 * Takes a specific IP address or a range specified using the IP/Netmask (e.g.
	 * 192.168.1.0/24 or 202.24.0.0/14).
	 *
	 * @param ipAddress the address or range of addresses from which the request must
	 * come.
	 */
	public IpAddressMatcher(String ipAddress) {

		if (ipAddress.indexOf('/') > 0) {
			String[] addressAndMask = StringUtils.split(ipAddress, "/");
			ipAddress = addressAndMask[0];
			nMaskBits = Integer.parseInt(addressAndMask[1]);
		}
		else {
			nMaskBits = -1;
		}
		requiredAddress = parseAddress(ipAddress);
		Assert.isTrue(requiredAddress.getAddress().length * 8 >= nMaskBits,
				"IP 地址长度太短");
	}

	public boolean matches(HttpServletRequest request) {
		return matches(request.getRemoteAddr());
	}

	public boolean matches(String address) {
		InetAddress remoteAddress = null;
		try {
			remoteAddress = parseAddress(address);
		} catch (IllegalArgumentException e) {
			return false;
		}

		if (!requiredAddress.getClass().equals(remoteAddress.getClass())) {
			return false;
		}

		if (nMaskBits < 0) {
			return remoteAddress.equals(requiredAddress);
		}

		byte[] remAddr = remoteAddress.getAddress();
		byte[] reqAddr = requiredAddress.getAddress();

		int nMaskFullBytes = nMaskBits / 8;
		byte finalByte = (byte) (0xFF00 >> (nMaskBits & 0x07));

		// System.out.println("Mask is " + new sun.misc.HexDumpEncoder().encode(mask));

		for (int i = 0; i < nMaskFullBytes; i++) {
			if (remAddr[i] != reqAddr[i]) {
				return false;
			}
		}

		if (finalByte != 0) {
			return (remAddr[nMaskFullBytes] & finalByte) == (reqAddr[nMaskFullBytes] & finalByte);
		}

		return true;
	}

	private InetAddress parseAddress(String address) {
		try {
			return InetAddress.getByName(address);
		}
		catch (UnknownHostException e) {
			throw new IllegalArgumentException("Failed to parse address " + address, e);
		}
	}
}
