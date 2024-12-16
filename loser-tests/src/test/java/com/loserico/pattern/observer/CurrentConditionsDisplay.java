package com.loserico.pattern.observer;

/**
 * 具体的观察者
 * <p>
 * Copyright: Copyright (c) 2024-03-29 17:49
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
class CurrentConditionsDisplay implements Observer {
    private float temperature;
    private float humidity;
    // 可以添加对Subject的引用，如果需要的话

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }

    public void display() {
        System.out.println("Current conditions: " + temperature 
            + "C degrees and " + humidity + "% humidity");
    }
}
