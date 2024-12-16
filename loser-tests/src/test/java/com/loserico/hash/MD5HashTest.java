package com.loserico.hash;

import com.loserico.common.lang.utils.IOUtils;
import lombok.SneakyThrows;
import org.junit.Test;

import jakarta.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Copyright: (C), 2020-09-07 9:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MD5HashTest {
	
	/**
	 * generates hash for a password and then verifies it
	 */
	@SneakyThrows
	@Test
	public void testGivenPassword_whenHashing_thenVerifying() {
		String hash = "35454B055CC325EA1AF2126E27707052";
		String password = "ILoveJava";
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(password.getBytes());
		byte[] digest = messageDigest.digest();
		String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		assertThat(myHash.equals(hash)).isTrue();
	}
	
	/**
	 * Similarly, we can also verify checksum of a file
	 */
	@SneakyThrows
	@Test
	public void testGivenFile_generatingChecksum_thenVerifying() {
		String checkSum = "5EB63BBBE01EEED093CB22BB8F5ACDC3";
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(IOUtils.readClassPathFileAsBytes("test_md5.txt"));
		byte[] digest = messageDigest.digest();
		String myCheckSum = DatatypeConverter.printHexBinary(digest).toUpperCase();
		assertThat(myCheckSum.equals(checkSum)).isTrue();
	}
}
