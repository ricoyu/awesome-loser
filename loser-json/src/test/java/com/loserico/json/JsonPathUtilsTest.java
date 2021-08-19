package com.loserico.json;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jsonpath.JsonPathUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>
 * Copyright: (C), 2021-04-29 16:47
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class JsonPathUtilsTest {
	
	@Test
	public void test() {
		String json = IOUtils.readClassPathFileAsString("es-data.json");
		List<Movie> movies = JsonPathUtils.readListNode(json, "$.hits.hits[*]._source", Movie.class);
		movies.forEach(System.out::println);
		JsonPathUtils.readNode(json, "$.hits");
	}
	
	@Test
	public void test2() {
		String allJson = IOUtils.readFileAsString("D:\\all.json");
		List<String> allIds = JsonPathUtils.readListNode(allJson, "$.content.data[*]._id").stream().sorted().collect(Collectors.toList());
		
		//检查下分页查到的数据是不是都在完整的数据里面
		for (int i = 1; i < 5; i++) {
			String json = IOUtils.readFileAsString("D:\\page" + i + ".json");
			List<String> idList = JsonPathUtils.readListNode(json, "$.content.data[*]._id");
			for (String id : idList) {
				if (!allIds.contains(id)) {
					log.warn("分页查到的ID不在完整的数据里面");
				}
			}
		}
		
		Set<String> ids = new HashSet<>();
		for (int i = 1; i < 5; i++) {
			String json = IOUtils.readFileAsString("D:\\page" + i + ".json");
			List<String> idList = JsonPathUtils.readListNode(json, "$.content.data[*]._id").stream().sorted().collect(Collectors.toList());
			System.out.println("第" + i + "页数据:");
			idList.forEach(System.out::println);
			
			List<String> duplicateIds = new ArrayList<>();
			for (String id : idList) {
				if (ids.contains(id)) {
					duplicateIds.add(id);
				}
			}
			if (!duplicateIds.isEmpty()) {
				System.out.println("第" + i + "页发现有" + duplicateIds.size() + "条数据重复:");
				duplicateIds.forEach(System.out::println);
			}
			ids.addAll(idList);
			
		}
		assertThat(ids.size()).isEqualTo(70);
	}
	
}
