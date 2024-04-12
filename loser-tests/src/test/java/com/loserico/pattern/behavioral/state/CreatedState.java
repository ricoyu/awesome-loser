package com.loserico.pattern.behavioral.state;

/**
 * 为每个可能的订单状态实现具体的状态类
 * <p>
 * Copyright: Copyright (c) 2024-04-02 13:50
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CreatedState implements OrderState {
    public void next(Order order) {
        order.setState(new PaidState());
    }

    public void previous(Order order) {
        System.out.println("The order is in its root state.");
    }

    public void printStatus() {
        System.out.println("Order created.");
    }
}