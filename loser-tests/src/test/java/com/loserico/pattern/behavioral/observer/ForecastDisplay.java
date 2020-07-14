package com.loserico.pattern.behavioral.observer;

import java.util.List;

/**
 * 显示未来几天天气的公告牌ForecastDisplay
 *
 * <p>
 * Copyright: (C), 2020/2/2 10:34
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ForecastDisplay implements Observer, DisplayElement {
	
	private WeatherData weatherData;
	
	/**
	 * 未来几天的温度
	 */
	private List<Float> forecastTemperatures;
	
	public ForecastDisplay(WeatherData weatherData) {
		this.weatherData = weatherData;
		this.weatherData.registerObserver(this);
	}
	
	@Override
	public void display() {
		System.out.println("未来几天的气温");
		int count = forecastTemperatures.size();
		for (int i = 0; i < count; i++) {
			System.out.println("第" + i + "天:" + forecastTemperatures.get(i) + "℃");
		}
	}
	
	@Override
	public void update() {
		this.forecastTemperatures = this.weatherData.getForecastTemperatures();
		display();
	}
}
