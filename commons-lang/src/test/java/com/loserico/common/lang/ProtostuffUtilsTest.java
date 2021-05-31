package com.loserico.common.lang;

import com.loserico.common.lang.utils.ProtostuffUtils;
import lombok.Data;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2020-10-13 10:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ProtostuffUtilsTest {
	
	@Test
	public void test() {
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
		System.out.println(grantedAuthority);
		byte[] bytes = ProtostuffUtils.toBytes(grantedAuthority);
		SimpleGrantedAuthority grantedAuthority1 = ProtostuffUtils.toObject(bytes, SimpleGrantedAuthority.class);
		System.out.println(grantedAuthority1);
		Object object = ProtostuffUtils.toObject(bytes, SimpleGrantedAuthority.class);
		System.out.println(object);
	}
	
	static class SimpleGrantedAuthority  {
		
		private final String role;
		
		private List<Resources> resources = new ArrayList<>();
		
		public SimpleGrantedAuthority(String role) {
			Assert.hasText(role, "A granted authority textual representation is required");
			this.role = role;
			resources.add(new Resources("rico"));
		}
		
		public String getAuthority() {
			return role;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			
			if (obj instanceof SimpleGrantedAuthority) {
				return role.equals(((SimpleGrantedAuthority) obj).role);
			}
			
			return false;
		}
		
		@Override
		public int hashCode() {
			return this.role.hashCode();
		}
		
		@Override
		public String toString() {
			return this.role + resources;
		}
	}
	
	@Data
	static class Resources {
		
		private String name;
		
		public Resources(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
}
