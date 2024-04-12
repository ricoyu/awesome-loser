package com.loserico.pattern.creational.factorymethod;

/**
 * 具体品牌的电脑类
 * <p>
 * Copyright: Copyright (c) 2024-03-29 14:10
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MacComputer extends Computer {
    @Override
    public void setOperationSystem() {
        System.out.println("Mac笔记本安装Mac系统");
    }
}