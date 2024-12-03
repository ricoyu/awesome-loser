package com.loserico.pattern.chain;

public class Client {
    public static void main(String[] args) {
        TextHandler sensitiveWordFilter = new SensitiveWordFilter();
        TextHandler advertisementFilter = new AdvertisementFilter();
        TextHandler lengthCheck = new LengthCheck();

        // 设置责任链
        sensitiveWordFilter.setNext(advertisementFilter);
        advertisementFilter.setNext(lengthCheck);

        // 发送请求
        sensitiveWordFilter.handleRequest("这是一段包含广告的文本");
    }
}