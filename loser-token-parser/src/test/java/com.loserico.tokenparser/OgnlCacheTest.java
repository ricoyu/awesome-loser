package com.loserico.tokenparser;

import com.loserico.tokenparser.ognl.OgnlCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020-09-16 10:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class OgnlCacheTest {
	
	public static void main(String[] args) {
		Corp corp = new Corp("上海观安信息科技有限公司", "腾飞创新园塔1", LocalDate.of(2020, 7, 20));
		User user = new User("三少爷", 38, corp);
		Object name = OgnlCache.getValue("name", user);
		System.out.println(name);
		
		Object enrollDate = OgnlCache.getValue("corp.enrollDate", user);
		System.out.println(enrollDate);
		
		Map<String, Object> params = new HashMap<>();
		params.put("user", user);
		Object corpName = OgnlCache.getValue("user.corp.name", params);
		System.out.println(corpName);
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class User {
		private String name;
		private int age;
		private Corp corp;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class Corp {
		
		private String name;
		private String address;
		private LocalDate enrollDate;
	}
}
