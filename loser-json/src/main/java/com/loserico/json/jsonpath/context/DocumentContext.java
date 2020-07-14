package com.loserico.json.jsonpath.context;

import java.lang.reflect.Type;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:23
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface DocumentContext extends com.jayway.jsonpath.DocumentContext {

    <T> T read(String path, Type type);

}
