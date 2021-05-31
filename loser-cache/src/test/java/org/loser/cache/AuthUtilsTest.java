package org.loser.cache;

import com.loserico.cache.auth.AuthUtils;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

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
	
	@Test
	public void testLogin() {
		AuthUtils.login("rico", "1234567890", 4, TimeUnit.SECONDS, null, null, null);
		AuthUtils.login("rico3", "1234567890123", 1, TimeUnit.HOURS, null, null, null);
	}
	
	@Test
	public void test() {
		String logined = AuthUtils.isLogined("rico");
		System.out.println(logined);
	}
	
	@Test
	public void testLogout() {
		AuthUtils.logout("1234567890");
	}
}
