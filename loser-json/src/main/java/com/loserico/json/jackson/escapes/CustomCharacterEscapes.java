package com.loserico.json.jackson.escapes;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;

/**
 * <p>
 * Copyright: (C), 2021-07-06 10:45
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CustomCharacterEscapes extends CharacterEscapes {
	
	private final int[] asciiEscapes;
	
	public CustomCharacterEscapes() {
		asciiEscapes = standardAsciiEscapesForJSON();
		//By default the ascii Escape table in jackson has " added as escape string overwriting that here.
		asciiEscapes['"'] = CharacterEscapes.ESCAPE_NONE;
	}
	
	@Override
	public int[] getEscapeCodesForAscii() {
		return asciiEscapes;
	}
	
	@Override
	public SerializableString getEscapeSequence(int i) {
		return null;
	}
}
