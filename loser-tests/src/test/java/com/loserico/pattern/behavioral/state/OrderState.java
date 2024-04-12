package com.loserico.pattern.behavioral.state;

/**
 * 状态接口，它包含了订单在每个状态下可以执行的操作
 * <p>
 * Copyright: Copyright (c) 2024-04-02 13:50
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface OrderState {
    void next(Order order);
    void previous(Order order);
    void printStatus();
}