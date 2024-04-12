package com.loserico.pattern.behavioral.state;

public class StatePatternRealWorldDemo {

    public static void main(String[] args) {
        Order order = new Order();
        order.printStatus();

        order.nextStep();
        order.printStatus();

        order.nextStep();
        order.printStatus();

        order.nextStep();
        order.printStatus();

        order.nextStep(); // 尝试进入不存在的下一个状态
    }
}
