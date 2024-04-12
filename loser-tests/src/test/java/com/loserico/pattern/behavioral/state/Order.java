package com.loserico.pattern.behavioral.state;

/**
 * 上下文类
 * <p>
 * Copyright: Copyright (c) 2024-04-02 13:52
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class Order {
    private OrderState state;

    public Order() {
        this.state = new CreatedState();
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public OrderState getState() {
        return state;
    }

    public void nextStep() {
        state.next(this);
    }

    public void previousStep() {
        state.previous(this);
    }

    public void printStatus() {
        state.printStatus();
    }
}
