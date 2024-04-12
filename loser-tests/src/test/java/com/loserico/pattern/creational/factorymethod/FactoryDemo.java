package com.loserico.pattern.creational.factorymethod;

public class FactoryDemo {

    public static void main(String[] args) {
        //生产Mac电脑
        ComputerFactory macFactory=new MacComputerFactory();
        macFactory.makeComputer().setOperationSystem();

        //生产小米电脑
        ComputerFactory miFactory=new MiComputerFactory();
        miFactory.makeComputer().setOperationSystem();
    }
}
