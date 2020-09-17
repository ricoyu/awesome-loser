package com.loserico.json.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.loserico.common.lang.utils.EnumUtils;
import com.loserico.common.lang.vo.OrderBean;
import com.loserico.common.lang.vo.OrderBean.ORDER_BY;
import com.loserico.common.lang.vo.Page;

import java.io.IOException;

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
		 */
		JsonNode currentPageNode = pageNode.get("currentPage");
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
		
		JsonNode orderNode = pageNode.get("order");
		if (orderNode != null) {
			String orderBy = orderNode.get("orderBy").textValue();
			String direction = orderNode.get("direction").textValue();
			OrderBean order = new OrderBean(orderBy, (ORDER_BY) EnumUtils.lookupEnum(ORDER_BY.class, direction));
			page.setOrder(order);
		}
		return page;
	}
}
