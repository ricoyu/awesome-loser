package com.loserico.pattern.flyweight;

import java.util.HashMap;
import java.util.Map;

/** 
 *描述：享元工厂 
 */
public class GlyphFactory {

	private Map<String, Glyph> map = new HashMap<>();

	public Glyph getGlyph(Context context) {
		String cStr = context.getC() + "";
		Glyph gl = map.get(cStr);
		if (gl == null) {
			gl = new Character(context.getC());
			map.put(cStr, gl);
		}
		gl.draw(context);
		return gl;
	}

}