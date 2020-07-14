package com.loserico.pattern.behavioral.observer;

/**
 * 显示当前天气的公告牌
 *
 * <p>
 * Copyright: (C), 2020/2/2 10:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class CurrentConditionsDisplay implements Observer, DisplayElement {
	
	private WeatherData weatherData;
	
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
	
	public CurrentConditionsDisplay(WeatherData weatherData) {
		this.weatherData = weatherData;
		weatherData.registerObserver(this);
	}
	
	@Override
	public void update() {
		this.temperature = this.weatherData.getTemperature();
		this.humidity = this.weatherData.getHumidity();
		this.pressure = this.weatherData.getPressure();
		display();
	}
	
	@Override
	public void display() {
		System.out.println("当前温度为：" + this.temperature + "℃");
		System.out.println("当前湿度为：" + this.humidity);
		System.out.println("当前气压为：" + this.pressure);
	}
}
