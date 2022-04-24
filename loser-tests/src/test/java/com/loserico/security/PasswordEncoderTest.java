package com.loserico.security;

import com.loserico.codec.RsaUtils;
import org.junit.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * Copyright: (C), 2022-03-01 16:22
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PasswordEncoderTest {
	
	@Test
	public void test() {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String encode = passwordEncoder.encode("Admin#124");
		System.out.println(encode);
	}
	
	@Test
	public void testRsaEncrypt() {
		String encrypted = RsaUtils.publicEncrypt("Admin#124");
		System.out.println(encrypted);
	}
}
