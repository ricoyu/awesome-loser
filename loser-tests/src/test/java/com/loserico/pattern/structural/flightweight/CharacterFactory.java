package com.loserico.pattern.structural.flightweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 蝇量工厂（CharacterFactory），用于创建和管理蝇量对象
 * <p>
 * Copyright: Copyright (c) 2024-03-29 17:22
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
class CharacterFactory {
    private Map<String, Character> characters = new HashMap<>();

    public Character getCharacter(char key, String style) {
        String compositeKey = key + "-" + style;
        Character character = characters.get(compositeKey);
        if (character == null) {
            character = new ConcreteCharacter(key, style);
            characters.put(compositeKey, character);
        }
        return character;
    }
}