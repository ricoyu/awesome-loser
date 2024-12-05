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

    FAIL("1","template.process.fail", "Fail"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR("2","template.internal.server.error", "服务器内部错误"),
    /*
     * 提交数据有误, 比如应该是布尔值的, 但是传了个"on", Jackson在转换成Boolean报错
     */
    BAD_REQUEST("3","template.bad.request", "请求参数不合法"),
    
    /**
     * 数据校验失败的时候msg取的是具体的校验失败的msg, 这里的"数据校验失败"其实是没有用的, 但是这里定义了error code
     */
    VALIDATION_FAIL("4","template.bad.request", "数据校验失败"),
    DUPLICATE_SUBMISSION("5", "duplicate.submit", "请勿重复提交"),
    MAX_UPLOAD_SIZE_EXCEEDED("6", "max.uploadsize.exceeded", "超出最大上传文件大小限制"),

    TOKEN_MISSING("7", "template.missing.token.error", "请提供Token"),
    TOKEN_INVALID("8", "template.invalid.token.error", "无效的Token"),
    OAUTH2_GET_TOKENKEY_ERROR("9", "template.get.tokenkey.error", "获取token key失败"),
    TOKEN_EXPIRED("10", "template.token.expired.error", "您尚未登录或者Token已过期, 请重新登录"),
    
    USERNAME_PASSWORD_MISMATCH("11", "template.username.password.error", "用户名或密码错误"),
    ACCOUNT_LOCKED("12", "template.account.locked", "账户已锁定"),
    ACCOUNT_DISABLED("13", "template.account.disabled", "账户已禁用"),
    ACCOUNT_EXPIRED("14", "template.account.expired", "账户已过期"),
    PASSWORD_EXPIRED("15", "template.password.expired", "密码已过期"),
    ALREADY_LOGIN("16", "template.already.login", "该账号已在别处登录, 不允许再次登录"),
    FIRST_LOGIN("17", "template.first.login", "该账号首次登录, 请修改密码!"),
	MISSING_AUTHORIZATION("18", "template.missing.authorization.header", "请提供Authorization请求头"),
    
    
    ACCESS_DENIED("40", "template.access.denied", "你无权访问该资源"),
    FLOW_EXCEPTION("41", "template.flow.control", "已被流控"),
    DEGRADE_EXCEPTION("42", "template.flow.degrade", "已被熔断"),
    HOT_PARAM_EXCEPTION("43", "template.hot.param", "已被热点参数限流"),
    SYSTEM_BLOCK_EXCEPTION("44", "template.system.control", "已被系统规则限流"),
    
    NOT_FOUND("45", "template.not.found", "Not Found"),
    
    //网管层找不对目标service
    GATEWAY_NOT_FOUND_SERVICE("46", "template.service.not.found", "Service Not Found"),
    
    METHOD_NOT_ALLOWED("47", "template.method.notallowed", "Method not Allowed"),
    
    TOO_MANY_REQUESTS("48", "template.too.many.requests", "Too Many Requests");

    
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
