package com.loserico.common.lang;

/**
 * 消息类型
 * <p>
 * Copyright: (C), 2023-12-18 17:26
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum MsgType {
	
	/**
	 * 无任务通讯时固定时间触发, 心跳报文
	 * 报文示例: <STX>11;ML001;1;LIFT;evt=LIF;<ETX>
	 */
	LIF(0),
	
	/**
	 * 收到需要ACKN的报文时回复的报文类型
	 */
	ACK(2),
	
	/**
	 * 发送新的运行任务
	 */
	MSG(0),
	
	/**
	 * PLC收到任务格式错误时回复的报文类型
	 */
	LOG(1),
	
	/**
	 * PLC执行任务结束时回复的报文类型
	 * 表示这个是一个任务状态上报
	 */
	REP(1),
	
	/**
	 * 通讯刚连接上或发任务之前或定时
	 * 我给提升机一个STR类型报文, 提升机反馈一个STM类型报文
	 */
	STR(0),
	
	/**
	 * 提升机在收到STR报文后反馈一个STM类型报文
	 */
	STM(1),
	
	/**
	 * 警告及故障报文
	 * 报警信号有变化时PLC发送的报文类型
	 */
	ERR(1);
	
	/**
	 * <ul>报文的方向
	 *     <li/>0 表示RCS-->PLCS
	 *     <li/>1 表示PLCS-->RCS
	 *     <li/>2 表示双向都可以
	 * </ul>
	 *
	 */
	private int direction;
	
	private MsgType(int direction) {
		this.direction = direction;
	}
	
}
