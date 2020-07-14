package com.loserico.hex;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JDKHashTest {

	@Test
	public void testSha256Hash() throws NoSuchAlgorithmException {
		String originalString = "asdasdad";
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
	}
	
	@Test
	public void testSha256Hash2HexDecimal() throws NoSuchAlgorithmException {
		String originalString = "asdasdad";
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		
		StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < encodedhash.length; i++) {
	    String hex = Integer.toHexString(0xff & encodedhash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    System.out.println(hexString.toString());
	}
	
	@Test
	public void testHashCode() {
		String s = "a";
		System.out.println(s.hashCode());
		System.out.println(s.hashCode() >>> 16);
	}
	
}
