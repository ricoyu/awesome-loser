package com.loserico.pattern.creational.abstractfactory;

public class FactoryMethodDemo {

    public static void main(String[] args) {
        FurnitureFactory modernFactory = new ModernFurnitureFactory();
        Chair modernChair = modernFactory.createChair();
        Table modernTable = modernFactory.createTable();
        modernChair.displayStyle(); // 输出: Displaying a modern chair.
        modernTable.displayStyle(); // 输出: Displaying a modern table.

        FurnitureFactory classicFactory = new ClassicFurnitureFactory();
        Chair classicChair = classicFactory.createChair();
        Table classicTable = classicFactory.createTable();
        classicChair.displayStyle(); // 输出: Displaying a classic chair.
        classicTable.displayStyle(); // 输出: Displaying a classic table.
    }
}