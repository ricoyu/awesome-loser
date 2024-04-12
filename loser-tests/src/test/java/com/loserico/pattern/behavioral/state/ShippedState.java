package com.loserico.pattern.behavioral.state;
/**
 * 为每个可能的订单状态实现具体的状态类
 * <p>
 * Copyright: Copyright (c) 2024-04-02 13:51
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class ShippedState implements OrderState {
    public void next(Order order) {
        order.setState(new CompletedState());
    }

    public void previous(Order order) {
        order.setState(new PaidState());
    }

    public void printStatus() {
        System.out.println("Order shipped.");
    }
}