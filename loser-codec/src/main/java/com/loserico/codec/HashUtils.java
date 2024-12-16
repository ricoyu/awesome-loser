package com.loserico.codec;

import com.loserico.codec.exception.EncodeException;
import com.loserico.codec.exception.HmacSha256Exception;
import com.loserico.codec.exception.NoSuchHashAlgorithmException;
import com.loserico.common.lang.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * <p>
 * Copyright: (C), 2019/10/20 15:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class HashUtils {
	
	private static final String CHARSET_UTF8 = "UTF-8";
	
	private static final int[] LOOKUP_TABLE = {0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50A5,
			0x60C6, 0x70E7, 0x8108, 0x9129, 0xA14A, 0xB16B, 0xC18C, 0xD1AD, 0xE1CE, 0xF1EF, 0x1231,
			0x0210, 0x3273, 0x2252, 0x52B5, 0x4294, 0x72F7, 0x62D6, 0x9339, 0x8318, 0xB37B, 0xA35A,
			0xD3BD, 0xC39C, 0xF3FF, 0xE3DE, 0x2462, 0x3443, 0x0420, 0x1401, 0x64E6, 0x74C7, 0x44A4,
			0x5485, 0xA56A, 0xB54B, 0x8528, 0x9509, 0xE5EE, 0xF5CF, 0xC5AC, 0xD58D, 0x3653, 0x2672,
			0x1611, 0x0630, 0x76D7, 0x66F6, 0x5695, 0x46B4, 0xB75B, 0xA77A, 0x9719, 0x8738, 0xF7DF,
			0xE7FE, 0xD79D, 0xC7BC, 0x48C4, 0x58E5, 0x6886, 0x78A7, 0x0840, 0x1861, 0x2802, 0x3823,
			0xC9CC, 0xD9ED, 0xE98E, 0xF9AF, 0x8948, 0x9969, 0xA90A, 0xB92B, 0x5AF5, 0x4AD4, 0x7AB7,
			0x6A96, 0x1A71, 0x0A50, 0x3A33, 0x2A12, 0xDBFD, 0xCBDC, 0xFBBF, 0xEB9E, 0x9B79, 0x8B58,
			0xBB3B, 0xAB1A, 0x6CA6, 0x7C87, 0x4CE4, 0x5CC5, 0x2C22, 0x3C03, 0x0C60, 0x1C41, 0xEDAE,
			0xFD8F, 0xCDEC, 0xDDCD, 0xAD2A, 0xBD0B, 0x8D68, 0x9D49, 0x7E97, 0x6EB6, 0x5ED5, 0x4EF4,
			0x3E13, 0x2E32, 0x1E51, 0x0E70, 0xFF9F, 0xEFBE, 0xDFDD, 0xCFFC, 0xBF1B, 0xAF3A, 0x9F59,
			0x8F78, 0x9188, 0x81A9, 0xB1CA, 0xA1EB, 0xD10C, 0xC12D, 0xF14E, 0xE16F, 0x1080, 0x00A1,
			0x30C2, 0x20E3, 0x5004, 0x4025, 0x7046, 0x6067, 0x83B9, 0x9398, 0xA3FB, 0xB3DA, 0xC33D,
			0xD31C, 0xE37F, 0xF35E, 0x02B1, 0x1290, 0x22F3, 0x32D2, 0x4235, 0x5214, 0x6277, 0x7256,
			0xB5EA, 0xA5CB, 0x95A8, 0x8589, 0xF56E, 0xE54F, 0xD52C, 0xC50D, 0x34E2, 0x24C3, 0x14A0,
			0x0481, 0x7466, 0x6447, 0x5424, 0x4405, 0xA7DB, 0xB7FA, 0x8799, 0x97B8, 0xE75F, 0xF77E,
			0xC71D, 0xD73C, 0x26D3, 0x36F2, 0x0691, 0x16B0, 0x6657, 0x7676, 0x4615, 0x5634, 0xD94C,
			0xC96D, 0xF90E, 0xE92F, 0x99C8, 0x89E9, 0xB98A, 0xA9AB, 0x5844, 0x4865, 0x7806, 0x6827,
			0x18C0, 0x08E1, 0x3882, 0x28A3, 0xCB7D, 0xDB5C, 0xEB3F, 0xFB1E, 0x8BF9, 0x9BD8, 0xABBB,
			0xBB9A, 0x4A75, 0x5A54, 0x6A37, 0x7A16, 0x0AF1, 0x1AD0, 0x2AB3, 0x3A92, 0xFD2E, 0xED0F,
			0xDD6C, 0xCD4D, 0xBDAA, 0xAD8B, 0x9DE8, 0x8DC9, 0x7C26, 0x6C07, 0x5C64, 0x4C45, 0x3CA2,
			0x2C83, 0x1CE0, 0x0CC1, 0xEF1F, 0xFF3E, 0xCF5D, 0xDF7C, 0xAF9B, 0xBFBA, 0x8FD9, 0x9FF8,
			0x6E17, 0x7E36, 0x4E55, 0x5E74, 0x2E93, 0x3EB2, 0x0ED1, 0x1EF0,};
	
	private HashUtils() {
		throw new InstantiationError("Must not instantiate this class");
	}
	
	/**
	 * Create a CRC16 checksum from the bytes. implementation is from mp911de/lettuce, modified with
	 * some more optimizations
	 *
	 * @param key
	 * @return CRC16 as integer value
	 */
	public static int crc16Hash(String key) {
		byte[] bytes = encode(key);
		
		int crc = 0x0000;
		int len = bytes.length;
		for (int i = 0; i < len; i++) {
			crc = ((crc << 8) ^ LOOKUP_TABLE[((crc >>> 8) ^ (bytes[i] & 0xFF)) & 0xFF]);
		}
		return crc & 0xFFFF;
	}
	
	
	/**
	 * 使用FNV1_32_HASH算法计算服务器的Hash值
	 * https://blog.csdn.net/u010558660/article/details/52767218
	 */
	public static int fnvHash(String str) {
		final int p = 16777619;
		int hash = (int) 2166136261L;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash ^ str.charAt(i)) * p;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		
		// 如果算出来的值为负数则取其绝对值
		if (hash < 0) {
			hash = Math.abs(hash);
		}
		return hash;
	}

	/**
	 * SHA256是一种哈希算法, 产生一个256位（32字节）的哈希值，用于数据完整性验证和消息摘要生成。
	 * <ul>哈希算法的特点是：
	 *     <li/>单向性：一旦数据被哈希，无法从哈希值反推回原始数据。
	 *     <li/>固定输出长度：无论输入数据的大小如何，输出的哈希值长度都是固定的。
	 *     <li/>碰撞抵抗性：不同的输入数据产生相同哈希值的可能性非常低。
	 * </ul>
	 * @param source
	 * @return String
	 */
	public static String sha256(String source) {
		byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
		return sha256(bytes);
	}

	/**
	 * SHA256是一种哈希算法, 产生一个256位（32字节）的哈希值，用于数据完整性验证和消息摘要生成。
	 * <ul>哈希算法的特点是：
	 *     <li/>单向性：一旦数据被哈希，无法从哈希值反推回原始数据。
	 *     <li/>固定输出长度：无论输入数据的大小如何，输出的哈希值长度都是固定的。
	 *     <li/>碰撞抵抗性：不同的输入数据产生相同哈希值的可能性非常低。
	 * </ul>
	 * @param bytes
	 * @return String
	 */
	public static String sha256(byte[] bytes) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			log.error("不支持该Hash算法[{}]", "SHA-256");
			throw new NoSuchHashAlgorithmException(e);
		}
		byte[] encodedhash = digest.digest(bytes);
		
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < encodedhash.length; i++) {
			String hex = Integer.toHexString(0xff & encodedhash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
	/**
	 * 用于JWT签名
	 *
	 * @param message
	 * @param secret
	 * @return String
	 */
	public static String hmacSha256(String message, String secret) {
		Assert.notNull(message, "message不能为null");
		Assert.notNull(secret, "secret不能为null");
		
		Mac hmacSHA256 = null;
		try {
			hmacSHA256 = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			hmacSHA256.init(secret_key);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			log.error("", e);
			throw new HmacSha256Exception(e);
		}
		
		return Base64.encodeBase64String(hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8)));
	}
	
	
	/**
	 * 使用UTF-8编码进行MD5哈希
	 * MD5 is a widely used cryptographic hash function, which produces a hash of 128 bit.
	 *
	 * @param bytes
	 * @return String
	 */
	public static String md5Hex(byte[] bytes) {
		return DigestUtils.md5Hex(bytes);
	}
	
	/**
	 * 使用UTF-8编码进行MD5哈希
	 * MD5 is a widely used cryptographic hash function, which produces a hash of 128 bit.
	 *
	 * @param source
	 * @return String
	 */
	public static String md5Hex(String source) {
		return DigestUtils.md5Hex(source);
	}
	
	/**
	 * 使用UTF-8编码进行MD5哈希
	 * MD5 is a widely used cryptographic hash function, which produces a hash of 128 bit.
	 *
	 * @param file
	 * @return String
	 */
	public static String md5Hex(File file) {
		Objects.requireNonNull(file, "file cannot be null!");
		if (!file.exists()) {
			return null;
		}
		byte[] bytes = IOUtils.readFileAsBytes(file);
		return DigestUtils.md5Hex(bytes);
	}
	
	/**
	 * Generates 32 bit murmur2 hash from byte array
	 *
	 * @param data byte array to hash
	 * @return 32 bit hash of the given array
	 */
	public static int hashcode(final byte[] data) {
		int length = data.length;
		int seed = 0x9747b28c;
		// 'm' and 'r' are mixing constants generated offline.
		// They're not really 'magic', they just happen to work well.
		final int m = 0x5bd1e995;
		final int r = 24;
		
		// Initialize the hash to a random value
		int h = seed ^ length;
		int length4 = length / 4;
		
		for (int i = 0; i < length4; i++) {
			final int i4 = i * 4;
			int k = (data[i4 + 0] & 0xff) + ((data[i4 + 1] & 0xff) << 8) + ((data[i4 + 2] & 0xff) << 16) + ((data[i4 + 3] & 0xff) << 24);
			k *= m;
			k ^= k >>> r;
			k *= m;
			h *= m;
			h ^= k;
		}
		
		// Handle the last few bytes of the input array
		switch (length % 4) {
			case 3:
				h ^= (data[(length & ~3) + 2] & 0xff) << 16;
				break;
			case 2:
				h ^= (data[(length & ~3) + 1] & 0xff) << 8;
				break;
			case 1:
				h ^= data[length & ~3] & 0xff;
				h *= m;
				break;
			default:
		}
		
		h ^= h >>> 13;
		h *= m;
		h ^= h >>> 15;
		
		return h;
	}
	
	
	/**
	 * A cheap way to deterministically convert a number to a positive value. When the input is
	 * positive, the original value is returned. When the input number is negative, the returned
	 * positive value is the original value bit AND against 0x7fffffff which is not its absolutely
	 * value.
	 * <p>
	 * 原来是正数, 那么原样返回
	 * 原来是负数, 那么转成正数, 因为符号位会变成0, 其他低位都和1位与, 所以原来是什么还是什么
	 *
	 * @param number a given number
	 * @return a positive number.
	 */
	public static int toPositive(int number) {
		//0x7fffffff的二进制表示: 01111111 11111111 11111111 11111111
		return number & 0x7fffffff;
	}
	
	private static byte[] encode(final String str) {
		try {
			if (str == null) {
				throw new IllegalArgumentException("value sent to redis cannot be null");
			}
			return str.getBytes(CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new EncodeException(e);
		}
	}
	
}
