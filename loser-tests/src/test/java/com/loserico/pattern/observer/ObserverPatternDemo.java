package com.loserico.pattern.observer;

public class ObserverPatternDemo {

    public static void main(String[] args) {
        WeatherStation weatherStation = new WeatherStation();
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay();

        weatherStation.registerObserver(currentDisplay);

        weatherStation.setMeasurements(28.2f, 65f, 1013.1f);
        weatherStation.setMeasurements(22.9f, 70f, 1012.0f);
    }
}
