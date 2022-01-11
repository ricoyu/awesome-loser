package com.loserico.json.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.loserico.common.lang.utils.EnumUtils;
import com.loserico.common.lang.utils.StringUtils;
import com.loserico.common.lang.vo.OrderBean;
import com.loserico.common.lang.vo.OrderBean.ORDER_BY;
import com.loserico.common.lang.vo.Page;

import java.io.IOException;
import java.util.Iterator;

/**
 * <p>
 * Copyright: (C), 2020-09-16 16:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PageDeserializer extends StdDeserializer<Page> {
	
	/**
	 * 分页时传入的SQL排序字段名, 需要防止SQL注入, 去掉一些特殊字符 
	 */
	private static String[] SQL_TRIM_STR = new String[]{" ", "\\(", "\\)", "\\*", "\\\\", "/"};
	
	public PageDeserializer() {
		this(null);
	}
	
	public PageDeserializer(Class<?> vc) {
		super(vc);
	}
	
	@Override
	public Page deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		JsonNode pageNode = jp.getCodec().readTree(jp);
		
		/*
		 * 这两个属性是必须要的, 如果为提供的话, 直接返回null
		 * 
		 * currentPage 等价于 pageNum
		 */
		JsonNode currentPageNode = pageNode.get("currentPage");
		if (currentPageNode == null) {
			currentPageNode = pageNode.get("pageNum");
		}
		JsonNode pageSizeNode = pageNode.get("pageSize");
		if (currentPageNode == null || pageSizeNode == null) {
			return null;
		}
		
		Page page = new Page();
		page.setCurrentPage(currentPageNode.intValue());
		page.setPageSize(pageSizeNode.intValue());
		
		JsonNode pagingIgnoreNode = pageNode.get("pagingIgnore");
		if (pagingIgnoreNode != null) {
			page.setPagingIgnore(pagingIgnoreNode.booleanValue());
		}
		
		/*
		 * {
		 *   "search":"HTTP规则",
		 *   "page": {
		 *     "currentPage": 1,
		 *     "pageSize": 10,
		 *     "order": {
		 *       "orderBy": "-create_time",
		 *       "direction": "ASC"
		 *     },
		 *     "sorts": ["-datatime"]
		 *   }
		 * }
		 * 
		 * 处理order方式排序
		 */
		JsonNode orderNode = pageNode.get("order");
		if (orderNode != null) {
			String orderBy = orderNode.get("orderBy").textValue();
			String direction = "ASC";
			JsonNode directionNode = orderNode.get("direction");
			if (directionNode != null) {
				direction = directionNode.textValue();
			}
			
			OrderBean order = new OrderBean(orderBy, (ORDER_BY) EnumUtils.lookupEnum(ORDER_BY.class, direction));
			page.setOrder(order);
			page.getOrders().add(order);
			
			return page;
		}
		
		/*
		 * 处理page对象里面的 sorts: ["-create_time"] 这种形式的排序
		 */
		JsonNode sortsNode = pageNode.get("sorts");
		if (sortsNode == null) {
			return page;
		}
		
		if (sortsNode.isArray()) {
			Iterator<JsonNode> it = sortsNode.iterator();
			while (it.hasNext()) {
				JsonNode next = it.next();
				String orderBy = next.textValue().trim();
				addOrder(page, StringUtils.trimAll(orderBy, SQL_TRIM_STR));
			}
			return page;
		}
		
		String orderBy = sortsNode.textValue();
		addOrder(page, StringUtils.trimAll(orderBy, SQL_TRIM_STR));
		return page;
	}
	
	private void addOrder(Page page, String orderBy) {
		orderBy = StringUtils.trimQuote(orderBy);
		String direction = null;
		if (orderBy != null && orderBy.startsWith("-")) {
			orderBy = orderBy.substring(1);
			direction = "DESC";
		} else {
			direction = "ASC";
		}
		
		OrderBean order = new OrderBean(orderBy, (ORDER_BY) EnumUtils.lookupEnum(ORDER_BY.class, direction));
		page.setOrder(order);
		page.getOrders().add(order);
	}
}
