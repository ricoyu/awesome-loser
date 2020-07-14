package com.loserico.codec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;

public class MyAES {
	
	private static final String KEY_ALGORITHM = "AES";
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String str = "uri=api/v1/resources&access_token=8B9OkolEYIFR807wLTLModwJJSypMMVVW2i3haoiWWBpSOLEoGqSZygtn4WYLyCQz8&timestamp=1534323558718";

	public static void main(String[] args) {
		try {
			//生成密钥
			KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
			keyGenerator.init(new SecureRandom());
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] keyBytes = secretKey.getEncoded();

			//转换密钥
			Key key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);

			//加密
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(str.getBytes());
			System.out.println(new BigInteger(1, result).toString(16));

			//解密
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(result);
			System.out.println(new String(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}