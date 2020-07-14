package com.loserico.jwt;

import com.loserico.codec.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.util.HashMap;

/**
 * <p>
 * Copyright: (C), 2020/5/21 19:35
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JwtUtilsTest {
	
	@Test
	public void test() {
		// 2.设置用户信息
		HashMap<String, Object> claims = new HashMap<>(8);
		claims.put("id", "1");
		claims.put("userName", "张三");
		
		// 生成jwt
		String jwt = JwtUtils.generateJwt(claims);
		// 会生成类似该字符串的内容:  aaaa.bbbb.cccc
		log.info("生成的jwt:{}", jwt);
		
		//校验jwt
       Boolean validateFlag = JwtUtils.validateJwt(jwt);
       log.info("校验jwt结果:{}",validateFlag);
		
		//异常校验jwt
		//Boolean validateFlag2 = JwtUtils.validateJwt(jwt + 2);
		//log.info("校验jwt结果:{}", validateFlag2);
		
		//解析jwt的头部信息
		String jwtHeader = jwt.split("\\.")[0];
		log.info("头部信息:{}", jwtHeader);
		byte[] header = Base64.decodeBase64(jwtHeader.getBytes());
		log.info("解密头部信息:{}", new String(header));
		
		String pload = jwt.split("\\.")[1];
		log.info("pload信息:{}", pload);
		byte[] jwtPload = Base64.decodeBase64(pload.getBytes());
		log.info("解密头部信息:{}", new String(jwtPload));
		
		
	}
}
