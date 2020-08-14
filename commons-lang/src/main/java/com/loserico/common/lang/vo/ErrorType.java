package com.loserico.common.lang.vo;

/**
 *  
 * <p>
 * Copyright: Copyright (c) 2020-05-02 10:26
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public interface ErrorType {
    
    /**
     * 返回code
     *
     * @return
     */
    String getCode();

    /**
     * 返回msg
     *
     * @return
     */
    String getMsg();
    
    /**
     * 消息国际化模板
     * @return
     */
    default String getMsgTemplate() {
        return null;
    };
}