package com.loserico.networking.utils;

import com.loserico.codec.RedixUtils;
import com.loserico.networking.matcher.IpAddressMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * IP 工具类
 * https://mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
 * 
 * https://stackoverflow.com/questions/31667745/ip-address-sub-net-mask-validation
 * 
 * <p>
 * Copyright: (C), 2021-04-08 14:15
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class IPUtils {
	
	
	/**
	 * xx.xx.xx.2/24
	 * 斜杠后面的数字就表示子网掩码, 数字具体代表32位子网掩码(二进制形式)中前面的"1"的个数
	 * <p>
	 * 2001:0db8:3c4d:0015::/64
	 * 如果是IPV6的话表示128位子网掩码(二进制形式)中前面的"1"的个数
	 */
	public static final Pattern SUBNET_MASK_PATTERN = Pattern.compile("(.+)/{1}(\\d{1,3})");
	
	/**
	 * (
	 * [0-9]         # 0-9
	 * |             # or
	 * [1-9][0-9]    # 10-99
	 * |             # or
	 * 1[0-9][0-9]   # 100-199
	 * |             # or
	 * 2[0-4][0-9]   # 200-249
	 * |             # or
	 * 25[0-5]       # 250-255
	 * )
	 * (\.(?!$)|$))    # ensure IPv4 doesn't end with a dot
	 * {4}             # 4 times.
	 */
	public static final Pattern IPV4_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
	
	/**
	 * IPv6 地址的长度为128 位, 由八个16 位字段组成
	 * (
	 * ([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|          # 1:2:3:4:5:6:7:8
	 * ([0-9a-fA-F]{1,4}:){1,7}:|                         # 1::                              1:2:3:4:5:6:7::
	 * ([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|         # 1::8             1:2:3:4:5:6::8  1:2:3:4:5:6::8
	 * ([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|  # 1::7:8           1:2:3:4:5::7:8  1:2:3:4:5::8
	 * ([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|  # 1::6:7:8         1:2:3:4::6:7:8  1:2:3:4::8
	 * ([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|  # 1::5:6:7:8       1:2:3::5:6:7:8  1:2:3::8
	 * ([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|  # 1::4:5:6:7:8     1:2::4:5:6:7:8  1:2::8
	 * [0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|       # 1::3:4:5:6:7:8   1::3:4:5:6:7:8  1::8
	 * :((:[0-9a-fA-F]{1,4}){1,7}|:)|                     # ::2:3:4:5:6:7:8  ::2:3:4:5:6:7:8 ::8       ::
	 * fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|     # fe80::7:8%eth0   fe80::7:8%1     (link-local IPv6 addresses with zone index)
	 * ::(ffff(:0{1,4}){0,1}:){0,1}
	 * ((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}
	 * (25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|          # ::255.255.255.255   ::ffff:255.255.255.255  ::ffff:0:255.255.255.255  (IPv4-mapped IPv6 addresses and IPv4-translated addresses)
	 * ([0-9a-fA-F]{1,4}:){1,4}:
	 * ((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}
	 * (25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])           # 2001:db8:3:4::192.0.2.33  64:ff9b::192.0.2.33 (IPv4-Embedded IPv6 Address)
	 * )
	 */
	public static final Pattern IPV6_PATTERN = Pattern.compile("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|" +
			"([0-9a-fA-F]{1,4}:){1,7}:|" +
			"([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +
			"([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
			"([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|" +
			"([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
			"([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|" +
			"[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|" +
			":((:[0-9a-fA-F]{1,4}){1,7}|:)|" +
			"fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|" +
			"::(ffff(:0{1,4}){0,1}:){0,1}" +
			"((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}" +
			"(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|" +
			"([0-9a-fA-F]{1,4}:){1,4}:" +
			"((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}" +
			"(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))");
	
	/**
	 * 校验IPV6 Prefix部分
	 */
	public static final Pattern IPV6_PREFIX_PATTERN = Pattern.compile("((([1-9])|([1-9][0-9])|(1[0-1][0-9]|12[0-8]))){0,1}");
	
	/**
	 * IPv4 范围从 0.0.0.0 到 255.255.255.255
	 * IPv4 地址由十进制数和点来表示, 每个地址包含 4 个十进制数, 其范围为 0 - 255, 用(".")分割。比如: 172.16.254.1
	 * 同时, IPv4 地址内的数不会以 0 开头, 比如, 地址 172.16.254.01 是不合法的
	 * 
	 * CIDR-notation string 是类似这种 192.168.0.1/16, 要验证后面的掩码是否合法, 只需要验证掩码在1~32之内即可
	 *
	 * @param ip
	 * @return
	 */
	public static boolean isValidIpV4(String ip) {
		if (isBlank(ip)) {
			return false;
		}
		
		//先检查是否包含子网掩码部分10.10.10.0/24
		String[] ipSubnetMaskPair = splitSubnetMask(ip);
		
		/*
		 * 表示纯IP, 没有子网掩码部分
		 * 直接用正则匹配, 检查格式是否正确即可
		 */
		if (ipSubnetMaskPair == null) {
			return IPV4_PATTERN.matcher(ip).matches();
		}
		
		ip = ipSubnetMaskPair[0];
		//先判断IP部分格式是否正确
		boolean matches = IPV4_PATTERN.matcher(ip).matches();
		//IP格式不正确的话直接返回
		if (!matches) {
			return false;
		}
		
		return isValidSubnetMask(ipSubnetMaskPair[1]);
	}
	
	/**
	 * IPv6 地址由 8 组 16 进制的数字来表示, 每组表示 16 比特
	 * 这些组数字通过 (":")分割。比如 2001:0db8:85a3:0000:0000:8a2e:0370:7334 是一个有效的地址。
	 * 而且, 我们可以加入一些以 0 开头的数字, 字母可以使用大写, 也可以是小写。
	 * 所以 2001:db8:85a3:0:0:8A2E:0370:7334 也是一个有效的 IPv6 address地址
	 * <p>
	 * 然而, 我们不能因为某个组的值为 0, 而使用一个空的组, 以至于出现 (::) 的情况。
	 * 比如 2001:0db8:85a3::8A2E:0370:7334 是无效的 IPv6 地址
	 * <p>
	 * 同时, 在 IPv6 地址中, 多余的 0 也是不被允许的。
	 * 比如 02001:0db8:85a3:0000:0000:8a2e:0370:7334 是无效的
	 * <p>
	 * fe80:0:0:0:0:0:c0a8:1/120 是合法的IPv6
	 *
	 * @param ip
	 * @return boolean
	 */
	public static boolean isValidIpV6(String ip) {
		if (isBlank(ip)) {
			return false;
		}
		
		//先检查是否包含Prefix部分10.10.10.0/24
		String[] ipSubnetMaskPair = splitSubnetMask(ip);
		
		/*
		 * 表示纯IP, 没有Prefix部分
		 * 直接用正则匹配, 检查格式是否正确即可
		 */
		if (ipSubnetMaskPair == null) {
			return IPV6_PATTERN.matcher(ip).matches();
		}
		
		ip = ipSubnetMaskPair[0];
		//先判断IP部分格式是否正确
		boolean matches = IPV6_PATTERN.matcher(ip).matches();
		//IP格式不正确的话直接返回
		if (!matches) {
			return false;
		}
		
		return isValidIPV6Prefix(ipSubnetMaskPair[1]);
	}
	
	/**
	 * 抽取IP地址中的子网掩码部分, 如
	 * xx.xx.xx.2/24 子网掩码为24
	 *
	 * @param ip
	 * @return
	 */
	public static Integer subnetMask(String ip) {
		if (isBlank(ip)) {
			return null;
		}
		
		ip = ip.trim();
		
		Matcher matcher = SUBNET_MASK_PATTERN.matcher(ip);
		if (matcher.matches()) {
			String subnetMask = matcher.group(2);
			return Integer.parseInt(subnetMask);
		}
		
		return null;
	}
	
	/**
	 * 抽取IP地址中的IP和子网掩码部分, 如
	 * xx.xx.xx.2/24 IP xx.xx.xx.2 子网掩码为24
	 * 如果不含子网掩码部分, 返回null
	 *
	 * @param ip
	 * @return String[]
	 */
	public static String[] splitSubnetMask(String ip) {
		if (isBlank(ip)) {
			return null;
		}
		ip = ip.trim();
		
		Matcher matcher = SUBNET_MASK_PATTERN.matcher(ip);
		if (matcher.matches()) {
			String[] ipSubnetMaskPair = new String[2];
			ipSubnetMaskPair[0] = matcher.group(1);
			ipSubnetMaskPair[1] = matcher.group(2);
			
			return ipSubnetMaskPair;
		}
		
		return null;
	}
	
	/**
	 * 判断IP是否在地址范围内
	 * @param ip     192.168.100.101
	 * @param subnet 192.168.100.1/24
	 * @return boolean
	 */
	public static boolean isIpInRange(String ip, String subnet) {
		IpAddressMatcher addressMatcher = new IpAddressMatcher(subnet);
		return addressMatcher.matches(ip);
	}
	
	/**
	 * 验证是合法的子网掩码, 验证规则是: 数字且处于[1, 32]
	 * @param subnetMask
	 * @return boolean
	 */
	private static boolean isValidSubnetMask(String subnetMask) {
		if (isBlank(subnetMask)) {
			return false;
		}
		
		try {
			int mask = Integer.parseInt(subnetMask.trim());
			return mask >= 1 && mask <= 32;
		} catch (NumberFormatException e) {
			log.warn("subnetMask {} should be a number!");
			return false;
		}
	}
	
	/**
	 * 判断是否合法的IPV6 Prefix
	 * @param prefix
	 * @return boolean
	 */
	private static boolean isValidIPV6Prefix(String prefix) {
		if (isBlank(prefix)) {
			return false;
		}
		
		return IPV6_PREFIX_PATTERN.matcher(prefix.trim()).matches();
	}
	
	/**
	 * IPv4的每部分是一个十进制数字
	 * @param subIp
	 * @return String IP的二进制表示
	 */
	private static String toBinaryStr(Integer subIp) {
		/*
		 * 10.10.10.0
		 * IP的每一部分都介于0~255之间, 255的二进制表示为11111111
		 * 所以如果小于255, 那么二进制前面补零, 补足8位
		 */
		String s = RedixUtils.intToBinary(subIp);
		int initialLength = s.length();
		for (int i = initialLength; i < 8; i++) {
			s = "0" + s;
		}
		
		return s;
	}
	
	/**
	 * IPv6的每部分是一个十六进制字符串, 转成二进制每组占16位
	 * @param subIp
	 * @return String IP的二进制表示
	 */
	private static String toBinaryStr(String subIp) {
		if (isBlank(subIp)) {
			return "0000000000000000";
		}
		String s = RedixUtils.hexToBinary(subIp);
		int initialLength = s.length();
		for (int i = initialLength; i < 16; i++) {
			s = "0" + s;
		}
		
		return s;
	}
}
