package com.loserico.pattern.behavioral.observer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Copyright: (C), 2020/2/2 10:21
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class WeatherData implements Subject {
	
	private Set<Observer> observers = new HashSet<>();
	
	/**
	 * 温度
	 */
	private float temperature;
	
	/**
	 * 湿度
	 */
	private float humidity;
	
	/**
	 * 气压
	 */
	private float pressure;
	
	/**
	 * 未来几天的温度
	 */
	private List<Float> forecastTemperatures;
	
	@Override
	public void registerObserver(Observer observer) {
		observers.add(observer);
	}
	
	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}
	
	@Override
	public void notifyObservers() {
		for (Observer observer : observers) {
			observer.update();
		}
	}
	
	public void measurementsChanged() {
		notifyObservers();
	}
	
	public void setMeasurements(float temperature,
	                            float humidity,
	                            float pressure,
	                            List<Float> forecastTemperatures) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		this.forecastTemperatures = forecastTemperatures;
		measurementsChanged();
	}
	
	public float getTemperature() {
		return temperature;
	}
	
	public float getHumidity() {
		return humidity;
	}
	
	public float getPressure() {
		return pressure;
	}
	
	public List<Float> getForecastTemperatures() {
		return this.forecastTemperatures;
	}
}
