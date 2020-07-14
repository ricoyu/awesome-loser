package com.loserico.security.utils;

import com.loserico.common.lang.utils.Assert;
import com.loserico.security.exception.JwtTokenParseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.function.Consumer;

/**
 * 根据JWT Token解析Jwt对象出来
 * <p>
 * Copyright: (C), 2020/5/23 16:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class Jwts {
	
	public static JwtBuilder signingKey(Key key) {
		Assert.notNull(key, "key cannot be null");
		JwtBuilder jwtBuilder = new JwtBuilder(key);
		return jwtBuilder;
	}
	
	public static class JwtBuilder {
		private Key key;
		private Consumer<Throwable> errorConsumer;
		
		private JwtBuilder(Key key) {
			this.key = key;
		}
		
		public JwtBuilder onError(Consumer<Throwable> errorConsumer) {
			this.errorConsumer = errorConsumer;
			return this;
		}
		
		public Jwt<JwsHeader, Claims> parseClaimsJws(String claimsJws) {
			try {
				return io.jsonwebtoken.Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
			} catch (Throwable e) {
				log.error("", e);
				if (errorConsumer != null) {
					errorConsumer.accept(e);
				} else {
					throw new JwtTokenParseException(e);
				}
			}
			
			return null;
		}
	}
}
