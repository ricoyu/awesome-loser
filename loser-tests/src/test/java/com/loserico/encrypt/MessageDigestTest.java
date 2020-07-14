package com.loserico.encrypt;

import org.junit.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * Copyright: (C), 2020/3/4 14:07
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MessageDigestTest {
	
	@Test
	public void testSha1() throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		/**
		 * digest() method is called to calculate message digest of the input string returned as array of byte 
		 */
		byte[] bytes = messageDigest.digest("hello".getBytes(StandardCharsets.UTF_8));
		/**
		 * Convert byte array into signum representation 
		 */
		BigInteger no = new BigInteger(1, bytes);
		/**
		 * Convert message digest into hex value 
		 */
		String hash = no.toString(16);
		/*
		 * Add preceding 0s to make it 32 bit
		 */
		while (hash.length() < 32) {
			hash = "0" + hash;
		}
		
		System.out.println(hash);
	}
	
	@Test
	public void testSha224() throws NoSuchAlgorithmException {
		/*
		 * getInstance() method is called with algorithm SHA-224
		 */
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-224");
		/*
		 * digest() method is called to calculate message digest of the input string returned as array of byte 
		 */
		byte[] bytes = messageDigest.digest("hello".getBytes(StandardCharsets.UTF_8));
		/*
		 * Convert byte array into signum representation
		 */
		BigInteger signsum = new BigInteger(1, bytes);
		String hash = signsum.toString(16);
		
		/*
		 * Add preceding 0s to make it 32 bit
		 */
		while (hash.length() < 32) {
			hash = "0" + hash;
		}
		
		System.out.println(hash);
	}
	
	@Test
	public void testMD2() throws NoSuchAlgorithmException {
		// getInstance() method is called with algorithm MD2 
		MessageDigest md = MessageDigest.getInstance("MD2");
		
		// digest() method is called 
		// to calculate message digest of the input string 
		// returned as array of byte 
		byte[] messageDigest = md.digest("hello".getBytes(StandardCharsets.UTF_8));
		
		// Convert byte array into signum representation 
		BigInteger no = new BigInteger(1, messageDigest);
		
		// Convert message digest into hex value 
		String hashtext = no.toString(16);
		
		// Add preceding 0s to make it 32 bit 
		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		System.out.println(hashtext);
	}
	
	@Test
	public void testMD5() throws NoSuchAlgorithmException {
		// getInstance() method is called with algorithm MD2 
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		// digest() method is called 
		// to calculate message digest of the input string 
		// returned as array of byte 
		byte[] messageDigest = md.digest("hello".getBytes(StandardCharsets.UTF_8));
		
		// Convert byte array into signum representation 
		BigInteger no = new BigInteger(1, messageDigest);
		
		// Convert message digest into hex value 
		String hashtext = no.toString(16);
		
		// Add preceding 0s to make it 32 bit 
		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		System.out.println(hashtext);
	}
	
	@Test
	public void testSHA512() throws NoSuchAlgorithmException {
		// getInstance() method is called with algorithm MD2 
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		
		// digest() method is called 
		// to calculate message digest of the input string 
		// returned as array of byte 
		byte[] messageDigest = md.digest("hello".getBytes(StandardCharsets.UTF_8));
		
		// Convert byte array into signum representation 
		BigInteger no = new BigInteger(1, messageDigest);
		
		// Convert message digest into hex value 
		String hashtext = no.toString(16);
		
		// Add preceding 0s to make it 32 bit 
		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		System.out.println(hashtext);
	}
}
