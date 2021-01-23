package com.loserico.json.jsonpath.context;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.spi.cache.Cache;
import com.jayway.jsonpath.spi.cache.CacheProvider;
import com.loserico.json.jsonpath.mapper.LoserMappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import static com.jayway.jsonpath.JsonPath.compile;
import static com.jayway.jsonpath.internal.Utils.notEmpty;
import static com.jayway.jsonpath.internal.Utils.notNull;
import static java.util.Arrays.asList;

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
public class JsonContext implements DocumentContext {

    private static final Logger logger = LoggerFactory.getLogger(JsonContext.class);

    private final Configuration configuration;
    private final Object json;

    public JsonContext(Object json, Configuration configuration) {
        notNull(json, "json can not be null");
        notNull(configuration, "configuration can not be null");
        this.configuration = configuration;
        this.json = json;
    }


    @Override
    public Configuration configuration() {
        return configuration;
    }

    @Override
    public Object json() {
        return json;
    }

    @Override
    public String jsonString() {
        return configuration.jsonProvider().toJson(json);
    }

    @Override
    public <T> T read(String path, Predicate... filters) {
        notEmpty(path, "path can not be null or empty");
        Cache cache = CacheProvider.getCache();

        path = path.trim();
        LinkedList filterStack = new LinkedList<Predicate>(asList(filters));
        String cacheKey = Utils.concat(path, filterStack.toString());

        JsonPath jsonPath = cache.get(cacheKey);
        if(jsonPath != null){
        	return read(jsonPath);
        } else {
		jsonPath = compile(path, filters);
		cache.put(cacheKey, jsonPath);
        	return read(jsonPath);
        }

    }

    @Override
    public <T> T read(String path, Class<T> type, Predicate... filters) {
        return convert(read(path, filters), type, configuration);
    }

    @Override
    public <T> T read(JsonPath path) {
        notNull(path, "path can not be null");
        return path.read(json, configuration);
    }

    @Override
    public <T> T read(JsonPath path, Class<T> type) {
        return convert(read(path), type, configuration);
    }

    @Override
    public <T> T read(JsonPath path, TypeRef<T> type) {
        return convert(read(path), type, configuration);
    }

    @Override
    public <T> T read(String path, TypeRef<T> type) {
        return convert(read(path), type, configuration);
    }

	@Override
	public <T> T read(String path, Type type) {
		return ((LoserMappingProvider)configuration.mappingProvider()).map(read(path), type, configuration);
	}
	
    @Override
    public ReadContext limit(int maxResults){
        return withListeners(new LimitingEvaluationListener(maxResults));
    }

    @Override
    public ReadContext withListeners(EvaluationListener... listener){
        return new JsonContext(json, configuration.setEvaluationListeners(listener));
    }


    private <T> T convert(Object obj, Class<T> targetType, Configuration configuration){
        return configuration.mappingProvider().map(obj, targetType, configuration);
    }

    private <T> T convert(Object obj, TypeRef<T> targetType, Configuration configuration){
        return configuration.mappingProvider().map(obj, targetType, configuration);
    }

    @Override
    public DocumentContext set(String path, Object newValue, Predicate... filters) {
        return set(compile(path, filters), newValue);
    }

    @Override
    public DocumentContext set(JsonPath path, Object newValue){
        List<String> modified = path.set(json, newValue, configuration.addOptions(Option.AS_PATH_LIST));
        if(logger.isDebugEnabled()){
            for (String p : modified) {
                logger.debug("Set path {} new value {}", p, newValue);
            }
        }
        return this;
    }

    @Override
    public DocumentContext map(String path, MapFunction mapFunction, Predicate... filters) {
        map(compile(path, filters), mapFunction);
        return this;
    }

    @Override
    public DocumentContext map(JsonPath path, MapFunction mapFunction) {
        path.map(json, mapFunction, configuration);
        return this;
    }

    @Override
    public DocumentContext delete(String path, Predicate... filters) {
        return delete(compile(path, filters));
    }

    @Override
    public DocumentContext delete(JsonPath path) {
        List<String> modified = path.delete(json, configuration.addOptions(Option.AS_PATH_LIST));
        if(logger.isDebugEnabled()){
            for (String p : modified) {
                logger.debug("Delete path {}");
            }
        }
        return this;
    }

    @Override
    public DocumentContext add(String path, Object value, Predicate... filters){
        return add(compile(path, filters), value);
    }

    @Override
    public DocumentContext add(JsonPath path, Object value){
        List<String> modified =  path.add(json, value, configuration.addOptions(Option.AS_PATH_LIST));
        if(logger.isDebugEnabled()){
            for (String p : modified) {
                logger.debug("Add path {} new value {}", p, value);
            }
        }
        return this;
    }

    @Override
    public DocumentContext put(String path, String key, Object value, Predicate... filters){
        return put(compile(path, filters), key, value);
    }

    @Override
    public DocumentContext renameKey(String path, String oldKeyName, String newKeyName, Predicate... filters) {
        return renameKey(compile(path, filters), oldKeyName, newKeyName);
    }

    @Override
    public DocumentContext renameKey(JsonPath path, String oldKeyName, String newKeyName) {
        List<String> modified =  path.renameKey(json, oldKeyName, newKeyName, configuration.addOptions(Option.AS_PATH_LIST));
        if(logger.isDebugEnabled()){
            for (String p : modified) {
                logger.debug("Rename path {} new value {}", p, newKeyName);
            }
        }
        return this;
    }


    @Override
    public DocumentContext put(JsonPath path, String key, Object value){
        List<String> modified = path.put(json, key, value, configuration.addOptions(Option.AS_PATH_LIST));
        if(logger.isDebugEnabled()){
            for (String p : modified) {
                logger.debug("Put path {} key {} value {}", p, key, value);
            }
        }
        return this;
    }

    private final static class LimitingEvaluationListener implements EvaluationListener {
        final int limit;

        private LimitingEvaluationListener(int limit) {
            this.limit = limit;
        }

        @Override
        public EvaluationContinuation resultFound(FoundResult found) {
            if(found.index() == limit - 1){
                return EvaluationContinuation.ABORT;
            } else {
                return EvaluationContinuation.CONTINUE;
            }
        }
    }
}
