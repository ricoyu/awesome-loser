package com.loserico.jackson.dynamicbeans;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.loserico.map.HashMap;

import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020-08-19 16:18
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class DatasetDynamic {
	
	private String album_id;
	
	private String album_title;
	
	private Map<String, Object> otherProperties = new HashMap<>();
	
	@JsonCreator
	public DatasetDynamic(@JsonProperty("album_id") String album_id, @JsonProperty("album_title") String album_title) {
		this.album_id = album_id;
		this.album_title = album_title;
	}
	
	public String getAlbum_id() {
		return album_id;
	}
	
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	
	public String getAlbum_title() {
		return album_title;
	}
	
	public void setAlbum_title(String album_title) {
		this.album_title = album_title;
	}
	
	public Object get(String name) {
		return otherProperties.get(name);
	}
	
	@JsonAnyGetter
	public Map<String, Object> any() {
		return otherProperties;
	}
	
	@JsonAnySetter
	public void set(String name, Object value) {
		otherProperties.put(name, value);
	}
}
