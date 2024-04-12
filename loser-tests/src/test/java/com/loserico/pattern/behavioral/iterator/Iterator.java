package com.loserico.pattern.behavioral.iterator;

/**
 * 定义迭代器接口
 * <p>
 * Copyright: Copyright (c) 2024-03-29 17:54
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface Iterator {
    boolean hasNext();
    Object next();
}
