package com.loserico.jackson.inherit;

public class Truck extends Vehicle {
	
	private double payloadCapacity;
	
	public Truck() {
	}
	
	public Truck(String make, String model, double payloadCapacity) {
		super(make, model);
		this.payloadCapacity = payloadCapacity;
	}
	
	public double getPayloadCapacity() {
		return payloadCapacity;
	}
	
	public void setPayloadCapacity(double payloadCapacity) {
		this.payloadCapacity = payloadCapacity;
	}
}
