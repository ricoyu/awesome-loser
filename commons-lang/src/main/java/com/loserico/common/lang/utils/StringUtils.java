package com.loserico.common.lang.utils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class StringUtils {
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final Charset UTF8_CHARSET = Charset.forName(DEFAULT_CHARSET);
	public static final String EMPTY_STRING = "";
	
	private static final Logger log = LoggerFactory.getLogger(StringUtils.class);
	
	/**
	 * 向url后追加参数，拼接时需要判断连s接符是? or &，同时需要对参数值进行编码
	 *
	 * @param origUrl
	 * @param parameterName
	 * @param parameterVal
	 * @return
	 */
	public static String appendUrlParameter(String origUrl, String parameterName, String parameterVal) {
		if (origUrl == null) {
			return null;
		}
		
		String bound = origUrl.contains("?") ? "&" : "?";
		try {
			return origUrl + bound + parameterName + "=" + URLEncoder.encode(parameterVal, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static String join(CharSequence... elements) {
		return String.join("", elements);
	}
	
	public static String joinWith(String spliter, CharSequence... elements) {
		return Joiner.on(spliter)
				.skipNulls()
				.join(elements);
	}
	
	public static String joinWith(String spliter, Object... elements) {
		return Joiner.on(spliter)
				.skipNulls()
				.join(elements);
	}
	
	/**
	 * <p>
	 * Joins the elements of the provided varargs into a single String containing the provided
	 * elements.
	 * </p>
	 *
	 * <p>
	 * No delimiter is added before or after the list. {@code null} elements and separator are
	 * treated as empty Strings ("").
	 * </p>
	 *
	 * <pre>
	 * StringUtils.joinWith(",", {"a", "b"})        = "a,b"
	 * StringUtils.joinWith(",", {"a", "b",""})     = "a,b,"
	 * StringUtils.joinWith(",", {"a", null, "b"})  = "a,,b"
	 * StringUtils.joinWith(null, {"a", "b"})       = "ab"
	 * StringUtils.joinWith(",", [123,456])       = "123,456"
	 * </pre>
	 *
	 * @param separator the separator character to use, null treated as ""
	 * @param objects   the varargs providing the values to join together. {@code null} elements are
	 *                  treated as ""
	 * @return the joined String.
	 * @throws IllegalArgumentException if a null varargs is provided
	 * @since 3.5
	 */
	public static String joinWith(final String separator, final List<?> objects) {
		if (objects == null) {
			throw new IllegalArgumentException("Object varargs must not be null");
		}
		
		final String sanitizedSeparator = defaultString(separator, EMPTY_STRING);
		
		final StringBuilder result = new StringBuilder();
		
		final Iterator<?> iterator = objects.iterator();
		while (iterator.hasNext()) {
			final String value = Objects.toString(iterator.next(), "");
			result.append(value);
			
			if (iterator.hasNext()) {
				result.append(sanitizedSeparator);
			}
		}
		
		return result.toString();
	}
	
	public static String join(String spliter, Collection<?> elements) {
		return Joiner.on(spliter)
				.skipNulls()
				.join(elements);
	}
	
	/**
	 * 将字符串有某种编码转变成另一种编码
	 *
	 * @param string        编码的字符串
	 * @param originCharset 原始编码格式
	 * @param targetCharset 目标编码格式
	 * @return String 编码后的字符串
	 */
	public static String encodeString(String string, Charset originCharset, Charset targetCharset) {
		return string = new String(string.getBytes(originCharset), targetCharset);
	}
	
	/**
	 * URL编码
	 *
	 * @param string  编码字符串
	 * @param charset 编码格式
	 * @return String
	 */
	public static String encodeUrl(String string, String charset) {
		if (null != charset && !charset.isEmpty()) {
			try {
				return URLEncoder.encode(string, charset);
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
		}
		try {
			return URLEncoder.encode(string, DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	/**
	 * URL编码
	 *
	 * @param string  解码字符串
	 * @param charset 解码格式
	 * @return String
	 */
	public static String decodeUrl(String string, String charset) {
		if (null != charset && !charset.isEmpty()) {
			try {
				return URLDecoder.decode(string, charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
		try {
			return URLDecoder.decode(string, DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	public static byte[] utf8Bytes(String data) {
		return data.getBytes(UTF8_CHARSET);
	}
	
	public static String utf8String(byte[] data) {
		return new String(data, UTF8_CHARSET);
	}
	
	/**
	 * 生成全局唯一99位长的hex字符串
	 *
	 * @return
	 */
	public static String uniqueKey() {
		return RandomStringUtils.randomAlphanumeric(99);
	}
	
	public static String uniqueKey(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}
	
	/**
	 * 通过Java API生成指定长度的随机字符串
	 *
	 * @param targetStringLength
	 * @return
	 */
	public static String randomStr(int targetStringLength) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}
	
	/**
	 * @param s1
	 * @param s2
	 * @return boolean
	 * @of 两个都是null认为不同<br />
	 * 一个null一个不为null认为不同<br/>
	 * 一个""一个" "认为相同<br/>
	 * 大小写敏感匹配
	 * @on
	 * @deprecated
	 */
	public static boolean stringEqual(String s1, String s2) {
		if (s1 == null) {
			return false;
		}
		
		if (s2 == null) {
			return false;
		}
		return s1.trim().equals(s2.trim());
	}
	
	/**
	 * <p>Compares two String, returning {@code true} if they represent
	 * equal sequences of characters.</p>
	 *
	 * <p>{@code null}s are handled without exceptions. Two {@code null}
	 * references are considered to be equal. The comparison is case sensitive.</p>
	 *
	 * <pre>
	 * StringUtils.equals(null, null)   = true
	 * StringUtils.equals(null, "abc")  = false
	 * StringUtils.equals("abc", null)  = false
	 * StringUtils.equals("abc", "abc") = true
	 * StringUtils.equals("abc", "abc ") = true
	 * StringUtils.equals("abc", "ABC") = false
	 * </pre>
	 *
	 * @param s1 the first CharSequence, may be {@code null}
	 * @param s2 the second CharSequence, may be {@code null}
	 * @return {@code true} if the String are equal (case-sensitive), or both {@code null}
	 * @see Object#equals(Object)
	 */
	public static boolean equalTo(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 == null) {
			return false;
		}
		
		if (s2 == null) {
			return false;
		}
		return s1.trim().equals(s2.trim());
	}
	
	/**
	 * source equals targets钟任何一个即返回true
	 *
	 * @param source
	 * @param targets
	 * @return boolean
	 */
	public static boolean equalsAny(String source, String... targets) {
		if (source == null) {
			return false;
		}
		
		if (targets == null) {
			return false;
		}
		
		boolean isEqual = false;
		for (int i = 0; i < targets.length; i++) {
			String target = targets[i];
			if (target == null) {
				continue;
			}
			if (equalTo(source, target)) {
				isEqual = true;
				break;
			}
		}
		return isEqual;
	}
	
	/**
	 * source与targets中任何一个euqals即返回true,不区分大小写
	 *
	 * @param source
	 * @param targets
	 * @return boolean
	 */
	public static boolean equalsIgAny(String source, String... targets) {
		if (source == null) {
			return false;
		}
		
		if (targets == null) {
			return false;
		}
		
		boolean isEqual = false;
		for (int i = 0; i < targets.length; i++) {
			String target = targets[i];
			if (target == null) {
				continue;
			}
			if (equalsIgCase(source, target)) {
				isEqual = true;
				break;
			}
		}
		return isEqual;
	}
	
	/**
	 * <blockquote><pre>
	 * beCompared 为null 认为不同
	 * beCompared与comparetors任意一个equal 认为相同
	 *
	 * 一个"" 一个" "			认为相同
	 * 不区分大小写
	 * </pre></blockquote>
	 *
	 * @param beCompared
	 * @param comparetors
	 * @return boolean
	 */
	public static boolean equalsIgCase(String beCompared, String... comparetors) {
		if (beCompared == null) {
			return false;
		}
		
		if (comparetors == null || comparetors.length == 0) {
			return false;
		}
		
		boolean equals = false;
		for (int i = 0; i < comparetors.length; i++) {
			String comparetor = comparetors[i];
			if (comparetor != null) {
				if (beCompared.trim().equalsIgnoreCase(comparetor.trim())) {
					return equals = true;
				}
			}
			
		}
		return equals;
	}
	
	/**
	 * 返回由surroundStr包裹target之新字符串<br/>
	 * target empty 则直接返回target<br/>
	 * surroundStr empty 则直接返回target<br/>
	 *
	 * @param target      目标字符串
	 * @param surroundStr 包裹目标字符串的字符串
	 * @return String
	 */
	public static String suroundWith(String target, String surroundStr) {
		if (isBlank(target)) {
			return target;
		}
		if (isBlank(surroundStr)) {
			return target;
		}
		return surroundStr + target + surroundStr;
	}
	
	/**
	 * 如果value为null，返回""，否则返回value。同时trim两边空格
	 *
	 * @param value
	 * @return String
	 */
	public static String escapeNull(String value) {
		if (isEmpty(value)) {
			return "";
		}
		return value.trim();
	}
	
	/**
	 * 测试str是否以prefix开头
	 *
	 * @param str
	 * @param prefix
	 * @return boolean
	 */
	public static boolean startsWith(String str, String prefix) {
		if (str == null) {
			return false;
		}
		
		if (prefix == null) {
			return false;
		}
		
		return str.startsWith(prefix);
	}
	
	/**
	 * 测试str是否以endWith指定的任何字符结尾
	 * <blockquote><pre>
	 * str为null则返回false
	 * endWith为null或者长度为0，返回false
	 * </pre></blockquote>
	 *
	 * @param str
	 * @param endWith
	 * @return boolean
	 * @on
	 */
	public static boolean endWith(String str, String... endWith) {
		if (null == str) {
			return false;
		}
		
		if (null == endWith) {
			return false;
		}
		
		if (endWith.length == 0) {
			return false;
		}
		
		for (int i = 0; i < endWith.length; i++) {
			String suffix = endWith[i];
			if (str.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 截取子串
	 * <blockquote><pre>
	 * 如果s为null则返回null
	 * 如果s为空字符串或者只包含空格，则返回""
	 * 如果beginIndex为负数，表示从后截取
	 * </pre></blockquote>
	 *
	 * @param s
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 * @on
	 */
	public static String subStr(String s, int beginIndex, int endIndex) {
		if (null == s) {
			return null;
		}
		
		if (beginIndex < 0) {
			beginIndex = s.length() + beginIndex;
		}
		if (endIndex < 0) {
			endIndex = s.length() + endIndex;
		}
		try {
			return s.substring(beginIndex, endIndex);
		} catch (IndexOutOfBoundsException e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 如果s为null则返回null，否则从beginIndex开始截取子串
	 * <blockquote><pre>
	 *  	"unhappy".substring(2) returns "happy"
	 *  	"Harbison".substring(3) returns "bison"
	 * 		"emptiness".substring(9) returns "" (an empty string)
	 *  </pre></blockquote>
	 *
	 * @param s
	 * @param beginIndex
	 * @return
	 * @on
	 */
	public static String subStr(String s, int beginIndex) {
		if (null == s) {
			return null;
		}
		
		if (beginIndex < 0) {
			beginIndex = s.length() + beginIndex;
		}
		try {
			return s.substring(beginIndex);
		} catch (IndexOutOfBoundsException e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 用空格连接每个字符串，如果字符串为null或者空字符串，则忽略之
	 *
	 * @param args
	 * @return String
	 */
	public static String concatSpace(String... args) {
		return asList(args).stream()
				.filter(s -> isNotBlank(s))
				.collect(joining(" "));
	}
	
	/**
	 * 连接每个字符串，如果字符串为null或者空字符串，则忽略之
	 *
	 * @param args
	 * @return String
	 */
	public static String concat(String... args) {
		return asList(args).stream()
				.filter(s -> isNotBlank(s))
				.collect(joining());
	}
	
	/**
	 * 连接每个字符串，如果字符串为null或者空字符串，则忽略之
	 *
	 * @param args
	 * @return String
	 */
	public static String concatWith(String delimiter, String... args) {
		return asList(args).stream()
				.filter(s -> isNotBlank(s))
				.collect(joining(delimiter));
	}
	
	/**
	 * 先调用参数的toString()方法将其转成字符串，null则为"" 然后连接起来
	 *
	 * @param args
	 * @return String
	 */
	public static String concat(Object... args) {
		return asList(args).stream()
				.map((arg) -> {
					if (arg == null) {
						return "";
					}
					return arg.toString();
				})
				.filter(s -> isNotBlank(s))
				.collect(joining());
	}
	
	/**
	 * 先调用参数的toString()方法将其转成字符串，null则为"" 然后连接起来
	 *
	 * @param args
	 * @return String
	 */
	public static String concatWith(String delimiter, Object... args) {
		return asList(args).stream()
				.map((arg) -> {
					if (arg == null) {
						return "";
					}
					return arg.toString();
				})
				.filter(s -> isNotBlank(s))
				.collect(joining(delimiter));
	}
	
	/**
	 * 如果str为null或者"", " ", 则返回alternate, 否则返回str
	 *
	 * @param str
	 * @param alternate
	 * @return String
	 */
	public static String ifEmpty(String str, String alternate) {
		if (isBlank(str)) {
			return alternate;
		}
		return str;
	}
	
	/**
	 * 检查source字符串是否包含target子串，大小写敏感
	 * <blockquote><pre>
	 * source   target   结果
	 * null             false
	 * 不为null  null    false
	 * "AbCde"  ""      true
	 * "AbCde"  " "     false
	 * "AbCde"  "b"     true
	 * AbCde"   "c"     false
	 * </pre></blockquote>
	 *
	 * @param source
	 * @param target
	 * @return boolean
	 * @on
	 */
	public static boolean contains(String source, String target) {
		if (source == null) {
			return false;
		}
		
		if (target == null) {
			return false;
		}
		
		return source.indexOf(target) != -1;
	}
	
	public static boolean containsAny(String source, String... targets) {
		if (source == null) {
			return false;
		}
		
		if (targets == null) {
			return false;
		}
		
		boolean contains = false;
		for (int i = 0; i < targets.length; i++) {
			String target = targets[i];
			if (target == null) {
				continue;
			}
			if (source.indexOf(target) != -1) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	/**
	 * 检查source字符串是否包含target子串，大小写敏感
	 * <blockquote><pre>
	 * source   target   结果
	 * null             false
	 * 不为null  null    false
	 * "AbCde"  ""      true
	 * "AbCde"  " "     false
	 * "AbCde"  "b"     true
	 * AbCde"   "c"     true
	 * </pre></blockquote>
	 *
	 * @param source
	 * @param target
	 * @return boolean
	 * @on
	 */
	public static boolean containsIgCase(String source, String target) {
		if (source == null) {
			return false;
		}
		
		if (target == null) {
			return false;
		}
		
		return source.toLowerCase().indexOf(target.toLowerCase()) != -1;
	}
	
	public static boolean containsAnyIgCase(String source, String... targets) {
		if (source == null) {
			return false;
		}
		
		if (targets == null) {
			return false;
		}
		
		boolean contains = false;
		for (int i = 0; i < targets.length; i++) {
			String target = targets[i];
			if (target == null) {
				continue;
			}
			if (source.toLowerCase().indexOf(target.toLowerCase()) != -1) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	/**
	 * 检查source是否包含targets的所有元素
	 * <blockquote><pre>
	 * source	null	false
	 * targets	null	false
	 * targets任何元素为null返回 false
	 * </pre></blockquote>
	 *
	 * @param source
	 * @param targets
	 * @return
	 * @on
	 */
	public static boolean containsAll(String source, String... targets) {
		if (source == null) {
			return false;
		}
		
		if (targets == null) {
			return false;
		}
		
		for (int i = 0; i < targets.length; i++) {
			String target = targets[i];
			if (target == null) {
				continue;
			}
			if (source.indexOf(target) == -1) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean containsAllIgCase(String source, String... targets) {
		if (source == null) {
			return false;
		}
		
		if (targets == null) {
			return false;
		}
		
		for (int i = 0; i < targets.length; i++) {
			String target = targets[i];
			if (target == null) {
				continue;
			}
			if (source.toLowerCase().indexOf(target.toLowerCase()) == -1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 安装字母顺序区分大小写比较
	 * null 和 "", "  " 认为是相等的
	 *
	 * <ul>
	 * <li>-1 prev < next
	 * <li>0 prev == next
	 * <li>1 prev > next
	 * </ul>
	 *
	 * @param prev
	 * @param next
	 * @return int
	 * @on
	 */
	public static int compareTo(String prev, String next) {
		if (isBlank(prev) && isBlank(next)) {
			return 0;
		}
		if (isBlank(prev) && !isBlank(next)) {
			return -1;
		}
		if (!isBlank(prev) && isBlank(next)) {
			return 1;
		}
		return prev.compareTo(next);
	}
	
	/**
	 * 安装字母顺序不区分大小写比较
	 * null 和 "", "  " 认为是相等的
	 *
	 * <ul>
	 * <li>-1 prev < next
	 * <li>0 prev == next
	 * <li>1 prev > next
	 * </ul>
	 *
	 * @param prev
	 * @param next
	 * @return int
	 * @on
	 */
	public static int compareToIgnoreCase(String prev, String next) {
		if (isBlank(prev) && isBlank(next)) {
			return 0;
		}
		if (isBlank(prev) && !isBlank(next)) {
			return -1;
		}
		if (!isBlank(prev) && isBlank(next)) {
			return 1;
		}
		return prev.compareToIgnoreCase(next);
	}
	
	/**
	 * 如果str为null或者"", " ", 则返回alternate, 否则返回str
	 *
	 * @param source
	 * @param replacement
	 * @return String
	 */
	public static String orElse(String source, String replacement) {
		return ofNullable(source).orElse(replacement);
	}
	
	/**
	 * 去掉str中的所有空白符，包括开头、结尾及中间任何位置
	 *
	 * @param str
	 * @return String
	 */
	public static String trimAll(String str) {
		if (str == null) {
			return str;
		}
		return CharMatcher.whitespace().removeFrom(str);
	}
	
	/**
	 * 去掉source中出现的所有trimStr
	 * 如果source为null，返回null
	 * 如果trimStr为null，返回source
	 * 同时首尾的空格也会去掉
	 * <p>
	 * 注意如果要去掉source中的.号，传入的trimStr应该是\\.
	 *
	 * @param source
	 * @param trimStrs
	 * @return String
	 */
	public static String trimAll(String source, String... trimStrs) {
		if (source == null) {
			return null;
		}
		
		if (trimStrs == null || trimStrs.length == 0) {
			return source;
		}
		
		for (int i = 0; i < trimStrs.length; i++) {
			String trimStr = trimStrs[i];
			source = source.replaceAll(trimStr, "");
		}
		
		return source.trim();
	}
	
	/**
	 * 移除开头结尾的所有空格、单引号、双引号
	 *
	 * @param source
	 * @return String
	 */
	public static String trimQuote(String source) {
		if (source == null) {
			return null;
		}
		source = source.trim();
		
		boolean found = false;
		if (source.startsWith("\"") || source.startsWith("'")) {
			source = source.substring(1, source.length());
			found = true;
		}
		if (source.endsWith("\"") || source.endsWith("'")) {
			source = source.substring(0, source.length() - 1);
			found = true;
		}
		if (found) {
			return trimQuote(source);
		}
		return source;
	}
	
	/**
	 * 移除尾部字符串
	 *
	 * @param str
	 * @param trailingStr
	 * @return String
	 */
	public static String trimTrailingCharacter(String str, String trailingStr) {
		return CharMatcher.anyOf(trailingStr).trimTrailingFrom(str);
	}
	
	/**
	 * 移除头部的字符串
	 *
	 * @param str
	 * @param leadingStr
	 * @return String
	 */
	public static String trimLeadingCharacter(String str, String leadingStr) {
		return CharMatcher.anyOf(leadingStr).trimLeadingFrom(str);
	}
	
	/**
	 * Returns a 'cleaned' representation of the specified argument. 'Cleaned' is defined as the
	 * following:
	 * <p/>
	 * <ol>
	 * <li>If the specified <code>String</code> is <code>null</code>, return <code>null</code></li>
	 * <li>If not <code>null</code>, {@link String#trim() trim()} it.</li>
	 * <li>If the trimmed string is equal to the empty String (i.e. &quot;&quot;), return
	 * <code>null</code></li>
	 * <li>If the trimmed string is not the empty string, return the trimmed version</li>.
	 * </ol>
	 * <p/>
	 * Therefore this method always ensures that any given string has trimmed text, and if it
	 * doesn't, <code>null</code> is returned.
	 *
	 * @param in the input String to clean.
	 * @return a populated-but-trimmed String or <code>null</code> otherwise
	 */
	public static String clean(String in) {
		String out = in;
		
		if (in != null) {
			out = in.trim();
			if (out.equals(EMPTY_STRING)) {
				out = null;
			}
		}
		
		return out;
	}
	
	/**
	 * 在clean(in)的基础上，如果字符串两头有引号("),那么把引号去掉
	 *
	 * @param in the input String to clean.
	 * @return string
	 * @on
	 */
	public static String cleanQuotationMark(String in) {
		String result = clean(in);
		if (result != null) {
			while ("\"".equals(result.substring(0, 1))) {
				result = result.substring(1);
				if (result.length() == 0) {
					return result;
				}
			}
			
			if ("\"".equals(result.substring(result.length() - 1, result.length()))) {
				result = result.substring(0, result.length() - 1);
				if (result.length() == 0) {
					return result;
				}
			}
		}
		
		return result;
	}
	
	public static String toLowerCase(String str) {
		if (str == null) {
			return str;
		}
		return str.toLowerCase();
	}
	
	public static String toUpperCase(String str) {
		if (str == null) {
			return str;
		}
		return str.toUpperCase();
	}
	
	/**
	 * 如果s不为null，调用func并返回值，否则返回null
	 *
	 * @param s
	 * @param altinate
	 * @return
	 */
	public static String ifNotNull(String s, Function<String, String> altinate) {
		if (s == null) {
			return null;
		}
		return altinate.apply(s);
	}
	
	/**
	 * 返回第一个不为null也不为空字符串的参数，否则返回null
	 *
	 * @param args
	 * @return String
	 */
	public static String firstNonNull(String... args) {
		if (args == null || args.length == 0) {
			return null;
		}
		for (int i = 0; i < args.length; i++) {
			if (isNotBlank(args[i])) {
				return args[i];
			}
		}
		return null;
	}
	
	/**
	 * bytes 转 UTF8 编码字符串
	 *
	 * @param bytes
	 * @return String
	 */
	public static String toString(byte[] bytes) {
		return toString(bytes, UTF_8);
	}
	
	/**
	 * bytes 转 指定编码字符串
	 *
	 * @param bytes
	 * @return String
	 */
	public static String toString(byte[] bytes, Charset charset) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return new String(bytes, charset);
	}
	
	public static String toString(Object value) {
		return value == null ? null : value.toString();
	}
	
	/**
	 * 获取最后N个字符
	 *
	 * @param source
	 * @param n
	 * @return String
	 */
	public static String lastN(String source, int n) {
		if (source == null) {
			return null;
		}
		return source.substring(source.length() - n);
	}
	
	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, the value of
	 * {@code defaultStr}.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.defaultString(null, "NULL")  = "NULL"
	 * StringUtils.defaultString("", "NULL")    = ""
	 * StringUtils.defaultString("bat", "NULL") = "bat"
	 * </pre>
	 *
	 * @param str        the String to check, may be null
	 * @param defaultStr the default String to return if the input is {@code null}, may be null
	 * @return the passed in String, or the default if it was {@code null}
	 * @see ObjectUtils#toString(Object, String)
	 * @see String#valueOf(Object)
	 */
	public static String defaultString(final String str, final String defaultStr) {
		return str == null ? defaultStr : str;
	}
	
	/**
	 * 采用MessageFormat格式化template, 占位符为: {0}, {1} ...
	 *
	 * @param template
	 * @param values
	 * @return
	 */
	public static String format(final String template, final Object... values) {
		Assert.notNull(template, "template can not be null");
		Assert.notNull(values, "values can not be null!");
		Object[] replacements = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			Object value = values[i];
			if (value instanceof Long) {
				replacements[i] = (value == null ? "" : value.toString());
			} else {
				replacements[i] = value;
			}
		}
		return MessageFormat.format(template, replacements);
	}
	
	/**
	 * 采用Apache commons-lang3的StrSubstitutor格式化模板, 占位符为{name}, {age}...
	 *
	 * @param template
	 * @param params
	 * @return
	 */
	public static String format(final String template, final Map<String, Object> params) {
		Assert.notNull(template, "template can not be null");
		Assert.notNull(params, "params can not be null!");
		
		return StrSubstitutor.replace(template, params);
	}
	
	/**
	 * 用逗号对字符串分割, 每个子串都去除两边空格后返回
	 *
	 * @param s
	 * @return String[]
	 */
	public static String[] split(String s) {
		return split(s, ",");
	}
	
	/**
	 * 用逗号对字符串分割, 每个子串都去除两边空格后返回
	 * @param s
	 * @return List<String>
	 */
	public static List<String> split2List(String s) {
		return split2List(s, ",");
	}
	
	/**
	 * 用spliter对字符串分割, 每个子串都去除两边空格后返回
	 *
	 * @param s
	 * @param spliter
	 * @return String[]
	 */
	public static String[] split(String s, String spliter) {
		if (isBlank(s)) {
			return new String[0];
		}
		
		if (spliter == null) {
			return new String[]{s};
		}
		
		String[] tempResultArr = s.split(spliter);
		String[] resultArr = new String[tempResultArr.length];
		for (int i = 0; i < tempResultArr.length; i++) {
			resultArr[i] = tempResultArr[i].trim();
		}
		
		return resultArr;
	}
	
	/**
	 * 用separator对字符串分割, 每个子串都去除两边空格后返回
	 *
	 * @param s
	 * @param separator
	 * @return
	 */
	public static List<String> split2List(String s, String separator) {
		if (isBlank(s)) {
			return emptyList();
		}
		
		return Splitter.on(separator)
				.trimResults()
				.omitEmptyStrings()
				.splitToList(s);
	}
	
}
