package com.loserico.algorithm_msb.linked_list;

/**
 * 双链表结构
 * <p>
 * Copyright: Copyright (c) 2024-03-28 17:58
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class DoubleNode {

    public DoubleNode prev;
    public DoubleNode next;
    private int value;

    public DoubleNode(int value) {
        this.value = value;
    }
}
