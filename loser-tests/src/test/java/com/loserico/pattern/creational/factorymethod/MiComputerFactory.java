package com.loserico.pattern.creational.factorymethod;

/**
 * 生产小米电脑的MiComputerFactory 工厂
 * <p>
 * Copyright: Copyright (c) 2024-03-29 14:13
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class MiComputerFactory implements ComputerFactory {
    @Override
    public Computer makeComputer() {
        return new MiComputer();
    }
}