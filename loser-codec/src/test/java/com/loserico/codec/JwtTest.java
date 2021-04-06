package com.loserico.codec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-03-31 11:53
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JwtTest {
	
	@Test
	public void testAlgorithm() {
		//HMAC
		Algorithm algorithm = Algorithm.HMAC256("thisisasceret");
		
		//RSA
		RSAPublicKey publicKey = RsaUtils.publicKey();
		RSAPrivateKey privateKey = RsaUtils.privateKey();
		Algorithm rsaAlgorithm = Algorithm.RSA512(publicKey, privateKey);
	}
	
	@Test
	public void testCreateAndSignToken() {
		Algorithm algorithm = Algorithm.HMAC256("thisisasceret");
		String token = JWT.create()
				.withIssuer("三少爷") //签发人
				.sign(algorithm);
		log.info(token);
		
		RSAPublicKey publicKey = RsaUtils.publicKey();
		RSAPrivateKey privateKey = RsaUtils.privateKey();
		Algorithm rsaAlgorithm = Algorithm.RSA512(publicKey, privateKey);
		token = JWT.create()
				.withIssuer("三少爷") //签发人
				.sign(rsaAlgorithm);
		log.info(token);
	}
	
	@Test
	public void testVerify() {
		Algorithm algorithm = Algorithm.HMAC256("thisisasceret");
		String token = JWT.create()
				.withIssuer("三少爷")
				.withSubject("测试签发JWT Token")
				.withClaim("username", "ricoyu")
				.withClaim("userId", 1)
				.withClaim("salt", UUID.randomUUID().toString())
				.withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
				.sign(algorithm);
		System.out.println(token);
		
		JWTVerifier verifier = JWT.require(algorithm).withIssuer("三少爷").build();
		DecodedJWT decodedJWT = verifier.verify(token);
		DecodedJWT decodedJWT2 = JWT.decode(token);
		Claim name = decodedJWT.getClaim("username");
		assertEquals(name.asString(), "ricoyu");
		assertEquals(decodedJWT.getSubject(), decodedJWT2.getSubject());
	}
	
	@Test
	public void test() {
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5Xnrb7lj5FKV1QgVG9rZW4iLCJzYWx0IjoiMzVlYzI5ZDgtNmE4YS00ZjFmLTkyNTctOTI4NTNhNGQyNzQyIiwiaXNzIjoi5LiJ5bCR54i3IiwiZXhwIjoxNjE3MTcyODI5LCJ1c2VySWQiOjEsInVzZXJuYW1lIjoicmljb3l1In0.xrSyY7nCVAauw_cV1lT7ROy6tR2ZQetqp6AuVA8cem4";
		DecodedJWT decodedJWT = JWT.decode(token);
		String username = decodedJWT.getClaim("username").asString();
		assertEquals(username, "ricoyu");
	}
}
