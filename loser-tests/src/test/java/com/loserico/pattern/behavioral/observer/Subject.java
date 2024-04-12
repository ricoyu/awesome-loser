package com.loserico.pattern.behavioral.observer;

/**
 * 被观察者接口
 * <p>
 * Copyright: Copyright (c) 2024-03-29 17:48
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}