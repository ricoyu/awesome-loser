package org.loser.cache;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NetFlowBean implements Serializable {
    
    /**
     * 引擎输出到Redis的时间点
     */
    private Long ts;
    
    /**
     * 网口名
     */
    @JsonProperty("mesh_port")
    private String meshPort;
    
    /**
     * 收到的报文数量
     */
    @JsonProperty("pkts_recvd")
    private Long pktsRecvd;
    
    /**
     * 丢弃的报文数量
     */
    @JsonProperty("pkts_drooped")
    private Long pktsDrooped;
    
    /**
     * 接收到的字节数量
     */
    @JsonProperty("bytes_recvd")
    private Long bytesRecvd;

}
