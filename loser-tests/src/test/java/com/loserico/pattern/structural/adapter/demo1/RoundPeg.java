package com.loserico.pattern.structural.adapter.demo1;

/**
 * 圆钉 
 * <p>
 * Copyright: Copyright (c) 2023-02-27 8:47
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class RoundPeg {
    private double radius;

    public RoundPeg() {}

    public RoundPeg(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}
