package com.loserico.jackson.autotype;

import com.loserico.jackson.inherit.*;

import java.util.*;

/**
 * <p>
 * Copyright: (C), 2021-01-17 19:39
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class AutoTypedDemo {
	
	public static void main(String[] args) {
		Car car = new Car("Mercedes-Benz", "S500", 5, 250.0);
		Car car2 = new Car("特斯拉", "T500", 5, 180.0);
		List<Object> cars = Arrays.asList(car, car2);
		Class<? extends List> aClass = cars.getClass();
		System.out.println(aClass);
	}
}
