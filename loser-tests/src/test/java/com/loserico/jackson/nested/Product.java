package com.loserico.jackson.nested;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020-09-08 10:11
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class Product {
	
	private String id;
	
	@JsonIgnore
	private String name;
	
	private String brandName;
	
	private String ownerName;
	
	@JsonProperty("brand")
	private void unpackNested(Map<String, Object> brand) {
		this.brandName = (String)brand.get("name");
		Map<String, String> owner = (Map<String, String>)brand.get("owner");
		this.ownerName = owner.get("name");
	}
}
