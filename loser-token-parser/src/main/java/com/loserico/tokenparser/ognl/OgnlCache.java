package com.loserico.tokenparser.ognl;

import com.loserico.tokenparser.exception.BuilderException;
import com.loserico.tokenparser.xml.OgnlClassResolver;
import ognl.Ognl;
import ognl.OgnlException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * Copyright: Copyright (c) 2020-09-16 10:56
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public final class OgnlCache {
	
	private static final OgnlMemberAccess MEMBER_ACCESS = new OgnlMemberAccess();
	private static final OgnlClassResolver CLASS_RESOLVER = new OgnlClassResolver();
	private static final Map<String, Object> expressionCache = new ConcurrentHashMap<>();
	
	private OgnlCache() {
		// Prevent Instantiation of Static Class
	}
	
	public static Object getValue(String expression, Object root) {
		try {
			Map context = Ognl.createDefaultContext(root, MEMBER_ACCESS, CLASS_RESOLVER, null);
			return Ognl.getValue(parseExpression(expression), context, root);
		} catch (OgnlException e) {
			throw new BuilderException("Error evaluating expression '" + expression + "'. Cause: " + e, e);
		}
	}
	
	private static Object parseExpression(String expression) throws OgnlException {
		Object node = expressionCache.get(expression);
		if (node == null) {
			node = Ognl.parseExpression(expression);
			expressionCache.put(expression, node);
		}
		return node;
	}
	
}
