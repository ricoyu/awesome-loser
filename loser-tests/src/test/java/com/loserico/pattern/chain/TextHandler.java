package com.loserico.pattern.chain;

interface TextHandler {
    void setNext(TextHandler nextHandler);
    void handleRequest(String text);
}