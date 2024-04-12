package com.loserico.pattern.behavioral.command;

/**
 * 命令的接收者
 * <p>
 * Copyright: Copyright (c) 2024-04-02 11:25
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class Light {
    public void on() {
        System.out.println("Light is ON");
    }

    public void off() {
        System.out.println("Light is OFF");
    }
}