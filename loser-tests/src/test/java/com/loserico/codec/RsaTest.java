package com.loserico.codec;

import com.loserico.common.lang.utils.IOUtils;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.assertj.core.api.Assertions.*;

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
		String dataFromJs =
				"P2KoWGC4/v9iZJI1oaBna7M9Kd9PeC6hD0wMdAc2H1WnBVWPz7bsaU+2PmCpoKOfWhYqR5FORaw3ZAJGzBi1BoL0XDOBLI+2kbLZgzDKWDgoy37iYXYi9hkNUiqF0fSsuqTLXVsZf6AotRl6TZBl9gCw9GrfmsNGkSOh5O9Aw7dfzN7q8PmgWa/CJu5zEch/gwxb0uAVyRmEQ5/+TMeHtUmKbz9GZT8fP90e6vBGfkVTtpVIAgG1rzXhz1oaAkwDXFNFljQ84XrM0gt1rmpaL1/Rjojw2FW00T/9dUAAANol8qiULoZSbDBAUtyB7c+IkoLvN5lUEToqB1ZDNV9ISQ==";
		System.out.println(rsa.privateDecrypt(dataFromJs));
	}
	
	@Test
	public void testReadPemPublicKey() {
		String publicKey = IOUtils.readClassPathFileAsString("rsa_public_key.pem");
		System.out.println(publicKey);
	}
	
	@SneakyThrows
	@Test
	public void testGetPublicKeyFromPem() {
		String publicKeyStr = IOUtils.readClassPathFileAsString("rsa_public_key.pem");
		publicKeyStr = publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "")
				.replaceAll(System.lineSeparator(), "")
				.replace("-----END PUBLIC KEY-----", "");
		
		byte[] encoded = Base64.decodeBase64(publicKeyStr);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		assertThat(publicKey).isNotNull();
	}
	
	@SneakyThrows
	@Test
	public void testReadPemPrivateKey() {
		String privateKeyStr = IOUtils.readClassPathFileAsString("rsa_key.pem");
		privateKeyStr = privateKeyStr.replace(RsaUtils.PEM_PRIVATE_KEY_BEGIN, "")
				.replaceAll(System.lineSeparator(), "")
				.replace(RsaUtils.PEM_PRIVATE_KEY_END, "");
		
		byte[] bytes = Base64.decodeBase64(privateKeyStr);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
		RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		assertThat(privateKey).isNotNull();
	}
	
	@SneakyThrows
	@Test
	public void testGeneratePemPublic() {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		StringBuilder publicKeyBuilder = new StringBuilder();
		String publicKey = publicKeyBuilder.append(RsaUtils.PEM_PUBLIC_KEY_BEGIN)
				.append(System.lineSeparator())
				.append(Base64.encodeBase64String(keyPair.getPublic().getEncoded()))
				.append(System.lineSeparator())
				.append(RsaUtils.PEM_PUBLIC_KEY_END)
				.toString();
		
		StringBuilder privateKeyBuilder = new StringBuilder();
		String privateKey = privateKeyBuilder.append(RsaUtils.PEM_PRIVATE_KEY_BEGIN)
				.append(System.lineSeparator())
				.append(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()))
				.append(System.lineSeparator())
				.append(RsaUtils.PEM_PRIVATE_KEY_END)
				.toString();
		
		System.out.println(publicKey);
		System.out.println();
		System.out.println(privateKey);
	}
}
