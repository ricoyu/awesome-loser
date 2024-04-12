package com.loserico.pattern.behavioral.observer;

/**
 * 观察者接口
 * <p>
 * Copyright: Copyright (c) 2024-03-29 17:47
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
interface Observer {
    void update(float temperature, float humidity, float pressure);
}