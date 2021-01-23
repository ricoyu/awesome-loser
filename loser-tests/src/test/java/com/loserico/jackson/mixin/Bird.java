package com.loserico.jackson.mixin;

import java.io.Serializable;

public class Bird implements Serializable {
	
	private String name;
	private String sound;
	private String habitat;
	
	public Bird(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSound() {
		return sound;
	}
	
	public String getHabitat() {
		return habitat;
	}
	
	public void setSound(String sound) {
		this.sound = sound;
	}
	
	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}
	
	@Override
	public String toString() {
		return "Bird [name=" + name + ", sound=" + sound + ", habitat=" + habitat + "]";
	}
	
}
