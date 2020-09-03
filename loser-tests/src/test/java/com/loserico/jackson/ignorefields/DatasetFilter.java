package com.loserico.jackson.ignorefields;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020-08-19 16:56
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
// ignore the property with name 'tags'.
@JsonIgnoreProperties({"tags"})
public class DatasetFilter {
	
	private String album_id;
	private String album_title;
	private String album_comments;
	private Map<String, Object> otherProperties = new HashMap<>();
	
	@JsonCreator
	public DatasetFilter(@JsonProperty("album_id") String album_id, @JsonProperty("album_title") String album_title) {
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
	
	// ignore the property specified by this getter.
	@JsonIgnore
	public String getAlbum_comments() {
		return album_comments;
	}
	
	public void setAlbum_comments(String album_comments) {
		this.album_comments = album_comments;
	}
	
	public Map<String, Object> getOtherProperties() {
		return otherProperties;
	}
	
	public void setOtherProperties(Map<String, Object> otherProperties) {
		this.otherProperties = otherProperties;
	}
	
	// this method is used to get all properties not specified earlier.
	@JsonAnyGetter
	public Map<String , Object> any() {
		return otherProperties;
	}
	
	@JsonAnySetter
	public void set(String name, Object value) {
		otherProperties.put(name, value);
	}
	
	public Object get(String name) {
		return otherProperties.get(name);
	}
}
