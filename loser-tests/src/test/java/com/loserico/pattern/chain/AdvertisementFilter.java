package com.loserico.pattern.chain;

/**
 * 广告内容过滤
 * <p/>
 * Copyright: Copyright (c) 2024-11-18 20:55
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
class AdvertisementFilter implements TextHandler {
    private TextHandler nextHandler;

    @Override
    public void setNext(TextHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handleRequest(String text) {
        if (text.contains("广告")) {
            System.out.println("广告内容已过滤");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(text);
        }
    }
}