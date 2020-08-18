package com.loserico.jackson.grantedAuthorityMixIn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loserico.jackson.grantedAuthorityMixIn.mixin.GrantedAuthorityMixIn;
import com.loserico.jackson.grantedAuthorityMixIn.mixin.SimpleGrantedAuthorityMixIn;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Copyright: (C), 2020-08-14 16:28
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class GrantedAuthoritySerializeTest {
	
	@SneakyThrows
	public static void main(String[] args) {
		SysUserDetails sysUserDetails = new SysUserDetails("1",
				"ricoyu",
				"123456",
				"三少爷",
				true,
				true,
				true,
				true,
				Collections.emptyList());
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ADMIN"));
		authorities.add(new SimpleGrantedAuthority("USER"));
		sysUserDetails.setAuthorities(authorities);
		Set<String> permissions = new HashSet<>();
		permissions.add("resource1");
		permissions.add("resource2");
		sysUserDetails.setPermissions(permissions);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.addMixIn(GrantedAuthority.class, GrantedAuthorityMixIn.class);
		mapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixIn.class);
		
		System.out.println(mapper.writeValueAsString(sysUserDetails));
		
		//String json = "{\"userId\":\"1\",\"username\":\"ricoyu\",\"password\":\"123456\",\"nickname\":\"三少爷\",\"authorities\":[{\"@class\":\"com.loserico.jackson.grantedAuthorityMixIn.SimpleGrantedAuthority\",\"authority\":\"ADMIN\"},{\"@class\":\"com.loserico.jackson.grantedAuthorityMixIn.SimpleGrantedAuthority\",\"authority\":\"USER\"}],\"permissions\":[\"resource2\",\"resource1\"],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true}";
		String json = "{\"userId\":\"1\",\"username\":\"admin\",\"password\":null,\"nickname\":\"管理员\",\"authorities\":[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"admin\",\"role\":\"admin\"}],\"permissions\":[\"/secure/resource1\",\"/secure/resource2\"],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true}";
		SysUserDetails userDetails = mapper.readValue(json, SysUserDetails.class);
		System.out.println(userDetails.getNickname());
		userDetails.getAuthorities().forEach((grantedAuthority) -> {
			System.out.println(grantedAuthority.getAuthority());
		});
	}
}
