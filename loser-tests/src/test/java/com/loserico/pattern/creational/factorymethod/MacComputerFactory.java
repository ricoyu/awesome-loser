package com.loserico.pattern.creational.factorymethod;

/**
 * 生产苹果电脑的MacComputerFactory 工厂
 * <p>
 * Copyright: Copyright (c) 2024-03-29 14:13
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MacComputerFactory implements ComputerFactory {
    @Override
    public Computer makeComputer() {
        return new MacComputer();
    }
}