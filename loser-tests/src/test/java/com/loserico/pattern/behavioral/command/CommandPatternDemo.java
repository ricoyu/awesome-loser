package com.loserico.pattern.behavioral.command;

/**
 * 客户端代码
 * <p>
 * Copyright: Copyright (c) 2024-04-02 11:28
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CommandPatternDemo {

    public static void main(String[] args) {
        Light light = new Light();
        Command lightOn = new LightOnCommand(light);
        Command lightOff = new LightOffCommand(light);

        RemoteControl control = new RemoteControl();

        // 打开灯
        control.setCommand(lightOn);
        control.pressButton();

        // 关闭灯
        control.setCommand(lightOff);
        control.pressButton();
    }
}
