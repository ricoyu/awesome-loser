package com.loserico.pattern.chain;

/**
 * 文本长度检查
 * <p/>
 * Copyright: Copyright (c) 2024-11-18 20:55
 * <p/>
 * Company: Sexy Uncle Inc.
 * <p/>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
class LengthCheck implements TextHandler {
    private TextHandler nextHandler;

    @Override
    public void setNext(TextHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handleRequest(String text) {
        if (text.length() > 1000) {
            System.out.println("文本过长，请缩短后再提交");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(text);
        }
    }
}