package com.loserico.pattern.chain;

/**
 * 敏感词过滤
 * <p/>
 * Copyright: Copyright (c) 2024-11-18 20:54
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
class SensitiveWordFilter implements TextHandler {
    private TextHandler nextHandler;

    @Override
    public void setNext(TextHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handleRequest(String text) {
        if (text.contains("敏感词")) {
            System.out.println("敏感词已过滤");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(text);
        }
    }
}