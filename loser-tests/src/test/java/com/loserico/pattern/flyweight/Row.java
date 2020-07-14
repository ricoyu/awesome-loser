package com.loserico.pattern.flyweight;

import java.util.ArrayList;
import java.util.List;

/** 
 *  
 *描述：行 不可共享享元 
 */
public class Row implements Glyph {

	private List<Character> list = new ArrayList<>();

	public void setCharacter(Glyph r) {
		list.add((Character) r);
	}

	public int getSize() {
		return list.size();
	}

	public String getRow() {
		StringBuilder sb = new StringBuilder();
		for (Character g : list) {
			sb.append(g.getC());
		}
		return sb.toString();
	}

	@Override
	public void draw(Context context) {
	}

}