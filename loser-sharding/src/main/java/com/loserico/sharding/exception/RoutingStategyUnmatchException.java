package com.loserico.sharding.exception;

import lombok.Data;

import static com.loserico.sharding.enumeration.RoutingErrors.LOADING_STATEGY_UN_MATCH;

/**
 * 加载路由策略和配置配置文件不匹配 
 * <p>
 * Copyright: Copyright (c) 2020-02-14 18:45
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class RoutingStategyUnmatchException extends RoutingException {

    public RoutingStategyUnmatchException() {
        super(LOADING_STATEGY_UN_MATCH.getMsg());
        setErrorCode(LOADING_STATEGY_UN_MATCH.getCode());
        setErrorMsg(LOADING_STATEGY_UN_MATCH.getMsg());

    }
}