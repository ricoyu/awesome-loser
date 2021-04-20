package com.loserico.searchlegacy.builder;

import com.loserico.searchlegacy.ElasticUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.context.CategoryQueryContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * 基于上下文的自动完成
 * <p>
 * Copyright: (C), 2021-02-16 9:33
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class ElasticContextSuggestBuilder {
	
	/**
	 * 索引名
	 */
	private String[] indices;
	
	/**
	 * 要在哪个字段上实现自动完成
	 */
	private String field;
	
	/**
	 * 用户的输入, 前缀匹配
	 */
	private String prefix;
	
	/**
	 * 要实现基于上下文的自动完成, 用户需要先为Index建立Mapping
	 * 下面这个Mapping设置了字段comment_autocomplete提供上下文的自动完成
	 * 其type是category
	 * <pre>
	 * {
	 *   "properties": {
	 *     "comment_autocomplete": {
	 *       "type": "completion",
	 *       "contexts": [
	 *         {
	 *           "type": "category",
	 *           "name": "comment_category"
	 *         }
	 *       ]
	 *     }
	 *   }
	 * }
	 * </pre>
	 * <p>
	 * 下面插入两篇文档, 一篇category设为movies, 一篇coffee
	 * <pre>
	 * {
	 *   "comment": "I love the star war movies",
	 *   "comment_autocomplete": {
	 *     "input": [
	 *       "star wars"
	 *     ],
	 *     "contexts": {
	 *       "comment_category": "movies"
	 *     }
	 *   }
	 * }
	 * </pre>
	 * <p>
	 * {
	 *   "comment": "Where can I find a Starbucks",
	 *   "comment_autocomplete": {
	 *     "input": [
	 *       "starbucks"
	 *     ],
	 *     "contexts": {
	 *       "comment_category": "coffee"
	 *     }
	 *   }
	 * }
	 * <p>
	 * 这里category属性就对应movies和coffee这两个值
	 */
	private String category;
	
	private String categoryName;
	
	/**
	 * 这个suggest的名字
	 */
	private String name;
	
	public ElasticContextSuggestBuilder(String... indices) {
		this.indices = indices;
	}
	
	/**
	 * 这个suggest的名字, 必须提供
	 *
	 * @param name
	 * @return
	 */
	public ElasticContextSuggestBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * 要实现基于上下文的自动完成, 用户需要先为Index建立Mapping<p/>
	 * 下面这个Mapping设置了字段comment_autocomplete提供上下文的自动完成
	 * 其type是category
	 * <pre>
	 * {
	 *   "properties": {
	 *     "comment_autocomplete": {
	 *       "type": "completion",
	 *       "contexts": [{
	 *         "type": "category",
	 *         "name": "comment_category"
	 *       }]
	 *     }
	 *   }
	 * }
	 * </pre>
	 * 下面插入两篇文档, 一篇category设为movies, 一篇coffee
	 * <pre>
	 * {
	 *   "comment": "I love the star war movies",
	 *   "comment_autocomplete": {
	 *     "input": ["star wars"],
	 *     "contexts": {
	 *       "comment_category": "movies"
	 *     }
	 *   }
	 * }
	 * </pre>
	 * <pre>
	 * {
	 *   "comment": "Where can I find a Starbucks",
	 *   "comment_autocomplete": {
	 *     "input": ["starbucks"],
	 *     "contexts": {
	 *       "comment_category": "coffee"
	 *     }
	 *   }
	 * }
	 * </pre>
	 * <p>
	 * 这里category属性就对应movies和coffee这两个值
	 *
	 * @param category
	 * @return
	 */
	public ElasticContextSuggestBuilder category(String category) {
		this.category = category;
		return this;
	}
	
	/**
	 * 定义的Mapping中context的名字, 比如comment_category
	 * <pre>
	 * {
	 *   "properties": {
	 *     "comment_autocomplete": {
	 *       "type": "completion",
	 *       "contexts": [{
	 *         "type": "category",
	 *         "name": "comment_category"
	 *       }]
	 *     }
	 *   }
	 * }
	 * </pre>
	 * 因为一个字段可以指定多个context, 所以这里要指定具体哪一个context
	 *
	 * @param categoryName
	 * @return
	 */
	public ElasticContextSuggestBuilder categoryName(String categoryName) {
		this.categoryName = categoryName;
		return this;
	}
	
	/**
	 * 用户的输入, 前缀匹配
	 *
	 * @param prefix
	 * @return
	 */
	public ElasticContextSuggestBuilder prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	/**
	 * 要实现自动完成的字段, Mapping中type是completion
	 *
	 * @param field
	 * @return
	 */
	public ElasticContextSuggestBuilder field(String field) {
		this.field = field;
		return this;
	}
	
	/**
	 * 执行查询, 返回suggest信息
	 *
	 * @return Set<String>
	 */
	public Set<String> suggest() {
		Map<String, List<? extends ToXContent>> contexts = Collections.singletonMap(categoryName,
				asList(CategoryQueryContext.builder()
						.setCategory(category)
						.build()));
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion(field)
				.prefix(prefix)
				.contexts(contexts);
		
		return ElasticUtils.suggest(indices)
				.suggestionBuilder(completionSuggestionBuilder)
				.name(name)
				.suggest();
	}
}
