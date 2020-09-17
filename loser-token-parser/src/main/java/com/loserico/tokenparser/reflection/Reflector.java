package com.loserico.tokenparser.reflection;

import java.lang.reflect.ReflectPermission;

/**
 * <p>
 * Copyright: (C), 2020-09-16 10:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Reflector {
	
	public static boolean canControlMemberAccessible() {
		try {
			SecurityManager securityManager = System.getSecurityManager();
			if (null != securityManager) {
				securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
			}
		} catch (SecurityException e) {
			return false;
		}
		return true;
	}
}
