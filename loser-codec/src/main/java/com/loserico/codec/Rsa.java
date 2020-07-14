package com.loserico.codec;

import com.loserico.codec.exception.PrivateDecryptException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * RSA 非对称加密 这个版本生成一次密钥对后会存在磁盘上, 不会每次都生成
 * <p>
 * RSA 加密演算法是一种非对称加密演算法. 在公开密钥加密和电子商业中RSA被广泛使用.
 * RSA是1977年由罗纳德·李维斯特(Ron Rivest), 阿迪·萨莫尔(Adi Shamir) 和伦纳德·阿德曼(Leonard Adleman)一起提出的
 * 当时他们三人都在麻省理工学院工作, RSA就是他们三人姓氏开头字母拼在一起组成的
 *
 * <p>
 * Copyright: Copyright (c) 2018-07-30 13:29
 * <p>
 * Company: DataSense
 * <p>
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class Rsa {
	
	public static final String CHARSET = "UTF-8";
	
	/**
	 * 加密算法
	 */
	public static final String ALGORITHM_RSA = "RSA";
	
	/**
	 * RSA 签名算法
	 */
	public static final String ALGORITHM_RSA_SIGN = "SHA256WithRSA";
	
	/**
	 * 密钥长度必须是64的倍数，在512到65536位之间
	 */
	private static final int KEY_LENGTH = 2048;
	
	/**
	 * String to hold the name of the private key file.
	 */
	public static final String PRIVATE_KEY_FILE = System.getProperty("user.home") + "/private.key";
	
	/**
	 * String to hold name of the public key file.
	 */
	public static final String PUBLIC_KEY_FILE = System.getProperty("user.home") + "/public.key";
	
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;
	
	@SuppressWarnings("resource")
	public static Rsa instance() {
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
				RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
				//得到私钥
				RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
				
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
				privateKeyOS.writeObject(keyPair.getPrivate());
				privateKeyOS.close();
				
				return new Rsa(publicKey, privateKey);
			} catch (Exception e) {
				throw new RuntimeException("生成密钥对失败", e);
			}
		} else {
			/*
			 * 从磁盘上读取密钥对
			 */
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
				RSAPublicKey publicKey = (RSAPublicKey) objectInputStream.readObject();
				
				objectInputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
				RSAPrivateKey privateKey = (RSAPrivateKey) objectInputStream.readObject();
				objectInputStream.close();
				
				return new Rsa(publicKey, privateKey);
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException("获取密钥对失败", e);
			}
		}
	}
	
	public Rsa(String publicKey, String privateKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			
			//通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
			this.publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
			//通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		} catch (Exception e) {
			throw new RuntimeException("不支持的密钥", e);
		}
	}
	
	public Rsa(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	/**
	 * 公钥加密, 用的公钥是储存在系统磁盘上的
	 * <p>
	 * Copyright: Copyright (c) 2020-03-09 17:48
	 * <p>
	 * Company: Sexy Uncle Inc.
	 * <p>
	 
	 * @author Rico Yu  ricoyu520@gmail.com
	 * @version 1.0
	 */
	public String publicEncrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return base64Encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	/**
	 * @param data 公钥加密后的加密串
	 * @param publicKey  Base64编码的RSA公钥字符串
	 * @return 公钥解密后的原始串
	 */
	public String publicEncrypt(String data, String publicKey) {
		try {
			//通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(base64Decode(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PublicKey key = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return base64Encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	public String privateEncrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return base64Encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
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
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(java.util.Base64.getDecoder().decode(key));
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
	 * RSA算法使用私钥对数据生成数字签名
	 *
	 * @param data       待签名的明文字符串
	 * @return RSA私钥签名后的经过Base64编码的字符串
	 */
	public String sign(String data) {
		try {
			//sign
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initSign(privateKey);
			signature.update(data.getBytes(CHARSET));
			return Base64.encodeBase64String(signature.sign());
		} catch (Exception e) {
			throw new RuntimeException("签名字符串[" + data + "]时遇到异常", e);
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
	 * @param sign      RSA签名得到的经过Base64编码的字符串
	 * @return true--验签通过,false--验签未通过
	 */
	public boolean verify(String data, String sign) {
		try {
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initVerify(publicKey);
			signature.update(data.getBytes(CHARSET));
			return signature.verify(Base64.decodeBase64(sign));
		} catch (Exception e) {
			throw new RuntimeException("验签字符串[" + data + "]时遇到异常", e);
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
	
	public String publicDecrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return new String(rsaSplitCodec(cipher,Cipher.DECRYPT_MODE, base64Decode(data)), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
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
	
	public String privateDecrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String( rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(data)), CHARSET);
		} catch (Exception e) {
			throw new PrivateDecryptException("解密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	public String privateDecrypt(String data, String privateKey) {
		try {
			// 取得私钥
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(base64Decode(privateKey));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			Key key = keyFactory.generatePrivate(pkcs8KeySpec);
			// 对数据解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, base64Decode(data)), CHARSET);
		} catch (Exception e) {
			throw new PrivateDecryptException("解密字符串[" + data + "]时遇到异常", e);
		}
	}
	
	@SuppressWarnings("deprecation")
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
	
	public String publicKey() {
		byte[] bytes = publicKey.getEncoded();
		return Base64.encodeBase64String(bytes);
		//return Base64.encodeBase64String(bytes);
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
	
	private static String base64Encode(byte[] data) {
		return java.util.Base64.getEncoder().encodeToString(data);
	}
	
	private static byte[] base64Decode(String encoded) {
		return java.util.Base64.getDecoder().decode(encoded);
	}
}