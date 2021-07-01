package org.loser.cache;

import com.loserico.cache.auth.AuthUtils;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * <p>
 * Copyright: (C), 2021-05-28 10:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AuthUtilsTest {
	
	@SneakyThrows
	@Test
	public void testSingleSignLogin() {
		//第一次登录
		boolean success = AuthUtils.login("rico", "token1", 50, TimeUnit.SECONDS, null, null, null);
		//在别处登录, 踢掉前一个
		success = AuthUtils.login("rico", "token2", 50, TimeUnit.HOURS, null, null, null);
		
		//rico是登录着的
		boolean logined = AuthUtils.isLogined("rico");
		assertTrue(logined);
		
		//token1已经被踢掉了, 所以是拿不到对应的username的
		String username = AuthUtils.auth("token1");
		assertNull(username);
		
		//token2此时是有效的
		username = AuthUtils.auth("token2");
		assertEquals(username, "rico");
		
		//登出token2
		boolean logout = AuthUtils.logout("token2");
		assertTrue(logout);
		
		success = AuthUtils.login("rico", "token3", 2, TimeUnit.SECONDS, null, null, null);
		assertTrue(success);
		
		TimeUnit.SECONDS.sleep(2);
		username = AuthUtils.auth("token3");
		assertNull(username);
		logined = AuthUtils.isLogined("rico");
		assertFalse(logined);
	}
	
	@Test
	public void testLogout() {
		boolean success = AuthUtils.login("rico", "token001", 50, TimeUnit.SECONDS, null, null, null);
		boolean logined = AuthUtils.isLogined("user");
		assertTrue(true);
		boolean logout = AuthUtils.logout("token001");
		assertTrue(logout);
		logined = AuthUtils.isLogined("rico");
		assertFalse(logined);
	}
	
	@SneakyThrows
	@Test
	public void testClearExpired() {
		boolean success = AuthUtils.login("rico", "token001", 3, TimeUnit.SECONDS, null, null, null);
		TimeUnit.SECONDS.sleep(3);
		AuthUtils.clearExpired();
		boolean logined = AuthUtils.isLogined("rico");
		assertFalse(logined);
	}
	
	@SneakyThrows
	@Test
	public void testMultiLogin() {
		boolean success = AuthUtils.login("rico", 
				"token001", 
				4, TimeUnit.SECONDS, 
				null, 
				null, 
				null, 
				false);
		assertTrue(success);
		success = AuthUtils.login("rico",
				"token002",
				4, TimeUnit.SECONDS,
				null,
				null,
				null,
				false);
		assertTrue(success);
		boolean logined = AuthUtils.isLogined("rico");
		assertTrue(logined);
		String username1 = AuthUtils.auth("token001");
		String username2 = AuthUtils.auth("token002");
		assertEquals(username1, username2);
		TimeUnit.SECONDS.sleep(4);
		logined = AuthUtils.isLogined("rico");
		assertFalse(logined);
	}
	
	@SneakyThrows
	@Test
	public void testTokenExpires() {
		AuthUtils.login("ricoyu", "asdasd", 2L, TimeUnit.MINUTES, null, null, null);
		TimeUnit.MINUTES.sleep(2);
		boolean logined = AuthUtils.isLogined("username");
		assertFalse(logined);
	}
	
	public static void main(String[] args) {
		System.out.println(new Date(1624937346000L));
	}
	
}
