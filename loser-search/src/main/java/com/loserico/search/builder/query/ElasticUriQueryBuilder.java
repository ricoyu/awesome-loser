package com.loserico.search.builder.query;

import com.loserico.common.lang.resource.PropertyReader;
import com.loserico.common.lang.utils.StringUtils;
import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.networking.utils.HttpUtils;
import com.loserico.search.enums.Direction;
import com.loserico.search.exception.UriQueryException;
import com.loserico.search.support.RestSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.loserico.common.lang.utils.Assert.notEmpty;
import static com.loserico.common.lang.utils.Assert.notNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * URI Query 接收的查询参数
 * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-search.html#search-search-api-query-params
 * <p>
 * Copyright: (C), 2021-04-28 17:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class ElasticUriQueryBuilder {
	private static final String USERNAME = "elastic.username";
	private static final String PASSWORD = "elastic.password";
	
	private static final String DF = "df=";
	private static final String SORT = "sort=";
	private static final String FROM = "from=";
	private static final String SIZE = "size=";
	
	
	/**
	 * 默认读取classpath下elastic.properties文件
	 */
	private static PropertyReader propertyReader = new PropertyReader("elastic");
	
	private static String username = propertyReader.getString(USERNAME);
	private static String password = propertyReader.getString(PASSWORD);
	
	/**
	 * 要查询的索引, 可以:<ol>
	 * <li/>指定单个索引 rico
	 * <li/>指定多个索引 rico,users
	 * <li/>指定通配符   test*
	 * </ol>
	 */
	private String index;
	
	/**
	 * 指定查询语句, 使用Query String Syntax
	 * 有多种查询语法
	 * <ul>
	 * <li/>df查询 GET movies/_search?q=2012&df=title
	 * <li/>指定字段查询 GET movies/_search?q=title:2012
	 * <li/>泛查询 GET movies/_search?q=2012              会对文档中所有字段进行查询
	 * <li/>GET movies/_search?q=title:Beautiful Mind     与下面的等价
	 * <li/>GET movies/_search?q=title:Beautiful OR Mind
	 * <li/>Pahrase Query(引号引起来的) GET movies/_search?q=title:"Beautiful Mind"  表示Beautiful Mind要同时出现并且按照规定的顺序, 与下面的等价
	 * <li/>GET movies/_search?q=title:Beautiful AND Mind
	 * <li/>分组 GET movies/_search?q=title:(Beautiful Mind)
	 * <li/>GET movies/_search?q=title:(Beautiful NOT Mind)  包含Beautiful 不包含 Mind
	 * <li/>GET movies/_search?q=title:(Beautiful %2BMind)   必须包含Mind, %2B是 + 号的转义字符
	 * <li/>范围查询 GET movies/_search?q=year:>1980
	 * <li/>GET movies/_search?q=2012&df=title&sort=year:desc&from=0&size=10&timeout=1s
	 * </ul>
	 */
	private String q;
	
	/**
	 * df是默认字段, 如果q没有指定字段, 那么就查询df指定的字段
	 * 要查询的字段, 不指定时, 会对所有字段进行查询<p>
	 * q=2012&df=title, 即查询title包含2012的文档
	 */
	private String df;
	
	/**
	 * GET movies/_search?q=2012&df=title&sort=year:desc&from=0&size=10&timeout=1s<p>
	 * <p>
	 * 参考上面的查询, sort语法是 字段名:asc|desc
	 */
	private String sort;
	
	/**
	 * 分页, 页码
	 */
	private Integer from;
	
	/**
	 * 分页, 每页大小
	 */
	private Integer size;
	
	/**
	 * 是否要返回source, 默认_source=true
	 */
	private boolean fetchSource = true;
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 * _source_excludes 参数, 多个值逗号隔开
	 */
	private String[] excludeSources;
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 * _source_includes 参数, 多个值逗号隔开
	 */
	private String[] includeSources;
	
	/**
	 * 查询返回的结果类型
	 */
	private Class resultType;
	
	public ElasticUriQueryBuilder(String index) {
		notNull(index, "index cannot be null!");
		this.index = index;
	}
	
	/**
	 * 指定查询语句, 使用Query String Syntax<p>
	 * 有多种查询语法
	 * <ul>
	 * <li/>df查询      GET movies/_search?q=2012&df=title
	 * <li/>指定字段查询 GET movies/_search?q=title:2012
	 * <li/>泛查询      GET movies/_search?q=2012              会对文档中所有字段进行查询
	 * <li/>Mind会在所有字段上查询  GET movies/_search?q=title:Beautiful Mind     与下面的等价
	 * <li/>Mind会在所有字段上查询  GET movies/_search?q=title:Beautiful OR Mind
	 * <li/>Pahrase Query(引号引起来的) GET movies/_search?q=title:"Beautiful Mind"  表示Beautiful Mind要同时出现并且按照规定的顺序
	 * <li/>                          GET movies/_search?q=title:Beautiful AND Mind
	 * <li/>分组        GET movies/_search?q=title:(Beautiful Mind)
	 * <li/>           GET movies/_search?q=title:(Beautiful NOT Mind)  包含Beautiful 不包含 Mind
	 * <li/>           GET movies/_search?q=title:(Beautiful %2BMind)   必须包含Mind, %2B是 + 号的转义字符
	 * <li/>范围查询    GET movies/_search?q=year:>1980
	 * </ul>
	 * 你可以就写查询条件: 2012<p>
	 * 也可以写完成的查询: q=2012 或者 q=2012&df=title 等<p>
	 *
	 * @param q
	 * @return QueryStringQueryBuilder
	 */
	public ElasticUriQueryBuilder query(String q) {
		notEmpty(q, "q cannot be empty");
		if (!q.startsWith("q=")) {
			q = "q=" + q;
		}
		this.q = q;
		return this;
	}
	
	/**
	 * 执行类似 GET movies/_search?q="Beautiful Mind" 的Phrase query
	 * 这里Phrase Query参数Beautiful Mind不需要传双引号, 会自动帮你加上
	 * <li/>Pahrase Query(引号引起来的) GET movies/_search?q=title:"Beautiful Mind"  表示Beautiful Mind要同时出现并且按照规定的顺序
	 * <li/>                          GET movies/_search?q=title:Beautiful AND Mind
	 *
	 * @param q
	 * @return QueryStringQueryBuilder
	 */
	public ElasticUriQueryBuilder phraseQuery(String q) {
		notEmpty(q, "q cannot be empty");
		if (!q.startsWith("q=")) {
			/*
			 * 如果q传的是title:Beautiful Mind
			 * 那么要拿到Beautiful Mind部分, 两边套上双引号
			 * 否则整个串套双引号
			 */
			int colonIndex = q.indexOf(":");
			if (colonIndex == -1) {
				q = "q=" + "\"" + q + "\"";
			} else {
				String phrase = q.substring(colonIndex+1);
				String field = q.substring(0, colonIndex);
				q = "q="+field+":\""+phrase+"\"";
			}
		} else {
			String phrase = q.substring(2);
			phrase = "q=" + "\"" + phrase + "\"";
		}
		this.q = q;
		return this;
	}
	
	/**
	 * 要查询的字段, 不指定时, 会对所有字段进行查询<p>
	 * 比如这样一个查询 q=2012&df=title 表示查询title包含2012的文档
	 * <p>
	 * 这里设置的参数df就表示要查询哪个字段, 如 title
	 *
	 * @param df
	 * @return UriQueryBuilder
	 */
	public ElasticUriQueryBuilder field(String df) {
		notEmpty(df, "df cannot be empty!");
		this.df = df;
		return this;
	}
	
	/**
	 * 要查询的字段, 不指定时, 会对所有字段进行查询<p>
	 * 比如这样一个查询 q=2012&df=title 表示查询title包含2012的文档
	 * <p>
	 * 这里设置的参数df就表示要查询哪个字段, 如 title
	 *
	 * @param df
	 * @return UriQueryBuilder
	 * @alias field
	 */
	public ElasticUriQueryBuilder df(String df) {
		notEmpty(df, "df cannot be empty!");
		this.df = df;
		return this;
	}
	
	/**
	 * GET movies/_search?q=2012&df=title&sort=year:desc&from=0&size=10&timeout=1s<p>
	 * <p>
	 * 参考上面的查询, sort语法是 字段名:asc|desc
	 *
	 * @param sort
	 * @return
	 */
	public ElasticUriQueryBuilder sort(String sort) {
		notNull(sort, "sort cannot be null!");
		this.sort = sort;
		return this;
	}
	
	/**
	 * 设置分页参数
	 *
	 * @param from
	 * @param size
	 * @return QueryStringQueryBuilder
	 */
	public ElasticUriQueryBuilder page(Integer from, Integer size) {
		notNull(from, "from cannot be null!");
		notNull(size, "size cannot be null!");
		this.from = from;
		this.size = size;
		return this;
	}
	
	/**
	 * 默认查询只返回前10条, 这里指定返回的数据量
	 *
	 * @param size
	 * @return QueryStringQueryBuilder
	 */
	public ElasticUriQueryBuilder size(Integer size) {
		notNull(size, "size cannot be null!");
		this.size = size;
		return this;
	}
	
	/**
	 * GET movies/_search?q=2012&df=title&sort=year:desc&from=0&size=10&timeout=1s<p>
	 * <p>
	 * 参考上面的查询, sort语法是 字段名:asc|desc
	 *
	 * @param direction
	 * @return UriQueryBuilder
	 */
	public ElasticUriQueryBuilder sort(String field, Direction direction) {
		notNull(field, "field cannot be null!");
		notNull(direction, "direction cannot be null!");
		this.sort = field + ":" + direction;
		return this;
	}
	
	/**
	 * 是否要获取_source
	 *
	 * @param fetchSource
	 * @return UriQueryBuilder
	 */
	public ElasticUriQueryBuilder fetchSource(boolean fetchSource) {
		this.fetchSource = fetchSource;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticUriQueryBuilder
	 */
	public ElasticUriQueryBuilder includeSources(String... fields) {
		this.includeSources = fields;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticUriQueryBuilder
	 */
	public ElasticUriQueryBuilder excludeSources(String... fields) {
		this.excludeSources = fields;
		return this;
	}
	
	/**
	 * 控制返回自己想要的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticUriQueryBuilder
	 */
	public ElasticUriQueryBuilder includeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.excludeSources = sources;
		return this;
	}
	
	/**
	 * 控制要排除哪些返回的字段, 而不是整个_source
	 *
	 * @param fields
	 * @return ElasticUriQueryBuilder
	 */
	public ElasticUriQueryBuilder excludeSources(List<String> fields) {
		String[] sources = fields.stream().toArray(String[]::new);
		this.excludeSources = sources;
		return this;
	}
	
	/**
	 * 查询返回的结果类型
	 *
	 * @param resultType
	 * @return UriQueryBuilder
	 */
	public ElasticUriQueryBuilder resultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}
	
	/**
	 * 执行查询
	 *
	 * @param <T>
	 * @return List<T>
	 */
	@SuppressWarnings({"unchecked"})
	public <T> List<T> queryForList() {
		if (isBlank(q)) {
			throw new IllegalArgumentException("Please set query string first!");
		}
		String queryString = buildQueryString();
		log.info("Query String Query: {}", queryString);
		
		String responseJson = null;
		if (isNotBlank(username) && isNotBlank(password)) {
			responseJson = HttpUtils.get(queryString).basicAuth(username, password).request();
		} else {
			responseJson = HttpUtils.get(queryString).request();
		}
		boolean error = JsonPathUtils.ifExists(responseJson, "error");
		if (error) {
			String rootCause = JsonPathUtils.readNode(responseJson, "$.error.root_cause[0].reason");
			throw new UriQueryException(rootCause);
		}
		if (resultType != null) {
			return JsonPathUtils.readListNode(responseJson, "$.hits.hits[*]._source", resultType);
		}
		return (List<T>) JsonPathUtils.readListNode(responseJson, "$.hits.hits[*]._source");
	}
	
	/**
	 * 有多种查询语法
	 * <ul>
	 * <li/>df查询 GET movies/_search?q=2012&df=title
	 * <li/>指定字段查询 GET movies/_search?q=title:2012
	 * <li/>泛查询 GET movies/_search?q=2012              会对文档中所有字段进行查询
	 * <li/>GET movies/_search?q=title:Beautiful Mind     与下面的等价
	 * <li/>GET movies/_search?q=title:Beautiful OR Mind
	 * <li/>Pahrase Query(引号引起来的) GET movies/_search?q=title:"Beautiful Mind"  表示Beautiful Mind要同时出现并且按照规定的顺序, 与下面的等价
	 * <li/>GET movies/_search?q=title:Beautiful AND Mind
	 * <li/>分组 GET movies/_search?q=title:(Beautiful Mind)
	 * <li/>GET movies/_search?q=title:(Beautiful NOT Mind)  包含Beautiful 不包含 Mind
	 * <li/>GET movies/_search?q=title:(Beautiful %2BMind)   必须包含Mind, %2B是 + 号的转义字符
	 * <li/>范围查询 GET movies/_search?q=year:>1980
	 * </ul>
	 *
	 * @return
	 */
	private String buildQueryString() {
		StringBuilder sb = new StringBuilder();
		sb.append(RestSupport.HOSTS.get(0))
				.append("/").append(index)
				.append("/").append("_search")
				.append("?").append(q);
		
		/*
		 * 拼接上 &df=title
		 */
		if (isNotBlank(df)) {
			sb.append("&").append(DF + df);
		}
		
		if (isNotBlank(sort)) {
			sb.append("&").append(SORT + sort);
		}
		
		if (from != null) {
			sb.append("&").append(FROM + from)
					.append("&").append(SIZE + size);
		} else if (size != null) {
			//只指定了获取的数据量
			sb.append("&").append(SIZE + size);
		}
		
		if (!fetchSource) {
			sb.append("&_source=false");
		}
		
		if (excludeSources != null && excludeSources.length > 0) {
			sb.append("&_source_excludes=")
					.append(StringUtils.joinWith(",", excludeSources));
		}
		
		if (includeSources != null && includeSources.length > 0) {
			sb.append("&_source_includes=")
					.append(StringUtils.joinWith(",", includeSources));
		}
		
		return sb.toString();
	}
	
}
