package com.loserico.pattern.behavioral.state;

/**
 * 每个可能的订单状态实现具体的状态类
 * <p>
 * Copyright: Copyright (c) 2024-04-02 13:52
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CompletedState implements OrderState {
    public void next(Order order) {
        System.out.println("This order is already completed.");
    }

    public void previous(Order order) {
        order.setState(new ShippedState());
    }

    public void printStatus() {
        System.out.println("Order completed.");
    }
}