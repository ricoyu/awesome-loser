package com.loserico.search.builder.query;

import com.loserico.common.lang.resource.PropertyReader;
import com.loserico.json.jsonpath.JsonPathUtils;
import com.loserico.networking.utils.HttpUtils;
import com.loserico.search.enums.Direction;
import com.loserico.search.exception.UriQueryException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.loserico.common.lang.utils.Assert.notEmpty;
import static com.loserico.common.lang.utils.Assert.notNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
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
public class UriQueryBuilder {
	
	private static final String DF = "df=";
	private static final String COLON = ":";
	private static final String SORT = "sort=";
	private static final String FROM = "from=";
	private static final String SIZE = "size=";
	private static String host = null;
	
	{
		PropertyReader reader = new PropertyReader("elastic");
		String hosts = reader.getString("elastic.rest.hosts");
		if (!hosts.contains(COLON)) {
			hosts = hosts + COLON + "9200"; //每页包含端口号就把默认的9200 端口拼接上去
		}
		host = hosts;
	}
	
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
	 * 查询返回的结果类型
	 */
	private Class resultType;
	
	public UriQueryBuilder(String index) {
		notNull(index, "index cannot be null!");
		this.index = index;
	}
	
	/**
	 * 指定查询语句, 使用Query String Syntax<p>
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
	 * 你可以就写查询条件: 2012<p>
	 * 也可以写完成的查询: q=2012 或者 q=2012&df=title 等<p>
	 *
	 * @param q
	 * @return QueryStringQueryBuilder
	 */
	public UriQueryBuilder query(String q) {
		notEmpty(q, "q cannot be empty");
		if (!q.startsWith("q=")) {
			q = "q=" + q;
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
	public UriQueryBuilder field(String df) {
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
	public UriQueryBuilder sort(String sort) {
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
	public UriQueryBuilder page(Integer from, Integer size) {
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
	public UriQueryBuilder size(Integer size) {
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
	public UriQueryBuilder sort(String field, Direction direction) {
		notNull(field, "field cannot be null!");
		notNull(direction, "direction cannot be null!");
		this.sort = field + ":" + direction;
		return this;
	}
	
	/**
	 * 查询返回的结果类型
	 *
	 * @param resultType
	 * @return UriQueryBuilder
	 */
	public UriQueryBuilder resultType(Class resultType) {
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
		log.info(queryString);
		
		String responseJson = HttpUtils.get(queryString).request();
		boolean error = JsonPathUtils.ifExists(responseJson, "error");
		if (error) {
			String rootCause = JsonPathUtils.readNode(responseJson, "$.error.root_cause[0].reason");
			throw new UriQueryException(rootCause);
		}
		if (resultType != null) {
			return JsonPathUtils.readListNode(responseJson, "$.hits.hits[*]._source", resultType);
		}
		return (List<T>)JsonPathUtils.readListNode(responseJson, "$.hits.hits[*]._source");
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
		if (!host.startsWith("http")) {
			sb.append("http://");
		}
		sb.append(host)
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
		
		return sb.toString();
	}
	
}
