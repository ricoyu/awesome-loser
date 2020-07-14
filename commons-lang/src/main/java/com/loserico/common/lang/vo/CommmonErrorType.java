package com.loserico.common.lang.vo;

/**
 * 定义最常用的一些ErrorType 
 * <p>
 * Copyright: Copyright (c) 2020-06-14 11:03
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum CommmonErrorType implements ErrorType {

    INTERNAL_SERVER_ERROR("000001","服务器内部错误");

    /**
     * 错误类型码
     */
    private String status;
    /**
     * 错误类型描述信息
     */
    private String msg;

    CommmonErrorType(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    
    
    @Override
    public String getCode() {
        return this.getCode();
    }
    
    @Override
    public String getMsg() {
        return this.msg;
    }
}