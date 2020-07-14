package com.loserico.java8.stream.async.model;

import java.io.Serializable;

public class Client implements Serializable {
	private static final long serialVersionUID = -6358742378177948329L;

	private String id;
	private double purchases;

	public Client() {
	}

	public Client(String id, double purchases) {
		this.id = id;
		this.purchases = purchases;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPurchases() {
		return purchases;
	}

	public void setPurchases(double purchases) {
		this.purchases = purchases;
	}
}