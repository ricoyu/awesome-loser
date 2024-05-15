package com.loserico.search;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IndexTest {

    /**
     * 创建Index的同时插入文档, 对应的DSL语句
     * PUT product/_doc/1
     * {
     *   "name": "小米手机",
     *   "desc": "手机中的战斗机",
     *   "price": 3999,
     *   "lv": "旗舰机",
     *   "type": "手机",
     *   "createtime": "2020-10-01T08:00:00Z",
     *   "tags": [
     *     "性价比",
     *     "发烧",
     *     "不卡顿"
     *   ]
     * }
     */
    @Test
    public void testCreateProduct() {
        boolean deleted = ElasticUtils.Admin.deleteIndex("product");
        System.out.println(deleted);
        String result = ElasticUtils.index("product")
                .id("1")
                .doc("{\n" +
                        "  \"name\": \"小米手机\",\n" +
                        "  \"desc\": \"手机中的战斗机\",\n" +
                        "  \"price\": 3999,\n" +
                        "  \"lv\": \"旗舰机\",\n" +
                        "  \"type\": \"手机\",\n" +
                        "  \"createtime\": \"2020-10-01T08:00:00Z\",\n" +
                        "  \"tags\": [\n" +
                        "    \"性价比\",\n" +
                        "    \"发烧\", \n" +
                        "    \"不卡顿\"\n" +
                        "  ]\n" +
                        "}")
                .execute();
        System.out.println(result);
    }

    /**
     * 仅创建Index, 同时设置分片和副本数
     */
    @Test
    public void testCreateIndexWithSettings() {
        boolean created = ElasticUtils.Admin.createIndex("blogs")
                .settings()
                .numberOfShards(3) //#主分片数3
                .numberOfReplicas(3) //每个主分片3个副本
                .thenCreate();
        assertTrue(created);
    }
}
