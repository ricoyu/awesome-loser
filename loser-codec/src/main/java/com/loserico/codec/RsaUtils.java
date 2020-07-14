package com.loserico.codec;

import com.loserico.codec.exception.RsaPublicKeyException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Objects;

/**
 * RSA 非对称加密/加密工具
 * <p>
 * Copyright: (C), 2020/3/4 15:26
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class RsaUtils {
	
	private static Logger log = LoggerFactory.getLogger(RsaUtils.class);
	
	public static final String CHARSET = "UTF-8";
	
	//密钥算法
	public static final String ALGORITHM_RSA = "RSA";
	
	//RSA 签名算法
	public static final String ALGORITHM_RSA_SIGN = "SHA256WithRSA";
	
	public static final int KEY_LENGTH = 2048;
	
	private RsaUtils() {
	}
	
	/**
	 * 初始化RSA密钥对, 通过publicKey, privateKey从Map中获取公钥和私钥
	 *
	 * @param keysize RSA1024已经不安全了,建议2048
	 * @return 经过Base64编码后的公私钥
	 */
	public static RsaKeyPair initRSAKey(int keysize) {
		if (keysize < KEY_LENGTH) {
			throw new IllegalArgumentException("RSA1024已经不安全了,请使用" + KEY_LENGTH + "初始化RSA密钥对");
		}
		//为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(ALGORITHM_RSA);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm-->[" + ALGORITHM_RSA + "]");
		}
		//初始化KeyPairGenerator对象
		kpg.initialize(KEY_LENGTH);
		//生成密匙对
		KeyPair keyPair = kpg.generateKeyPair();
		//得到公钥
		PublicKey publicKey = keyPair.getPublic();
		String publicKeyStr = base64Encode(publicKey.getEncoded());
		//得到私钥
		PrivateKey privateKey = keyPair.getPrivate();
		String privateKeyStr = base64Encode(privateKey.getEncoded());
		
		return new RsaKeyPair(publicKeyStr, privateKeyStr);
	}
	
	/**
	 * RSA算法公钥加密数据
	 *
	 * @param data      待加密的明文字符串
	 * @param publicKey RSA公钥字符串
	 * @return RSA公钥加密后的经过Base64编码的密文字符串
	 */
	public static String publicEncrypt(String data, String publicKey) {
		try {
			//通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PublicKey key = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return base64Encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法公钥解密数据
	 *
	 * @param data      待解密的经过Base64编码的密文字符串
	 * @param publicKey RSA公钥字符串
	 * @return RSA公钥解密后的明文字符串
	 */
	public static String publicDecrypt(String data, String publicKey) {
		try {
			//通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PublicKey key = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(data)), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法私钥加密数据
	 *
	 * @param data 待加密的明文字符串
	 * @param key  RSA私钥字符串
	 * @return RSA私钥加密后的经过Base64编码的密文字符串
	 */
	public static String privateEncrypt(String data, String key) {
		try {
			//通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return base64Encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法私钥解密数据
	 *
	 * @param data 待解密的经过Base64编码的密文字符串
	 * @param key  RSA私钥字符串
	 * @return RSA私钥解密后的明文字符串
	 */
	public static String privateDecrypt(String data, String key) {
		try {
			//通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(base64Decode(key));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(data)), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法使用私钥对数据生成数字签名
	 *
	 * @param data       待签名的明文字符串
	 * @param privateKey RSA私钥字符串
	 * @return RSA私钥签名后的经过Base64编码的字符串
	 */
	public static String sign(String data, String privateKey) {
		Objects.requireNonNull(data, "data不能为null");
		try {
			//通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(base64Decode(privateKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PrivateKey key = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initSign(key);
			signature.update(data.getBytes(CHARSET));
			return base64Encode(signature.sign());
		} catch (Exception e) {
			throw new RuntimeException("签名字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法使用公钥校验数字签名
	 *
	 * @param data      参与签名的明文字符串
	 * @param publicKey RSA公钥字符串
	 * @param sign      RSA签名得到的经过Base64编码的字符串
	 * @return true--验签通过,false--验签未通过
	 */
	public static boolean verify(String data, String publicKey, String sign) {
		try {
			//通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PublicKey key = keyFactory.generatePublic(x509KeySpec);
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initVerify(key);
			signature.update(data.getBytes(CHARSET));
			return signature.verify(base64Decode(sign));
		} catch (Exception e) {
			throw new RuntimeException("验签字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * 根据公钥字符串获取公钥对象
	 * 完整的公钥是类似这样的
	 * 
	 * -----BEGIN PUBLIC KEY-----
	 * MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtgaTD/42sQ5WZXKBTPUHkv0kib5UJrkoEaLZa/3lcFolse5rKIu8JEbf6YjJP6UOdOc21+r9vJZSaKNOgm5XqaD41o8Zs2eiGa2QX0y5LLfOHm9RIRakm9WR7drYI43XNnEE0XQL0QgoIh6Z9IruPbqpuXa38+FlBobHl5dc5TZK35u+HkYf67v/a4QP1W7S8y2S4xt8vzFa1GvR5eZGL3jp+Mmk5BYtpLT6e94XNh1IAMjGor7jox7fmI4oJ+xr75kmJSS2RvL9yI5QbklPaWfiWQ/sHDZPzuW83RcST9x6tg8GKGW6mGrcKh5We/L9T4slA1ky2QRI9ATS1haD5QIDAQAB
	 * -----END PUBLIC KEY-----
	 * 
	 * publicKey是中间部分的字符串, 不包含头尾的BEGIN/END PUBLIC KEY
	 * @param publicKey
	 * @return PublicKey
	 */
	public static PublicKey publicKey(String publicKey) {
		//通过X509编码的Key指令获得公钥对象
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKey));
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			return keyFactory.generatePublic(x509KeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			log.error("根据公钥串[{}]获取公钥对象失败", publicKey, e);
			throw new RsaPublicKeyException(MessageFormat.format("根据公钥串[{0}]获取公钥对象失败", publicKey), e);
		}
	}
	
	/**
	 * 公钥字符串头尾有两行:
	 * -----BEGIN PUBLIC KEY-----
	 * MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtgaTD/42sQ5WZXKBTPUHkv0kib5UJrkoEaLZa/3l....
	 * -----END PUBLIC KEY-----
	 * 
	 * 这个方法的作用是去掉头尾, 取中间部分
	 * @param publicKey
	 * @return String
	 */
	public static String extractPublicKey(String publicKey) {
		if (publicKey == null) {
			return null;
		}
		return publicKey.replaceAll("\\-*BEGIN PUBLIC KEY\\-*", "")
				.replaceAll("\\-*END PUBLIC KEY\\-*", "")
				.trim();
	}
	
	/**
	 * RSA算法分段加解密数据
	 *
	 * @param cipher 初始化了加解密工作模式后的javax.crypto.Cipher对象
	 * @param opmode 加解密模式,值为javax.crypto.Cipher.ENCRYPT_MODE/DECRYPT_MODE
	 * @return 加密或解密后得到的数据的字节数组
	 */
	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas) {
		int maxBlock = 0;
		if (opmode == Cipher.DECRYPT_MODE) {
			maxBlock = KEY_LENGTH / 8;
		} else {
			maxBlock = KEY_LENGTH / 8 - 11;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] buff;
		int i = 0;
		try {
			while (datas.length > offSet) {
				if (datas.length - offSet > maxBlock) {
					buff = cipher.doFinal(datas, offSet, maxBlock);
				} else {
					buff = cipher.doFinal(datas, offSet, datas.length - offSet);
				}
				out.write(buff, 0, buff.length);
				i++;
				offSet = i * maxBlock;
			}
		} catch (Exception e) {
			throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
		}
		byte[] resultDatas = out.toByteArray();
		IOUtils.closeQuietly(out);
		return resultDatas;
	}
	
	/**
	 * 存储生成的RSA公钥/私钥对
	 * <p>
	 * Copyright: Copyright (c) 2020-03-04 16:29
	 * <p>
	 * Company: Sexy Uncle Inc.
	 * <p>
	 *
	 * @author Rico Yu  ricoyu520@gmail.com
	 * @version 1.0
	 */
	public static class RsaKeyPair {
		private final String publicKey;
		private final String privateKey;
		
		public RsaKeyPair(String publicKey, String privateKey) {
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}
		
		public String publicKey() {
			return publicKey;
		}
		
		public String privateKey() {
			return privateKey;
		}
		
	}
	
	private static String base64Encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}
	
	private static byte[] base64Decode(String encoded) {
		return Base64.getDecoder().decode(encoded);
	}
	
	public static void main(String[] args) {
		/*long begin = System.currentTimeMillis();
		RsaKeyPair rsaKeyPair = initRSAKey(2048);
		System.out.println("公钥:" + rsaKeyPair.publicKey());
		System.out.println("私钥:" + rsaKeyPair.privateKey);
		String sign = sign("ABCabc123测试", rsaKeyPair.privateKey());
		System.out.println("签名:" + sign);
		System.out.println("校验:" + verify("ABCabc123测试", rsaKeyPair.publicKey(), sign));
		
		String encrypted = publicEncrypt("三少爷是很牛逼的", rsaKeyPair.publicKey());
		System.out.println("密文:" + encrypted);
		String decrypted = privateDecrypt(encrypted, rsaKeyPair.privateKey());
		System.out.println("明文:" + decrypted);
		
		encrypted = privateEncrypt("仔仔是个讨厌鬼", rsaKeyPair.privateKey());
		System.out.println("密文:" + encrypted);
		decrypted = publicDecrypt(encrypted, rsaKeyPair.publicKey());
		System.out.println("明文:" + decrypted);
		long end = System.currentTimeMillis();
		System.out.println("花费时间:" + (end - begin)+"毫秒");*/
		
		String publicKey =
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA49/8JF52dNBDAeiCZHvDjK/rSHj4Ac2Le7OikFMUwtDgN22eClYGvXhbp/segPhKJ8JWKrWc9d1lX7X/RyrlVobAwY8rcPqA0fI55qzn6Y8H4/zrPiOxwrwHfVG6K46v7NVcxreHnpKoHlE2rRBwcMmoPJFk0An6q/Tl6sZLMdcWWDFKRLMOnsfasOmBn22Or1mJZRHcT/yIDy5n6KSXc8eFMfXskO7NL9eJYdLEfv5TGP1lmGkP50s2YfC6c9KgVlb8Zay1TaX6Z+H8L9U8vhirCFFgP+J2E/BzQRq9AtLvIRE+k2MCFjkc5ZOONwseQSdKj0bPX9OTKb9lanDw5QIDAQAB";
		String encrypted = publicEncrypt("太极者, 无极而生. 动静之机, 阴阳之母也.", publicKey);
		System.out.println(encrypted);
	}
}


