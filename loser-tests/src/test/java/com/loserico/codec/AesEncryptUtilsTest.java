package com.loserico.codec;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AesEncryptUtilsTest {
	
	@Test
	public void testGenerateKey() {
		String key = AesEncryptUtils.key();
		System.out.println(key);
	}

	@Test
	public void testJsEncryptJavaDecrypt() {
		String key = "VkqnmlLM3zFXlyzv";
		String encrypted = "sjKlu/pgQ7gy79KjWqKpb4lMmRp/6FaunjWLG2s2jN1fqwwp+LXAKqHN1XuKycQ7FLQNj5UKuR2qM3yszUWgm2cH1GbfP/PdPLsss2Ce3Ru93GxleLwlu/0208m+TCJEfRePWzkcoPp4gNdCHeTSZ+2D+2MSzAEqcOp/UOV+P5yVwnyplLTlKv+5FUyBbjFG";
		String original = "uri=api/v1/resources&access_token=8B9OkolEY你好IFR807wLTLModwJJSypMMVVW2i3haoiWWBpSOLEoGqSZygtn4WYLyCQz8&timestamp=1534323558718";
		String decrypted = AesEncryptUtils.decrypt(encrypted, key);
		assertEquals(original, decrypted);
		System.out.println(decrypted);
	}
}
