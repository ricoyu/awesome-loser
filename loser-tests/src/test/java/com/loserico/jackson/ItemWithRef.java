package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class ItemWithRef {
	public int id;
	public String itemName;

	@JsonManagedReference
	public UserWithRef owner;

	public ItemWithRef() {
	}

	public ItemWithRef(int id, String itemName, UserWithRef owner) {
		this.id = id;
		this.itemName = itemName;
		this.owner = owner;
	}
}