package com.loserico.search;

import org.junit.Test;

import java.util.List;

public class IncludeExcludeTest {

    @Test
    public void testInclude() {
        List<Object> products = ElasticUtils.Query.matchAllQuery("product")
                .includeSources("name")
                .queryForList();
        products.forEach(System.out::println);
    }
}
