package com.loserico.codec;

import org.junit.Test;

public class RsaTest {

	@Test
	public void testPublicEncryptPrivateDecrypt() {
		Rsa rsa = Rsa.instance();
		String data = "你好，这是RSA非对称加密测试";
		//		String data = "hello world";

		String encrypted = rsa.publicEncrypt(data);
		System.out.println(encrypted);
		
		//System.out.println(rsa.publicKey());
		String encrypted2 = rsa.publicEncrypt(data, rsa.publicKey());
		System.out.println(encrypted2);
		
		String decrypted = rsa.privateDecrypt(encrypted);
		System.out.println(decrypted);
		String decrypted2 = rsa.privateDecrypt(encrypted2);
		System.out.println(decrypted);
		
		//String dataFromJs = "C266RxLcM5xGeZbn25AeZL7N1v8y8WCUZLDd22DYvPO9Taqf0Q/1sW3uvzINCDMB5PoUR/lu03BdsrSa+ETIX4g0NRkRrh66M3V2anRTh51ZitDgtMhgw3yf9h4atMyNojLbXVoZG7Kk2jS34LIr4kAhz1yMhHOnZr16SlNdj0g=";
		String dataFromJs = "P2KoWGC4/v9iZJI1oaBna7M9Kd9PeC6hD0wMdAc2H1WnBVWPz7bsaU+2PmCpoKOfWhYqR5FORaw3ZAJGzBi1BoL0XDOBLI+2kbLZgzDKWDgoy37iYXYi9hkNUiqF0fSsuqTLXVsZf6AotRl6TZBl9gCw9GrfmsNGkSOh5O9Aw7dfzN7q8PmgWa/CJu5zEch/gwxb0uAVyRmEQ5/+TMeHtUmKbz9GZT8fP90e6vBGfkVTtpVIAgG1rzXhz1oaAkwDXFNFljQ84XrM0gt1rmpaL1/Rjojw2FW00T/9dUAAANol8qiULoZSbDBAUtyB7c+IkoLvN5lUEToqB1ZDNV9ISQ==";
		System.out.println(rsa.privateDecrypt(dataFromJs));
	}

}
