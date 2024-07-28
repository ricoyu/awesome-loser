package com.loserico.codec;

import com.google.common.hash.Hashing;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import jakarta.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * The SHA (Secure Hash Algorithm) is one of the popular cryptographic hash functions. 
 * A cryptographic hash can be used to make a signature for a text or a data file.<p>
 * The SHA-256 algorithm generates an almost-unique, fixed-size 256-bit (32-byte) hash. 
 * This is a one-way function, so the result cannot be decrypted back to the original value.<p>
 * Currently, SHA-2 hashing is widely used as it is being considered as the most secure hashing algorithm in the cryptographic arena.
 * In this article, let’s have a look how we can perform SHA-256 hashing operations using various Java libraries.
 * <p>
 * Copyright: Copyright (c) 2017-12-22 10:57
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu	ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class SHA2Test {

	@Test
	public void testSHA256ByMessageDigest() throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		String originalString = "123456";
		byte[] encodedhash = digest.digest(originalString.getBytes(UTF_8));
		String hexString = bytesToHex2(encodedhash);
		System.out.println(hexString);
	}

	@Test
	public void testSHA256ByGuava() {
		String originalString = "password123456";
		String hexString = Hashing.sha256()
				.hashString(originalString, UTF_8)
				.toString();
		System.out.println(hexString);
	}

	@Test
	public void testSHA256ByApacheCommonCodec() {
		String originalString = "password123456";
		String hexString = DigestUtils.sha256Hex(originalString);
		System.out.println(hexString);
	}
	
	private static final char[] hexCode = "0123456789abcdef".toCharArray();
	private static String bytesToHex2(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
	}
	
	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private static String bytes2Hex(byte[] bytes) {
		return DatatypeConverter.printHexBinary(bytes);
	}
}
