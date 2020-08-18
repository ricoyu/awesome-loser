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
public enum CommonErrorType implements ErrorType {
    
    /**
     * 
     */
    INTERNAL_SERVER_ERROR("50001","服务器内部错误");

    /**
     * 错误类型码
     */
    private String code;
    
    /**
     * 错误类型描述信息
     */
    private String msg;

    CommonErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    
    @Override
    public String getCode() {
        return this.code;
    }
    
    @Override
    public String getMsg() {
        return this.msg;
    }
}