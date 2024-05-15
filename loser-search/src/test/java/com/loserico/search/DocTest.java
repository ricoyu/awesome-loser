package com.loserico.search;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

    @Data
    @NoArgsConstructor
    public static class User {
        private String user;
        private LocalDateTime postDate;
        private String message;
    }
}
