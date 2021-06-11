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
     * 提交数据有误, 比如应该是布尔值的, 但是传了个"on", Jackson在转换成Boolean报错
     */
    BAD_REQUEST("4000000","template.bad.request", "请求参数不合法"),
    
    /**
     * 数据校验失败的时候msg取的是具体的校验失败的msg, 这里的"数据校验失败"其实是没有用的, 但是这里定义了error code
     */
    VALIDATION_FAIL("4000001","template.bad.request", "数据校验失败"),
    DUPLICATE_SUBMISSION("4000002", "duplicate.submit", "请勿重复提交"),
    
    /*
     * 安全相关
     */
    RSA_DECRYPT_FAIL("4010001", "template.rsa.decrypt.fail", "RSA解密失败"),
    
    TIMESTAMP_MISSING("4010003", "template.missing.timestamp.error", "缺少timestamp参数"),
    TIMESTAMP_MISMATCH("4010004", "template.timestamp.mismatch.error", "timestamp参数不匹配"),
    TIMESTAMP_INVALID("4010005", "template.timestamp.invalid.error", "timestamp参数必须是UNIX miliseconds"),
    
    TOKEN_MISSING("4010006", "template.missing.token.error", "请提供Token"),
    TOKEN_INVALID("4010007", "template.invalid.token.error", "无效的Token"),
    TOKEN_EXPIRED("4010008", "template.token.expired.error", "您尚未登录或者Token已过期, 请重新登录"),
    
    USERNAME_PASSWORD_MISMATCH("4010009", "template.username.password.error", "用户名或密码错误"),
    USERNAME_PASSWORD_MISSING("4010010", "template.username.password.missing", "请提供用户名或密码"),
    ACCOUNT_LOCKED("4010011", "template.account.locked", "账户已锁定"),
    ACCOUNT_DISABLED("4010012", "template.account.disabled", "账户已禁用"),
    ACCOUNT_EXPIRED("4010013", "template.account.expired", "账户已过期"),
    PASSWORD_EXPIRED("4010014", "template.password.expired", "密码已过期"),
    ALREADY_LOGIN("4010015", "template.already.login", "该账号已在别处登录, 不允许再次登录"),
    
    
    INVALID_URI_ACCESS("4030001", "template.invalid.uri.access", "访问的URI不合法"),
    ACCESS_DENIED("4030002", "template.access.denied", "你无权访问该资源"),
    
    NOT_FOUND("4040000", "template.not.found", "Not Found"),
    
    METHOD_NOT_ALLOWED("4050000", "template.method.notallowed", "Method not Allowed"),
    
    TOO_MANY_REQUESTS("4290000", "template.too.many.requests", "Too Many Requests"),
    
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
    
    @Override
    public String msgTemplate() {
        return this.msgTemplate;
    }
    
    
}
