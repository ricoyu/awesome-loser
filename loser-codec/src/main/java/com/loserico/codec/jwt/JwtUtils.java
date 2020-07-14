package com.loserico.codec.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
public final class JwtUtils {
	
	/**
	 * 秘钥
	 */
	private final static String SECRET =
			"smlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlz";
	/**
	 * 有效期，单位秒
	 */
	private final static Long expirationTimeInSecond = 3600L;
	
	/**
	 * 从jwt中解析Claims(Hashmap)里面封装的用户信息
	 *
	 * @param token
	 * @return Claims
	 */
	public static Claims getClaimsFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
			log.error("token解析错误", e);
			throw new IllegalArgumentException("Token invalided.");
		}
	}
	
	/**
	 * 获取jwt的过期时间
	 *
	 * @param token
	 * @return Date
	 */
	public static Date getExpirationDateFromToken(String token) {
		return getClaimsFromToken(token).getExpiration();
	}
	
	/**
	 * 判断jwt是否过期
	 *
	 * @param token jwt令牌
	 * @return true 表示过期, false 没有过期
	 */
	private static Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	/**
	 * 计算过期时间
	 *
	 * @return Date
	 */
	private static Date getExpirationTime() {
		return new Date(System.currentTimeMillis() + expirationTimeInSecond * 1000);
	}
	
	/**
	 * 指定用户生成token
	 * 支持的算法详见：https://github.com/jwtk/jjwt#features
	 *
	 * @param claims 用户信息
	 * @return String jwt Token
	 */
	public static String generateJwt(Map<String, Object> claims) {
		Date createdTime = new Date();
		Date expirationTime = getExpirationTime();
		
		
		byte[] keyBytes = SECRET.getBytes();
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(createdTime)
				.setExpiration(expirationTime)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	/**
	 * 校验token
	 *
	 * @param token jwt
	 * @return 未过期返回true, 否则返回false
	 */
	public static Boolean validateJwt(String token) {
		return !isTokenExpired(token);
	}
	
}