package com.loserico.codec;

import com.loserico.codec.exception.PrivateDecryptException;
import com.loserico.codec.exception.PrivateEncryptException;
import com.loserico.codec.exception.PublicDecryptException;
import com.loserico.codec.exception.PublicEncryptException;
import com.loserico.codec.exception.RsaPrivateKeyException;
import com.loserico.codec.exception.RsaPublicKeyException;
import com.loserico.codec.exception.RsaSignException;
import com.loserico.codec.exception.RsaSignVerifyException;
import com.loserico.common.lang.resource.PropertyReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
@Slf4j
public final class RsaUtils {
	
	private static final PropertyReader propertyReader = new PropertyReader("codec.properties");
	
	public static final String CHARSET = "UTF-8";
	
	/**
	 * 密钥算法
	 */
	public static final String ALGORITHM_RSA = "RSA";
	
	/**
	 * RSA 签名算法
	 */
	public static final String ALGORITHM_RSA_SIGN = "SHA256WithRSA";
	
	/**
	 * 密钥对长度
	 */
	public static final int KEY_LENGTH = 2048;
	
	/**
	 * String to hold the name of the private key file.
	 */
	public static final String PRIVATE_KEY_FILE = System.getProperty("user.home") + "/private.key";
	
	/**
	 * String to hold name of the public key file.
	 */
	public static final String PUBLIC_KEY_FILE = System.getProperty("user.home") + "/public.key";
	
	/**
	 * PEM格式的公钥以一行 -----BEGIN PUBLIC KEY----- 开头
	 * 中间是公钥字符串
	 * 最后是一行         -----END PUBLIC KEY-----
	 */
	public static final String PEM_PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";
	
	public static final String PEM_PUBLIC_KEY_END = "-----END PUBLIC KEY-----";
	
	/**
	 * PEM格式的私钥以一行 -----BEGIN PRIVATE KEY----- 开头
	 * 中间是私钥字符串
	 * 最后是一行         -----END PRIVATE KEY-----
	 */
	public static final String PEM_PRIVATE_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----";
	
	public static final String PEM_PRIVATE_KEY_END = "-----END PRIVATE KEY-----";
	
	/**
	 * 公钥
	 */
	private static RSAPublicKey publicKey = null;
	
	/**
	 * 公钥串
	 */
	private static String publicKeyStr = null;
	
	/**
	 * 私钥
	 */
	private static RSAPrivateKey privateKey = null;
	
	/**
	 * 私钥串
	 */
	private static String privateKeyStr = null;
	
	private RsaUtils() {
	}
	
	
	/**
	 * 初始化RSA密钥对
	 *
	 * @param keysize RSA1024已经不安全了,建议2048
	 * @return 经过Base64编码后的公私钥
	 */
	static {
		/*
		 * 磁盘上不存在密钥对则重新生成
		 */
		if (!keysPresent()) {
			
			try {
				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
				
				//初始化KeyPairGenerator对象
				keyPairGenerator.initialize(KEY_LENGTH);
				//生成密匙对
				KeyPair keyPair = keyPairGenerator.generateKeyPair();
				//得到公钥
				publicKey = (RSAPublicKey) keyPair.getPublic();
				//得到私钥
				privateKey = (RSAPrivateKey) keyPair.getPrivate();
				
				write2Disk(publicKey, privateKey);
				
			} catch (Exception e) {
				throw new RuntimeException("生成密钥对失败", e);
			}
		} else {
			/*
			 * 从磁盘上读取密钥对
			 */
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
				publicKey = (RSAPublicKey) objectInputStream.readObject();
				
				objectInputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
				privateKey = (RSAPrivateKey) objectInputStream.readObject();
				objectInputStream.close();
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException("获取密钥对失败", e);
			}
		}
		
		//得到公钥串
		publicKeyStr = base64Encode(publicKey.getEncoded());
		//得到私钥串
		privateKeyStr = base64Encode(privateKey.getEncoded());
	}
	
	/**
	 * 工具公钥串/私钥串重新初始化, 必须同时提供公钥私钥, 否则报错
	 *
	 * @param publicKeyStr
	 * @param privateKeyStr
	 */
	public synchronized static void reinitialize(String publicKeyStr, String privateKeyStr) {
		if (isBlank(publicKeyStr) || isBlank(privateKeyStr)) {
			throw new IllegalArgumentException("公钥/私钥不能为空");
		}
		
		//先备份旧的, 如果重新初始化失败, 回退旧版
		String publicKeyStrOld = RsaUtils.publicKeyStr;
		String privateKeyStrOld = RsaUtils.privateKeyStr;
		
		RSAPublicKey publicKeyOld = RsaUtils.publicKey;
		RSAPrivateKey privateKeyOld = RsaUtils.privateKey;
		
		try {
			RsaUtils.publicKeyStr = publicKeyStr;
			RsaUtils.privateKeyStr = privateKeyStr;
			
			RsaUtils.publicKey = RsaUtils.publicKey(publicKeyStr);
			RsaUtils.privateKey = RsaUtils.privateKey(privateKeyStr);
			
			write2Disk(publicKey, privateKey);
		} catch (Throwable e) {
			log.info("回退旧版公私钥...");
			RsaUtils.publicKeyStr = publicKeyStrOld;
			RsaUtils.privateKeyStr = privateKeyStrOld;
			RsaUtils.publicKey = publicKeyOld;
			RsaUtils.privateKey = privateKeyOld;
			
			throw new RuntimeException("重新输出化密钥对失败", e);
		}
	}
	
	/**
	 * 公钥加密, 用的公钥是储存在系统磁盘上的
	 * <p>
	 * Copyright: Copyright (c) 2020-03-09 17:48
	 * <p>
	 * Company: Sexy Uncle Inc.
	 * <p>
	 *
	 * @author Rico Yu  ricoyu520@gmail.com
	 * @version 1.0
	 */
	public static String publicEncrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return base64Encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new PublicEncryptException("加密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法公钥加密数据
	 *
	 * @param data         公钥加密后的加密串
	 * @param publicKeyStr Base64编码的RSA公钥字符串
	 * @return 公钥解密后的原始串
	 */
	public static String publicEncrypt(String data, String publicKeyStr) {
		try {
			//通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKeyStr));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PublicKey key = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return base64Encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new PublicEncryptException("加密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法公钥解密数据
	 *
	 * @param data
	 * @return String
	 */
	public static String publicDecrypt(String data) {
		if (isBlank(data)) {
			return data;
		}
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(data)), CHARSET);
		} catch (Exception e) {
			throw new PublicDecryptException("解密字符串[" + data + "]时遇到异常", e);
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
		if (isBlank(data)) {
			return data;
		}
		try {
			//通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PublicKey key = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(data)), CHARSET);
		} catch (Exception e) {
			throw new PublicDecryptException("解密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法私钥加密数据
	 *
	 * @param data          待加密的明文字符串
	 * @param privateKeyStr RSA私钥字符串
	 * @return RSA私钥加密后的经过Base64编码的密文字符串
	 */
	public static String privateEncrypt(String data, String privateKeyStr) {
		try {
			//通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return base64Encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new PrivateEncryptException("加密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法私钥解密数据
	 *
	 * @param data
	 * @return String
	 */
	public static String privateEncrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return base64Encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new PrivateEncryptException("加密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法私钥解密数据
	 *
	 * @param data 待解密的经过Base64编码的密文字符串
	 * @return RSA私钥解密后的明文字符串
	 */
	public static String privateDecrypt(String data) {
		if (isBlank(data)) {
			return data;
		}
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(data)), CHARSET);
		} catch (Exception e) {
			throw new PrivateDecryptException("解密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法私钥解密数据
	 *
	 * @param data          待解密的经过Base64编码的密文字符串
	 * @param privateKeyStr RSA私钥字符串
	 * @return RSA私钥解密后的明文字符串
	 */
	public static String privateDecrypt(String data, String privateKeyStr) {
		if (isBlank(data)) {
			return data;
		}
		try {
			//通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(base64Decode(privateKeyStr));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(data)), CHARSET);
		} catch (Exception e) {
			throw new PrivateDecryptException("解密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	
	/**
	 * RSA算法使用私钥对数据生成数字签名
	 *
	 * @param data 待签名的明文字符串
	 * @return RSA私钥签名后的经过Base64编码的字符串
	 */
	public static String sign(String data) {
		if (data == null) {
			return null;
		}
		return sign(data.getBytes(UTF_8));
	}
	
	/**
	 * RSA算法使用私钥对数据生成数字签名
	 *
	 * @param data 待签名的明文字符串
	 * @return RSA私钥签名后的经过Base64编码的字符串
	 */
	public static String sign(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}
		try {
			//sign
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initSign(privateKey);
			signature.update(data);
			return encodeBase64String(signature.sign());
		} catch (Exception e) {
			throw new RsaSignException("签名字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法使用私钥对数据生成数字签名
	 *
	 * @param data          待签名的明文字符串
	 * @param privateKeyStr RSA私钥字符串
	 * @return RSA私钥签名后的经过Base64编码的字符串
	 */
	public static String sign(byte[] data, String privateKeyStr) {
		if (data == null || data.length == 0) {
			log.info("data is null or data.length == 0");
			return null;
		}
		Objects.requireNonNull(data, "data不能为null");
		try {
			//通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(base64Decode(privateKeyStr));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PrivateKey key = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initSign(key);
			signature.update(data);
			return base64Encode(signature.sign());
		} catch (Exception e) {
			throw new RsaSignException("签名字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法使用私钥对数据生成数字签名
	 *
	 * @param data          待签名的明文字符串
	 * @param privateKeyStr RSA私钥字符串
	 * @return RSA私钥签名后的经过Base64编码的字符串
	 */
	public static String sign(String data, String privateKeyStr) {
		if (data == null) {
			log.info("data is null!");
			return null;
		}
		return sign(data.getBytes(UTF_8), privateKeyStr);
	}
	
	/**
	 * RSA算法使用公钥校验数字签名
	 *
	 * @param data 参与签名的明文字符串
	 * @param sign RSA签名得到的经过Base64编码的字符串
	 * @return true--验签通过,false--验签未通过
	 */
	public boolean verify(String data, String sign) {
		return verify(data.getBytes(UTF_8), sign);
	}
	
	/**
	 * RSA算法使用公钥校验数字签名
	 *
	 * @param data 参与签名的明文字符串
	 * @param sign RSA签名得到的经过Base64编码的字符串
	 * @return true--验签通过,false--验签未通过
	 */
	public boolean verify(byte[] data, String sign) {
		if (data == null || data.length == 0 || sign == null) {
			log.info("data or sign is null");
			return false;
		}
		try {
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initVerify(publicKey);
			signature.update(data);
			return signature.verify(decodeBase64(sign));
		} catch (Exception e) {
			throw new RsaSignVerifyException("验签字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * RSA算法使用公钥校验数字签名
	 *
	 * @param data         参与签名的明文字符串
	 * @param publicKeyStr RSA公钥字符串
	 * @param sign         RSA签名得到的经过Base64编码的字符串
	 * @return true--验签通过,false--验签未通过
	 */
	public static boolean verify(String data, String sign, String publicKeyStr) {
		if (data == null || sign == null) {
			log.info("data or sign is null");
			return false;
		}
		try {
			//通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKeyStr));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PublicKey key = keyFactory.generatePublic(x509KeySpec);
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initVerify(key);
			signature.update(data.getBytes(CHARSET));
			return signature.verify(base64Decode(sign));
		} catch (Exception e) {
			throw new RsaSignVerifyException("验签字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * 根据公钥字符串获取公钥对象
	 * 完整的公钥是类似这样的
	 * <p>
	 * -----BEGIN PUBLIC KEY-----
	 * MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtgaTD/42sQ5WZXKBTPUHkv0kib5UJrkoEaLZa/3lcFolse5rKIu8JEbf6YjJP6UOdOc21+r9vJZSaKNOgm5XqaD41o8Zs2eiGa2QX0y5LLfOHm9RIRakm9WR7drYI43XNnEE0XQL0QgoIh6Z9IruPbqpuXa38+FlBobHl5dc5TZK35u+HkYf67v/a4QP1W7S8y2S4xt8vzFa1GvR5eZGL3jp+Mmk5BYtpLT6e94XNh1IAMjGor7jox7fmI4oJ+xr75kmJSS2RvL9yI5QbklPaWfiWQ/sHDZPzuW83RcST9x6tg8GKGW6mGrcKh5We/L9T4slA1ky2QRI9ATS1haD5QIDAQAB
	 * -----END PUBLIC KEY-----
	 * <p>
	 * publicKey是中间部分的字符串, 不包含头尾的BEGIN/END PUBLIC KEY
	 *
	 * @param publicKeyStr
	 * @return PublicKey
	 */
	public static RSAPublicKey publicKey(String publicKeyStr) {
		//通过X509编码的Key指令获得公钥对象
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKeyStr));
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			log.error("根据公钥串[{}]获取公钥对象失败", publicKey, e);
			throw new RsaPublicKeyException(MessageFormat.format("根据公钥串[{0}]获取公钥对象失败", publicKey), e);
		}
	}
	
	/**
	 * 根据私钥字符串获取私钥对象
	 *
	 * @param privateKeyStr
	 * @return PrivateKey
	 */
	public static RSAPrivateKey privateKey(String privateKeyStr) {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(base64Decode(privateKeyStr));
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RsaPrivateKeyException(MessageFormat.format("根据私钥串[{0}]获取私钥对象失败", privateKeyStr), e);
		}
		
	}
	
	/**
	 * 公钥字符串头尾有两行:
	 * -----BEGIN PUBLIC KEY-----
	 * MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtgaTD/42sQ5WZXKBTPUHkv0kib5UJrkoEaLZa/3l....
	 * -----END PUBLIC KEY-----
	 * <p>
	 * 这个方法的作用是去掉头尾, 取中间部分
	 *
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
	
	private static String base64Encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}
	
	private static byte[] base64Decode(String encoded) {
		return Base64.getDecoder().decode(encoded);
	}
	
	/**
	 * 检查公钥/私钥是否都已生成
	 *
	 * @return boolean
	 */
	private static boolean keysPresent() {
		File privateKey = new File(PRIVATE_KEY_FILE);
		File publicKey = new File(PUBLIC_KEY_FILE);
		
		return privateKey.exists() && publicKey.exists();
	}
	
	private static void write2Disk(PublicKey publicKey, PrivateKey privateKey) throws IOException {
		File privateKeyFile = new File(PRIVATE_KEY_FILE);
		File publicKeyFile = new File(PUBLIC_KEY_FILE);
		
		if (publicKeyFile.getParentFile() != null) {
			publicKeyFile.getParentFile().mkdirs();
		}
		publicKeyFile.createNewFile();
		
		if (privateKeyFile.getParentFile() != null) {
			privateKeyFile.getParentFile().mkdirs();
		}
		privateKeyFile.createNewFile();
		
		ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
		publicKeyOS.writeObject(publicKey);
		publicKeyOS.close();
		
		ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
		privateKeyOS.writeObject(privateKey);
		privateKeyOS.close();
	}
	
	public static String getPublicKeyStr() {
		return publicKeyStr;
	}
	
	public static String getPrivateKeyStr() {
		return privateKeyStr;
	}
}


