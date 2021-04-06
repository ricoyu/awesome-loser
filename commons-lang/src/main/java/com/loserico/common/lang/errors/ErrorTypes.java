package com.loserico.common.lang.errors;

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
public enum ErrorTypes implements ErrorType {
    
    /**
     * 执行成功
     */
    SUCCESS("0","template.process.success", "Success"),
    
    /*
     * 提交数据有误
     */
    BAD_REQUEST("4000000","template.bad.request", "请求不合法"),
    
    /*
     * 安全相关
     */
    RSA_DECRYPT_FAIL("4010001", "template.rsa.decrypt.fail", "RSA解密失败"),
    MISSING_TIMESTAMP("4010003", "template.missing.timestamp.error", "缺少timestamp参数"),
    TIMESTAMP_MISMATCH("4010004", "template.timestamp.mismatch.error", "timestamp参数不匹配"),
    MISSING_TOKEN("4010005", "template.missing.token.error", "请提供access_token"),
    INVALID_TOKEN("4010006", "template.invalid.token.error", "无效的access_token"),
    TOKEN_EXPIRED("4010007", "template.token.expired.error", "您尚未登录或者Token已过期, 请重新登录"),
    
    USERNAME_PASSWORD_MISMATCH("4010008", "template.username.password.error", "用户名或密码错误"),
    ACCOUNT_LOCKED_CODE("4010009", "template.account.locked", "账户已锁定"),
    ACCOUNT_DISABLED("4010010", "template.account.disabled", "账户已禁用"),
    ACCOUNT_EXPIRED("4010011", "template.account.expired", "账户已过期"),
    PASSWORD_EXPIRED("4010012", "template.password.expired", "密码已过期"),
    
    
    INVALID_ACCESS("4030001", "template.invalid.uti.access", "访问的URI不合法"),
    ACCESS_DENIED("4030002", "template.access.denied", "你无权访问该资源"),
    
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR("5000000","template.internal.server.error", "服务器内部错误");
    
    /**
     * 错误类型码
     */
    private String code;
    
    /**
     * 国际化消息模板
     */
    private String msgTemplate;
    
    /**
     * 错误类型描述信息
     */
    private String msg;

    private ErrorTypes(String code, String msgTemplate, String msg) {
        this.code = code;
        this.msgTemplate = msgTemplate;
        this.msg = msg;
    }
    
    
    @Override
    public String code() {
        return this.code;
    }
    
    @Override
    public String message() {
        return this.msg;
    }
}
