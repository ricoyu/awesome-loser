package com.loserico.json.jsonpath.context;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.internal.ParseContextImpl;

import static com.jayway.jsonpath.internal.Utils.notEmpty;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:24
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ParseContext extends ParseContextImpl{
	
	private Configuration configuration = Configuration.defaultConfiguration();

    @Override
    public DocumentContext parse(String json) {
        notEmpty(json, "json string can not be null or empty");
        Object obj = configuration.jsonProvider().parse(json);
        return new JsonContext(obj, configuration);
    }
}
