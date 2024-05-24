package com.loserico.search;

import org.junit.Test;

import java.util.List;

import static com.loserico.search.enums.Direction.DESC;

public class SortTest {

    @Test
    public void testMatchAllSort() {
        List<Object> products = ElasticUtils.Query.matchAllQuery("product")
                .sort("name.keyword", DESC)
                .queryForList();
        products.forEach(System.out::println);
    }
}
