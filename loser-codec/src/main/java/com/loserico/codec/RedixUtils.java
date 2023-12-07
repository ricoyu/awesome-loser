package com.loserico.codec;

import java.math.BigInteger;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 各种进制之间转换工具
 * <p>
 * Copyright: Copyright (c) 2019-05-12 17:13
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RedixUtils {
	
	private static final char[] HEX_CODE = "0123456789abcdef".toCharArray();
	private static final String HEX_STR = "0123456789ABCDEF";
	private static final String HEX_PREFIX = "0x";
	
	/**
	 * byte[]转16进制字符串
	 *
	 * @param data
	 * @return String
	 */
	public static String bytes2Hex(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);
		for (byte b : data) {
			r.append(HEX_CODE[(b >> 4) & 0xF]);
			r.append(HEX_CODE[(b & 0xF)]);
		}
		return r.toString();
	}
	
	/**
	 * 16进制字符串转十进制long
	 *
	 * @param s
	 * @return long
	 */
	public static long hex2Decimal(String s) {
		s = preCheck(s);
		
		long value = 0L;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = HEX_STR.indexOf(c);
			value = 16 * value + d;
		}
		
		return value;
	}
	
	/**
	 * int 转 byte[]
	 *
	 * @param value
	 * @return byte[]
	 */
	public static byte[] int2Bytes(int value) {
		return new byte[]{
				/*
				 * Java 里int占8个字节, 无符号右移24位, 最高8位就移动到低8位上, 前面高位都补0, 
				 * 强转byte会把前面高位的0都去掉, 只保留低8位, 这样就得到了最高8位的值 
				 */
				(byte) (value >>> 24),
				//强转byte就是只保留最低8位, 最高24位都被抹掉了
				(byte) (value >>> 16),
				(byte) (value >>> 8),
				//最低8位不需要移动, 直接强转就行
				(byte) value};
	}
	
	/**
	 * long 转 byte[]
	 *
	 * @param value
	 * @return
	 */
	public static byte[] long2Bytes(long value) {
		return new byte[]{
				(byte) (value >>> 56),
				(byte) (value >>> 48),
				(byte) (value >>> 40),
				(byte) (value >>> 32),
				(byte) (value >>> 24),
				(byte) (value >>> 16),
				(byte) (value >>> 8),
				(byte) value};
	}
	
	public static byte[] float2Bytes(float value) {
		// 将float转换为其等价的int表示
		int intBits = Float.floatToIntBits(value);
		byte[] result = new byte[4];
		
		// 从int中提取每个字节
		result[0] = (byte) (intBits & 0xFF);
		result[1] = (byte) ((intBits >> 8) & 0xFF);
		result[2] = (byte) ((intBits >> 16) & 0xFF);
		result[3] = (byte) ((intBits >> 24) & 0xFF);
		
		return result;
	}
	
	/**
	 * 将16进制字符串转换为byte数组
	 *
	 * @param hex 16进制字符串
	 * @return 对应的byte数组
	 */
	public static byte[] hex2Bytes(String hex) {
		hex = preCheck(hex);
		if (hex == null || hex.length() == 0) {
			return new byte[0];
		}
		
		// 处理奇数长度的字符串, 左侧补零
		if (hex.length() % 2 != 0) {
			hex = "0" + hex;
		}
		
		int len = hex.length();
		/*
		 * 一个byte是8位, 两个16进制字符是8位, 所以一个byte可以表示两个16进制字符
		 */
		byte[] data = new byte[len / 2];
		/*
		 * 在这个循环中, i 是用于遍历16进制字符串的索引。由于每两个16进制字符对应于一个字节, 所以每次迭代中i都会增加2。
		 */
		for (int i = 0; i < len; i += 2) {
			/*
			 * hexString.charAt(i) 和 hexString.charAt(i + 1) 这两个方法调用分别获取16进制字符串中的当前字符和下一个字符。
			 * Character.digit(hexString.charAt(i), 16) 将字符 char 转换为它在16进制中的数值。例如，如果字符是 'A' 或 'a'，它会被转换成10。
			 * << 4 这部分是将上面得到的数值左移4位。
			 * 在二进制中, 左移4位等同于乘以16。这是因为每个16进制数占4位二进制。例如, 如果hexString.charAt(i)是'A'，它的数值是10，左移4位后变成160（即二进制的10100000）
			 * 最后, (Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16)
			 * 这部分代码将两个16进制数值合并为一个字节。第一个数值（经过左移位操作）占据了字节的高4位，而第二个数值占据了低4位。
			 *
			 * 字节数组 data 的每个位置存储一个字节。由于字符串中的每两个字符转换成一个字节，字符串中的第0和1个字符对应于 data[0]，第2和3个字符对应于 data[1]，依此类推。
			 * 因此，i / 2 被用来从字符串索引 i 计算出字节数组 data 的相应索引。例如，当 i 为0或1时，i / 2 等于0; 当 i 为2或3时，i / 2 等于1，以此类推。
			 *
			 * 这样, 循环每执行一次, 就会将16进制字符串中的两个字符转换为一个字节, 并将其存储在字节数组 data 的下一个位置。通过整除i / 2, 我们能够确保每两个字符正确地映射到字节数组的单个索引位置上。
			 */
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
					+ Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}
	
	/**
	 * 二进制字符串转int
	 *
	 * @param binaryStr
	 * @return int
	 */
	public static int binaryStr2Int(String binaryStr) {
		return Integer.parseInt(binaryStr, 2);
	}
	
	/**
	 * 十六进制转二进制字符串
	 *
	 * @param hex
	 * @return String
	 */
	public static String hex2BinaryStr(String hex) {
		hex = preCheck(hex);
		return new BigInteger(hex, 16).toString(2);
	}
	
	/**
	 * int型转二进制字符串
	 *
	 * @param num
	 * @return String
	 */
	public static String int2BinaryStr(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 31; i >= 0; i--) {
			sb.append((num & (1 << i)) == 0 ? "0" : "1");
		}
		return sb.toString();
	}
	
	/**
	 * long转二进制字符串
	 *
	 * @param l
	 * @return String
	 */
	public static String long2BinaryStr(long l) {
		return Long.toBinaryString(l);
	}
	
	/**
	 * char转二进制字符串
	 *
	 * @param c
	 * @return String
	 */
	public static String char2BinaryStr(char c) {
		StringBuilder sb = new StringBuilder();
		for (int i = 13; i >= 0; i--) {
			// 使用位移和按位与操作来检查每一位
			sb.append((c >> i) & 1);
		}
		return sb.toString();
	}
	
	/**
	 * char转byte[]
	 * @param value
	 * @return byte[]
	 */
	public static byte[] char2Bytes(char value) {
		byte[] result = new byte[2];
		
		/*
		 * char是16位, 所以我们需要将它分成两个字节。
		 * 首先获取高8位 (前8位)
		 */
		result[0] = (byte) (value >> 8); // 将char值右移8位, 然后强制转换为byte
		
		// 然后获取低8位（后8位）
		result[1] = (byte) (value & 0xFF); // 通过与0xFF进行位与操作来获取低8位
		
		return result;
	}
	
	/**
	 * 用字符串的形式表示一个byte的二进制形式
	 *
	 * @param b
	 * @return String
	 */
	//public static String byte2BinaryStr(byte b) {
	//	StringBuilder sb = new StringBuilder();
	//	for (int i = 7; i >= 0; i--) {
	//		// 使用位移和按位与操作来检查每一位
	//		sb.append((b >> i) & 1);
	//	}
	//	return sb.toString();
	//}
	
	/**
	 * 用字符串的形式表示byte数组的二进制形式
	 *
	 * @param bytes
	 * @return String
	 */
	public static String bytes2BinaryStr(byte... bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			for (int j = 7; j >= 0; j--) {
				// 使用位移和按位与操作来检查每一位
				sb.append((b >> j) & 1);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 打印byte数组的二进制形式
	 *
	 * @param bytes 字节数组
	 */
	public static void print(byte... bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			for (int i = 7; i >= 0; i--) {
				// 使用位移和按位与操作来检查每一位
				sb.append((b >> i) & 1);
			}
		}
		System.out.println(sb.toString());
	}
	
	/**
	 * int型转16进制, 不带0x前缀
	 *
	 * @param i
	 * @return String
	 */
	public static String int2Hex(Integer i) {
		if (i == null) {
			return null;
		}
		
		return Integer.toHexString(i);
	}
	
	/**
	 * 16进制字符串转整型
	 *
	 * @param hex
	 * @return Integer
	 */
	public static Integer hex2Int(String hex) {
		if (isBlank(hex)) {
			return null;
		}
		
		if (hex.startsWith(HEX_PREFIX)) {
			hex = hex.substring(2);
		}
		return Integer.parseInt(hex, 16);
	}
	
	
	/**
	 * 判断val是不是2的n次幂, 如 2, 4, 8, 16...
	 *
	 * <pre> {@code
	 * 16  0001 0000
	 * -16 1111 0000
	 * }</pre>
	 * 位与后还是0001 0000, 所以2的n次幂就有这个特性 <p/>
	 *
	 * @param val
	 * @return boolean
	 */
	public static boolean isPowerOfTwo(int val) {
		return (val & -val) == val;
	}
	
	/**
	 * 对hex预处理, null检查, 空字符串检查, trim, 去掉开头的0x, 返回处理后的字符串
	 *
	 * @param hex
	 * @return String
	 */
	private static String preCheck(String hex) {
		if (isBlank(hex)) {
			throw new IllegalArgumentException("Can not be blank!");
		}
		
		hex = hex.trim().toUpperCase();
		if (hex.indexOf("0X") != -1) {
			hex = hex.substring(2);
		}
		return hex;
	}
	
	/**
	 * Converts a binary string to a byte array.
	 * If the binary string is not a multiple of 8 in length, it is left-padded with zeros.
	 *
	 * @param binaryString the binary string to convert
	 * @return the resulting byte array
	 */
	public static byte[] binaryStr2Bytes(String binaryString) {
		// 检查输入字符串是否为空或null
		if (binaryString == null || binaryString.isEmpty()) {
			throw new IllegalArgumentException("Binary string must be non-null and not empty.");
		}
		
		// 确定字符串长度是否是8的倍数
		int length = binaryString.length();
		int missingLength = 8 - (length % 8);
		
		// 如果长度不是8的倍数，左边用0补足
		if (missingLength != 8) {
			char[] padding = new char[missingLength];
			java.util.Arrays.fill(padding, '0'); // 用0填充缺失的部分
			binaryString = new String(padding) + binaryString; // 将填充和原始字符串组合
		}
		
		// 计算所需的字节数
		int numBytes = binaryString.length() / 8;
		byte[] byteArray = new byte[numBytes];
		
		for (int i = 0; i < numBytes; i++) {
			// 每8位解析一次，转换为一个字节
			int byteValue = Integer.parseInt(binaryString.substring(i * 8, (i + 1) * 8), 2);
			byteArray[i] = (byte) byteValue;
		}
		
		return byteArray; // 返回结果数组
	}
	
	/**
	 * byte[]转int
	 *
	 * @param bytes
	 * @return int
	 */
	public static int bytes2Int(byte[] bytes) {
		// 检查输入数组是否为null，或长度超过4（因为int是4字节）
		if (bytes == null || bytes.length > 4) {
			throw new IllegalArgumentException("Byte array must be non-null and have length of 1 to 4 bytes.");
		}
		
		int result = 0; // 初始化结果为0
		// 遍历字节数组
		for (int i = 0; i < bytes.length; i++) {
			/*
			 * |= 是一个位运算符的赋值运算符，用于对两个数值进行按位或（bitwise OR）操作，并将结果赋值给左侧的变量
			 *
			 * bytes[i] & 0xFF 将字节转换为无符号整数
			 * 在Java中, 字节（byte）是一个8位的有符号整数, 其取值范围是从-128到127。当你直接把一个byte类型的值视为int时,
			 * 会发生符号扩展 (sign extension), 这意味着原来byte的最高位 (符号位) 会扩展到int的高位。
			 * 这可能导致意料之外的负数值, 如果原始byte是正数。
			 *
			 * 使用bytes[i] & 0xFF操作可以防止这种符号扩展, 并且正确地将字节转换为无符号整数。看下面的例子
			 * String bytes = "11011011";
			 * byte[] data = RedixUtils.binaryStr2ByteArray(bytes);
			 * int i = (int)data[0];  //-37
			 * int j = (int)data[0]&0xff; //219
			 */
			result |= (bytes[i] & 0xFF) << (8 * (bytes.length - i - 1));
		}
		return result; // 返回最终计算的整数结果
	}
	
	
	/**
	 * 字节数组转float, java里float占4字节
	 *
	 * @param bytes
	 * @return float
	 */
	public static float bytes2Float(byte[] bytes) {
		int intBits =
				(bytes[3] & 0xFF) << 24 |
						(bytes[2] & 0xFF) << 16 |
						(bytes[1] & 0xFF) << 8 |
						(bytes[0] & 0xFF);
		return Float.intBitsToFloat(intBits);
	}
}
