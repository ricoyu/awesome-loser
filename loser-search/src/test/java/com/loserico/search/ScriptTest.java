package com.loserico.search;

import com.loserico.common.lang.utils.IOUtils;
import com.loserico.json.jackson.JacksonUtils;
import com.loserico.networking.utils.HttpUtils;
import com.loserico.search.support.RestSupport;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试painless script用法
 * <p>
 * Copyright: (C), 2021-07-21 9:30
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ScriptTest {
	
	@Test
	public void testCreateStoredScriptHttp() {
		String script = "{\"script\": {\"lang\": \"painless\",  \"source\": \"String fieldName = \"\"; for(int i=0; i<params.fields.length; i++) { String field = params.fields[i]; if(!\"\".equals(fieldName)) {        fieldName +=\"|\";} if(doc.containsKey(field+\".keyword\")) {fieldName += doc[field+\".keyword\"].value;} else {fieldName += doc[field].value;}} return fieldName;\"  }}";
		
		Object response = HttpUtils.post(RestSupport.HOST+"/_scripts/multi_field_agg")
				.body(script)
				.request();
		
		System.out.println(response);
	}
	
	@Test
	public void testCreateStoredScript() {
		//String script = "{  \"script\": { \"lang\": \"painless\", \"source\": \"String fieldName = ''; for(int i=0; i<params.fields.length; i++) {String field = params.fields[i];if(!''.equals(fieldName)) {fieldName +='|';}if(doc.containsKey(field+'.keyword')) {fieldName += doc[field+'.keyword'].value;} else {fieldName += doc[field].value;}}return fieldName;\" }}";
		String script = "{  \"script\": { " +
				"\"lang\": \"painless\", " +
				"\"source\": " +
				"\"String fieldName = ''; " +
				"for(int i=0; i<params.fields.length; i++) {" +
				"String field = params.fields[i];" +
				"if(!''.equals(fieldName)) {" +
				"fieldName +='|';" +
				"}" +
				"if(doc.containsKey(field+'.keyword')) {" +
				"fieldName += doc[field+'.keyword'].value;" +
				"} else {" +
				"fieldName += doc[field].value;" +
				"}}" +
				"return fieldName;\" }}";
		
		AcknowledgedResponse response = ElasticUtils.client.admin().cluster().preparePutStoredScript()
				.setId("multi_field_agg")
				.setContent(new BytesArray(script), XContentType.JSON)
				.get();
		
		System.out.println(response.isAcknowledged());
	}
	
	@Test
	public void testCreateStoredScriptFromFile() {
		AcknowledgedResponse deleteResponse = ElasticUtils.client.admin().cluster().prepareDeleteStoredScript("multi_field_agg").get();
		System.out.println(deleteResponse.isAcknowledged());
		AcknowledgedResponse response = ElasticUtils.client.admin().cluster().preparePutStoredScript()
				.setId("multi_field_agg")
				.setContent(new BytesArray(IOUtils.readClassPathFileAsString("scripts/multi_field.painless")), XContentType.JSON)
				.get();
		
		System.out.println(response.isAcknowledged());
	}
	
	@Test
	public void test() {
		Map<String, Object> params = new HashMap<>();
		params.put("fields", new String[]{"src_ip", "src_port"});
		
		String script = "String fieldName = \"\";\n" +
				"for(int i=0; i<params.fields.length; i++) {\n" +
				"  String field = params.fields[i];\n" +
				"  if(doc.containsKey(field+'.keyword')) {\n" +
				"    fieldName += doc[field+'.keyword'].value;\n" +
				"  } else {\n" +
				"    fieldName += doc[field].value;\n" +
				"  }\n" +
				"}\n" +
				"return fieldName;";
		Script painless = new Script(ScriptType.INLINE, "painless", script, params);
		SearchResponse response = ElasticUtils.client.prepareSearch("events")
				.addAggregation(AggregationBuilders.terms("script_agg").script(painless))
				.get();
		Aggregations aggregations = response.getAggregations();
		Aggregation scriptAgg = aggregations.get("script_agg");
		System.out.println(JacksonUtils.toPrettyJson(scriptAgg.toString()));
	}
	
	@Test
	public void testStored() {
		Map<String, Object> params = new HashMap<>();
		params.put("fields", new String[]{"src_ip", "src_port"});
		Script painless = new Script(ScriptType.STORED, null, "multi_field_agg", params);
		SearchResponse response = ElasticUtils.client.prepareSearch("events")
				.addAggregation(AggregationBuilders.terms("script_agg").script(painless))
				.get();
		Aggregations aggregations = response.getAggregations();
		Aggregation scriptAgg = aggregations.get("script_agg");
		System.out.println(JacksonUtils.toPrettyJson(scriptAgg.toString()));
	}
	
	@Test
	public void testElasticUtilsAPI() {
		//boolean acknowaged = ElasticUtils.Cluster.createMultiFieldAgg();
		//Assert.assertTrue(acknowaged);
		
		List<Map<String, Object>> resultMap = ElasticUtils.Aggs.multiTerms("netlog_2021-07-19")
				.of("script_agg", "src_ip", "src_port", "action")
				.fetchTotalHits(true)
				.get();
		
		JacksonUtils.toPrettyJson(resultMap);
		System.out.println(ElasticUtils.Aggs.totalHits());
	}
}
