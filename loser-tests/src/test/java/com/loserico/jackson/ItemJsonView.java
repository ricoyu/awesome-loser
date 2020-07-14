package com.loserico.jackson;

import com.fasterxml.jackson.annotation.JsonView;

public class ItemJsonView {

	@JsonView(Views.Private.class)
	public int id;

	@JsonView(Views.Public.class)
	public String itemName;

	@JsonView(Views.Internal.class)
	public String ownerName;

	public ItemJsonView(int id, String itemName, String ownerName) {
		this.id = id;
		this.itemName = itemName;
		this.ownerName = ownerName;
	}

	public ItemJsonView() {
	}

}