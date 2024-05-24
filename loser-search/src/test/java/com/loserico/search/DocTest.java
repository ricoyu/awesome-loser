package com.loserico.search;

import com.loserico.search.support.UpdateResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.loserico.json.jackson.JacksonUtils.toJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocTest {

    /**
     * 对应 GET product/_search
     */
    @Test
    public void testMatchAllQuery() {
        List<Object> products = ElasticUtils.Query.matchAllQuery("products").queryForList();
    }

    @Test
    public void testGetDocById() {
        String user = ElasticUtils.get("users", "YM3Deo8ByYxHJQGNXPTj");
        System.out.println(user);
        User user1 = ElasticUtils.Query.byId("users", "YM3Deo8ByYxHJQGNXPTj", User.class);
        assertEquals(user1.getUser(), "mike");

    }

    @Test
    public void testQueryStringQuery() {
        List<Object> ecommerces = ElasticUtils.Query.queryString("kibana_sample_data_ecommerce")
                .query("customer_first_name=Eddie")
                .queryForList();

        for (Object ecommerce : ecommerces) {
        	System.out.println(ecommerce);
        }

       ecommerces = ElasticUtils.Query.uriQuery("kibana_sample_data_ecommerce")
                .query("customer_first_name=Eddie")
                .queryForList();

        for (Object ecommerce : ecommerces) {
        	System.out.println(ecommerce);
        }
    }

    @Test
    public void testGetProductById() {
        String product = ElasticUtils.get("product", 2);
        System.out.println(product);
    }

    @Data
    @NoArgsConstructor
    public static class User {
        private String user;
        private LocalDateTime postDate;
        private String message;
    }

    @Test
    public void testUpsert() {
        UpdateResult updateResult = ElasticUtils.update("product")
                .id(1)
                .doc("{\"price\": 9999}")
                .upsert(true)
                .update();
        System.out.println(toJson(updateResult));
    }

    @Test
    public void testDeleteDocById() {
        boolean deleted = ElasticUtils.delete("product", 2);
        assertTrue(deleted);
    }
}
