package com.loserico.tokenparser.xml;

import com.loserico.tokenparser.io.Resources;
import ognl.DefaultClassResolver;

public class OgnlClassResolver extends DefaultClassResolver {
	
	@Override
	protected Class toClassForName(String className) throws ClassNotFoundException {
		return Resources.classForName(className);
	}
	
}
