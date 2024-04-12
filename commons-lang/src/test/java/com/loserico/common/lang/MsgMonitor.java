package com.loserico.common.lang;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息监听表
 * </p>
 *
 * @author forest
 * @since 2023年05月31日
 */
@Data
public class MsgMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 来源应用
     */
    private String sourceApp;

    /**
     * 主题
     */
    private String topic;

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
    private String msgData;

    /**
     * 消息内容中提取的映射字段
     */
    private String strField1;

    /**
     * 消息内容中提取的映射字段
     */
    private String strField2;

    /**
     * 消息内容中提取的映射字段
     */
    private String strField3;

    /**
     * 消息内容中提取的映射字段
     */
    private String strField4;

    /**
     * 消息内容中提取的映射字段
     */
    private Integer intField1;

    /**
     * 消息内容中提取的映射字段
     */
    private Integer intField2;

    /**
     * 消息内容中提取的映射字段
     */
    private Integer intField3;

    /**
     * 消息内容中提取的映射字段
     */
    private Integer intField4;

    /**
     * 大消息
     */
//    @TableField(value = "big_msg", typeHandler = BlobTypeHandler.class)
//    private byte[] bigMsg;

    /**
     * 发送状态:0-初始;1-发送成功;99-发送异常
     */
    private Integer msgStatus;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否逻辑删除（0：否 1：是）
     */
    private Integer isDelete;


}
