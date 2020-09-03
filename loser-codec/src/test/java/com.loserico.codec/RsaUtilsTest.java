package com.loserico.codec;

import org.junit.Test;

/**
 * <p>
 * Copyright: (C), 2020-08-28 16:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RsaUtilsTest {
	
	@Test
	public void testPublicEncrypt() {
		System.out.println(RsaUtils.publicEncrypt("123456", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm0TOI2rlCmYWDVDu3EJvvdxK6/esXgF+iPvZQPO1cLxe+2JU5cqthD4mB8tOz6J8A31oAtS82hVkEzHv6d/dDOiAOJ93oKOd4+3+PKNtN09TV4YAsMu5oRnGd5SwXNDJX88v6oiHfTwMYAOxdNeACK9ySKYZYdbZZUZ9pnmC1mbeHjdeVAaFZgmkfg7EJifZ8T/Fz2rAKDIdHNNVCMHPYvzkNYmzX6AvqM9sJB9f38YuUxf1jbC0x/QAYwEiAZgqJ7U091FCkRmQBC4gOsHuY1AfyPNslRnBGKlGDJtPePWy1H08BQ5hy0ZorD/ZX9vClksnoCY38EqwHk+CSByHcQIDAQAB"));
	}
}
