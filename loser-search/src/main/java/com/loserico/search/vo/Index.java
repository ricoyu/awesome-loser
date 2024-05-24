package com.loserico.search.vo;

import com.loserico.search.enums.IndexState;
import lombok.Data;
import lombok.ToString;

/**
 * 表示一个索引
 * <p>
 * Copyright: Copyright (c) 2024-05-15 21:11
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@ToString
public class Index {

    /**
     * 索引名
     */
    private String name;

    private String uuid;

    private int numberOfShards;

    private int numberOfReplicas;

    private IndexState state;

}
