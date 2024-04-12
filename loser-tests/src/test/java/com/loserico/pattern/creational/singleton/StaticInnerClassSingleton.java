package com.loserico.pattern.creational.singleton;

public class StaticInnerClassSingleton {

    private static class SingletonHolder {

        private static final StaticInnerClassSingleton SINGLETON = new StaticInnerClassSingleton();
    }

    private StaticInnerClassSingleton(){}

    public static StaticInnerClassSingleton getInstance() {
        return SingletonHolder.SINGLETON;
    }
}
