package com.loserico.search;

import com.loserico.networking.enums.HttpMethod;
import com.loserico.networking.utils.HttpUtils;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2021-07-15 11:42
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PipelineTest {
	
	@Test
	public void testCreatePipeline() {
		Object result = HttpUtils.post()
				.method(HttpMethod.PUT)
				.path("_ingest/pipeline/blog_pipeline")
				.host("192.168.100.101")
				.port(9200)
				.body("")
				.request();
	}
	
	@Test
	public void testCreate() {
		ElasticUtils.Admin.pipeline("blog_pipeline").delete();
		ElasticUtils.Admin.pipeline("blog_pipeline")
				.pipeFilename("blog_pipeline.json")
				.create();
		String pipeline = ElasticUtils.Admin.pipeline("blog_pipeline").get();
		System.out.println(pipeline);
		
		ElasticUtils.Admin.deleteIndex("tech_blogs");
		ElasticUtils.index("tech_blogs")
				.doc("{\"title\": \"Introducing big data......\", \"tags\": \"hadoop,elasticsearch,spark\", \"content\": \"You know, for big data\"}")
				.execute();
		String id =
				ElasticUtils.index("tech_blogs")
						.doc("{\"title\": \"Introducing cloud computering\", \"tags\": \"openstacks,k8s\", \"content\": \"You know, for cloud\"}")
						.id(2)
						.pipeline("blog_pipeline")
						.refresh(true)
						.execute();
		
		List<String> techBlogs = ElasticUtils.Query.matchAllQuery("tech_blogs").queryForList();
		techBlogs.forEach(System.out::println);
		System.out.println(id);
		
	}
}
