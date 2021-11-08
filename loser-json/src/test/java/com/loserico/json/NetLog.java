package com.loserico.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NetLog implements ProtoTypeAware, Comparable<NetLog> {
	
	/**
	 * 日志编号
	 */
	private String id;
	
	/**
	 * 探针唯一id
	 */
	@JsonProperty("dev_id")
	private String devId;
	
	/**
	 * 事件发生时间, 时间戳形式
	 */
	@JsonProperty("create_time")
	private Long createTime;
	
	/**
	 * 这个字段是为了确定存储到哪个索引里面
	 */
	@JsonIgnore
	private String netLogDate;
	
	/**
	 * 展示协议类型, 如 http
	 * 取 proto_type
	 */
	@JsonProperty("proto_type")
	private ProtoType protoType;
	
	/**
	 * 传输层协议, 如 tcp
	 * <p>
	 * 取 proto
	 */
	@JsonProperty("proto")
	private String proto;
	
	/**
	 * 目的IP地址
	 * 取 dest_ip
	 */
	@JsonProperty("dst_ip")
	private String dstIp;
	
	/**
	 * 目的IP地址端口  受害端口
	 * 取 dest_port
	 */
	@JsonProperty("dst_port")
	private Integer dstPort;
	
	/**
	 * 来源IP地址
	 */
	@JsonProperty("src_ip")
	private String srcIp;
	
	/**
	 * 来源IP地址端口
	 */
	@JsonProperty("src_port")
	private Integer srcPort;
	
	/**
	 * 流编号
	 */
	@JsonProperty("flow_id")
	private String flowId;
	
	/**
	 * 逗号隔开的网卡名
	 */
	@JsonProperty("in_iface")
	private String inIface;
	
	// ---------------- 下面的都是后端补全 ----------------
	
	/**
	 * 级联ID  TODO ETL-设备表
	 */
	@JsonProperty("branch_id")
	private String branchId;
	
	/**
	 * 探针设备IP地址  TODO 后端补全
	 */
	@JsonProperty("dev_ip")
	private String devIp;
	
	/**
	 * 目的IP地址所属市
	 */
	@JsonProperty("dst_city")
	private String dstCity;
	
	/**
	 * 目的IP地址所属国家
	 */
	@JsonProperty("dst_country")
	private String dstCountry;
	
	/**
	 * 目的IP地址所属省份
	 */
	@JsonProperty("dst_province")
	private String dstProvince;
	
	/**
	 * 目的ip所属纬度
	 */
	@JsonProperty("dst_location_lat")
	private String dstLocationlat;
	
	/**
	 * 目的ip所属经度
	 */
	@JsonProperty("dst_location_lon")
	private String dstLocationLon;
	
	
	/**
	 * 来源IP地址所属市
	 */
	@JsonProperty("src_city")
	private String srcCity;
	
	/**
	 * 来源IP地址所属国家
	 */
	@JsonProperty("src_country")
	private String srcCountry;
	
	/**
	 * 来源IP地址所属省份
	 */
	@JsonProperty("src_province")
	private String srcProvince;
			
	/**
	 * 来源ip所属纬度
	 */
	@JsonProperty("src_location_lat")
	private String srcLocationlat;
	
	/**
	 * 来源ip所属经度
	 */
	@JsonProperty("src_location_lon")
	private String srcLocationLon;
	
	/**
	 * 具体协议字段
	 */
	private Object data;
	
	/**
	 * 日志行为分类  TODO 先空着
	 */
	private String action;
	
	/**
	 * 日志行为结果 TODO 先空着
	 */
	@JsonProperty("action_result")
	private String actionResult;
	
	private Asset asset;
	
	@Override
	public int compareTo(NetLog netLog) {
		if (netLog == null) {
			return -1;
		}
		
		if (this.createTime < netLog.getCreateTime()) {
			return -1;
		}
		
		if (this.createTime > netLog.getCreateTime()) {
			return 1;
		}
		return 0;
	}
}
