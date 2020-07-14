package com.loserico.codec;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class MyDES {
	
	//算法名称
	private static final String KEY_ALGORITHM = "DES";
	//算法名称/加密模式/填充方式
	//DES共有四种工作模式，ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
	private static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
	//明文
	private static final String str = "i am guo feng";

	public static void main(String[] args) {

		try {
			//获得DES生成者实例
			KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
			//指定长度默认值,密钥长度必须的8的倍数
			keyGenerator.init(56);
			//生成一个密钥
			SecretKey Key = keyGenerator.generateKey();
			//得到加密私钥的byte数组
			byte[] byteKey = Key.getEncoded();

			//key转换
			DESKeySpec desKeySpec = new DESKeySpec(byteKey);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
			SecretKey secretKey = factory.generateSecret(desKeySpec);

			//加密
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] result = cipher.doFinal(str.getBytes());
			System.out.println(new BigInteger(1, result).toString(16));

			//解密
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			result = cipher.doFinal(result);
			System.out.println(new String(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}