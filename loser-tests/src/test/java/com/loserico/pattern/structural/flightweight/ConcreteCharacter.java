package com.loserico.pattern.structural.flightweight;

/**
 * 实现蝇量接口的具体字符类
 * <p>
 * Copyright: Copyright (c) 2024-03-29 17:21
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
class ConcreteCharacter implements Character {
    private char character;
    private String style; // 字体样式，可以是字体、大小、颜色等的组合，这里简化为一个字符串表示

    public ConcreteCharacter(char character, String style) {
        this.character = character;
        this.style = style;
    }

    @Override
    public void printCharacter() {
        System.out.println(character + " (" + style + ")");
    }
}