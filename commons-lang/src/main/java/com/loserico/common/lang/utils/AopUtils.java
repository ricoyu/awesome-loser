package com.loserico.common.lang.utils;
import org.springframework.aop.framework.Advised;
import org.springframework.util.Assert;

import static org.springframework.aop.support.AopUtils.isAopProxy;

/**
 * {@code AopTestUtils} is a collection of AOP-related utility methods for
 * use in unit and integration testing scenarios.
 *
 * <p>For Spring's core AOP utilities, see
 *
 * @author Sam Brannen
 * @since 4.2
 * @see ReflectionUtils
 */
public class AopUtils {

	/**
	 * Get the <em>target</em> object of the supplied {@code candidate} object.
	 * <p>If the supplied {@code candidate} is a Spring proxy, the target of the proxy will
	 * be returned; otherwise, the {@code candidate} will be returned
	 * <em>as is</em>.
	 * @param candidate the instance to check (potentially a Spring AOP proxy);
	 * never {@code null}
	 * @return the target object or the {@code candidate}; never {@code null}
	 * @throws IllegalStateException if an error occurs while unwrapping a proxy
	 * @see Advised#getTargetSource()
	 * @see #getUltimateTargetObject
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getTargetObject(Object candidate) {
		Assert.notNull(candidate, "candidate must not be null");
		try {
			if (isAopProxy(candidate) && (candidate instanceof Advised)) {
				return (T) ((Advised) candidate).getTargetSource().getTarget();
			}
		}
		catch (Exception ex) {
			throw new IllegalStateException("Failed to unwrap proxied object", ex);
		}
		return (T) candidate;
	}

	/**
	 * Get the ultimate <em>target</em> object of the supplied {@code candidate}
	 * object, unwrapping not only a top-level proxy but also any number of
	 * nested proxies.
	 * <p>If the supplied {@code candidate} is a Spring proxy, the ultimate target of all
	 * nested proxies will be returned; otherwise, the {@code candidate}
	 * will be returned <em>as is</em>.
	 * @param candidate the instance to check (potentially a Spring AOP proxy);
	 * never {@code null}
	 * @return the ultimate target object or the {@code candidate}; never
	 * {@code null}
	 * @throws IllegalStateException if an error occurs while unwrapping a proxy
	 * @see Advised#getTargetSource()
	 * @see org.springframework.aop.framework.AopProxyUtils#ultimateTargetClass
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getUltimateTargetObject(Object candidate) {
		Assert.notNull(candidate, "candidate must not be null");
		try {
			if (isAopProxy(candidate) && (candidate instanceof Advised)) {
				return (T) getUltimateTargetObject(((Advised) candidate).getTargetSource().getTarget());
			}
		}
		catch (Exception ex) {
			throw new IllegalStateException("Failed to unwrap proxied object", ex);
		}
		return (T) candidate;
	}

}