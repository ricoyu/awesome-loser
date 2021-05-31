package com.loserico.search.annotation;

import com.loserico.search.enums.Dynamic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Copyright: (C), 2021-05-06 14:09
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Index {
	
	/**
	 * 索引名
	 */
	String value();
	
	/**
	 * 别名
	 */
	String alias() default "";
	
	/**
	 * Mapping设置<p>
	 * 设置索引的dynamic属性: true false strict
	 */
	Dynamic dynamic() default Dynamic.FALSE;
	
	/**
	 * 是否要存储_source
	 */
	boolean sourceEnabled() default true;
	
	/**
	 * 设置主分片数, 默认1
	 */
	int numberOfShards() default 1;
	
	/**
	 * 设置副本数, 默认0
	 */
	int numberOfReplicas() default 0;
	
	/**
	 * 设置这个索引默认的pipeline<p>
	 * index.default_pipeline
	 * <p>
	 * The default ingest node pipeline for this index.<p>
	 * Index requests will fail if the default pipeline is set and the pipeline does not exist.<p>
	 * The default may be overridden using the pipeline parameter.<p>
	 */
	String defaultPipeline() default "";
	
	/**
	 * 在Elasticsearch节点上可以配置node.attr.my_node_type: hot<p>
	 * 将节点配置成Hot或者Warm节点, 然后在设置索引的时候, 可以指定这个索引需要存放到Hot还是Warm节点上<p>
	 * <pre>
	 * PUT logs-2019-06-27
	 * {
	 *   "settings": {
	 *     "num_of_shards": 3,
	 *     "num_of_replicas": 1
	 *     "index.routing.allocation.require.my_node_type": "hot"
	 *   }
	 * }
	 * </pre>
	 * <p>
	 * 这个indexRouting格式为my_node_type=hot
	 */
	String indexRouting() default "";
	
	/**
	 * 是否要将索引设置为只读
	 */
	boolean blocksWrite() default false;
}
