package com.loserico.pattern.structural.flightweight;

public class FlyweightPatternDemo {

    public static void main(String[] args) {
        CharacterFactory factory = new CharacterFactory();

        // 创建字符，共享样式
        Character characterA1 = factory.getCharacter('A', "Bold 12pt Arial");
        Character characterA2 = factory.getCharacter('A', "Bold 12pt Arial");
        Character characterB = factory.getCharacter('B', "Bold 12pt Arial");

        // 输出字符信息
        characterA1.printCharacter();
        characterA2.printCharacter();
        characterB.printCharacter();

        // 检查两个'A'字符是否为同一对象
        System.out.println("characterA1 == characterA2? " + (characterA1 == characterA2)); // 应输出true
    }
}