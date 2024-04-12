package com.loserico.pattern.behavioral.command;

/**
 * 命令调用者，它负责调用命令对象执行请求
 * <p>
 * Copyright: Copyright (c) 2024-04-02 11:27
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}