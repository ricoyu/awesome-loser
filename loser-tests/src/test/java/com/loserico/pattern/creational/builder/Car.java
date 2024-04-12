package com.loserico.pattern.creational.builder;

import lombok.Data;

@Data
public class Car {
    private final String engine;
    private final int wheels;
    private final String color;
    private final String brand;
    private final boolean hasSunroof;

    // 私有化构造函数，防止外部直接创建Car实例
    private Car(Builder builder) {
        this.engine = builder.engine;
        this.wheels = builder.wheels;
        this.color = builder.color;
        this.brand = builder.brand;
        this.hasSunroof = builder.hasSunroof;
    }

    @Override
    public String toString() {
        return "Car{" +
                "engine='" + engine + '\'' +
                ", wheels=" + wheels +
                ", color='" + color + '\'' +
                ", brand='" + brand + '\'' +
                ", hasSunroof=" + hasSunroof +
                '}';
    }

    // 静态方法用于获取Builder实例
    public static Builder builder() {
        return new Builder();
    }

    // Builder静态内部类
    public static class Builder {
        private String engine;
        private int wheels;
        private String color = "Black"; // 默认值
        private String brand;
        private boolean hasSunroof = false; // 默认值

        public Builder engine(String engine) {
            this.engine = engine;
            return this;
        }

        public Builder wheels(int wheels) {
            this.wheels = wheels;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder sunroof(boolean hasSunroof) {
            this.hasSunroof = hasSunroof;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
