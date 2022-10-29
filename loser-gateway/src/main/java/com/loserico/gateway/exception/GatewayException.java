package com.loserico.cloud.exception;

import com.loserico.common.lang.errors.ErrorTypes;
import lombok.Data;

@Data
public class GatewayException extends RuntimeException {

    private String code;

    private String msg;

    public GatewayException(ErrorTypes errorType) {
        this.code = errorType.code();
        this.msg = errorType.message();
    }

}
