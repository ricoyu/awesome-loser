package com.loserico.search;

import org.junit.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

/**
 * 测试集群健康
 * <p>
 * Copyright: Copyright (c) 2024-05-14 9:04
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ClusterHealthTest {

    @Test
    public void testMakeClusterYellow() {
        ElasticUtils.ping();
        boolean created = ElasticUtils.Admin.createIndex("test002")
                .settings()
                .numberOfShards(5)
                .numberOfReplicas(7)
                .thenCreate();

        try {
            SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String health = ElasticUtils.Cluster.health();
        assertEquals(health, "YELLOW");
    }
}
