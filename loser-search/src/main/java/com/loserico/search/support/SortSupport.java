package com.loserico.search.support;

import com.loserico.search.enums.Direction;
import com.loserico.search.enums.SortOrder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.loserico.common.lang.utils.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 排序功能支持
 * <p>
 * Copyright: (C), 2021-08-26 9:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class SortSupport {
	
	/**
	 * 排序字符串支持的格式:
	 * <ul>
	 *     <li/>name,age:asc,year:desc
	 *     <li/>-name,year:desc
	 * </ul>
	 * 字段名前面-表示desc降序
	 *
	 * @param sort
	 * @return List<SortOrder>
	 */
	public static List<SortOrder> sort(String sort) {
		if (isBlank(sort)) {
			return Collections.emptyList();
		}
		
		List<SortOrder> sortOrders = new ArrayList<>();
		/*
		 * 先把 name,age:asc,year:desc 这种形式的排序字符串用,拆开
		 */
		String[] sorts = split(sort, ",");
		for (int i = 0; i < sorts.length; i++) {
			String s = sorts[i];
			if (isBlank(s)) {
				log.warn("you entered a block sort!");
				continue;
			}
			
			//再用冒号拆出 field 和 asc|desc
			String[] fieldAndDirection = s.trim().split(":");
			//field:asc 拆开来长度只能是1或者2
			if (fieldAndDirection.length == 0) {
				log.warn("wrong sort gramma {}, should be of type field1:asc,field2:desc", sort);
				continue;
			}
			
			//数组第一个元素是字段名, 不能省略, 所以为空的话表示语法不正确
			String field = fieldAndDirection[0].trim();
			if (isBlank(field)) {
				log.warn("field is blank, skip");
				continue;
			}
			
			String direction = null;
			if (fieldAndDirection.length > 1) {
				direction = fieldAndDirection[1].trim();
			}
			//排序规则为空的话, 看下字段名是不是-开头的, 如果是的话, 降序, 否则默认升序
			if (isBlank(direction)) {
				if (field.startsWith("-")) {
					field = field.substring(1);
					direction = "desc";
				} else {
					direction = "asc";
				}
			}
			
			SortOrder sortOrder = null;
			if ("_score".equals(field)) {
				SortOrder.SortOrderBuilder scoreSort = SortOrder.scoreSort();
				if (Direction.ASC.name().equalsIgnoreCase(direction)) {
					sortOrder = scoreSort.asc();
				} else if (Direction.DESC.name().equalsIgnoreCase(direction)) {
					sortOrder = scoreSort.desc();
				}
			} else {
				sortOrder = SortOrder.fieldSort(field).direction(direction);
			}
			sortOrders.add(sortOrder);
		}
		
		return sortOrders;
	}
}
