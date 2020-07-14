package com.loserico.orm.utils;

import com.loserico.orm.exception.NoSuchHashAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(HashUtils.class);

	public static String sha256(String source) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			logger.error("不支持该Hash算法[{}]", "SHA-256");
			throw new NoSuchHashAlgorithmException(e);
		}
		byte[] encodedhash = digest.digest(source.getBytes(StandardCharsets.UTF_8));
		
		StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < encodedhash.length; i++) {
	    String hex = Integer.toHexString(0xff & encodedhash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	   return hexString.toString();
	}
}
