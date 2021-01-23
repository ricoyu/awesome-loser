package com.loserico.jackson.inherit;

import lombok.*;

@Data
public class Car extends Vehicle {
    
    private int seatingCapacity;
    private double topSpeed;
    
    public Car() {
        
    }
    
    public Car(String make, String model, int seatingCapacity, double topSpeed) {
        super(make, model);
        this.seatingCapacity = seatingCapacity;
        this.topSpeed = topSpeed;
    }
    
    public int getSeatingCapacity() {
        return seatingCapacity;
    }
    
    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }
    
    public double getTopSpeed() {
        return topSpeed;
    }
    
    public void setTopSpeed(double topSpeed) {
        this.topSpeed = topSpeed;
    }
}

