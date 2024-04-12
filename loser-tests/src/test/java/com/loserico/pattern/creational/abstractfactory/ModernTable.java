package com.loserico.pattern.creational.abstractfactory;

class ModernTable implements Table {
    @Override
    public void displayStyle() {
        System.out.println("Displaying a modern table.");
    }
}