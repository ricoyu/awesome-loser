package com.loserico.jackson.deserialize;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RequestDTO implements Serializable {

    /**
     * 应用编号
     */
    private String appCode;
    /**
     * 主题
     */
    private String topic;

    private String operator;
    /**
     * 消息ID
     */
    private String msgId;
    /**
     * 业务ID
     */
    private String bizId;
    /**
     * 消息内容
     */
    private String payLoad;
    /**
     * 接收时间
     */
    private Date receiveTime;
}