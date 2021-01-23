package com.loserico.json.jsonpath.mapper;
import com.jayway.jsonpath.Configuration;

import java.lang.reflect.Type;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2019-10-15 9:28
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface LoserMappingProvider extends com.jayway.jsonpath.spi.mapper.MappingProvider{


    /**
     *
     * @param source object to map
     * @param targetType the type the source object should be mapped to
     * @param configuration current configuration
     * @param <T> the mapped result type
     * @return return the mapped object
     */
    @Override
    <T> T map(Object source, Class<T> targetType, Configuration configuration);

    /**
     *
     * @param source object to map
     * @param targetType the type the source object should be mapped to
     * @param configuration current configuration
     * @param <T> the mapped result type
     * @return return the mapped object
     */
    <T> T map(Object source, Type targetType, Configuration configuration);
}
