package com.loserico.pattern.behavioral.command;

/**
 * 具体的命令实现，打开灯的命令
 * <p>
 * Copyright: Copyright (c) 2024-04-02 11:24
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }
}