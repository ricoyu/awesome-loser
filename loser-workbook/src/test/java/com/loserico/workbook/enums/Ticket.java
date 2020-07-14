package com.loserico.workbook.enums;

/**
 * <p>
 * Copyright: Copyright (c) 2019-10-15 14:05
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum Ticket {

	ALL(-1, "所有", "所有"),
	PURCHASE_ORDER(2, "采购单", "采购入库单"),
	PURCHASE_RETURNED(40, "采购退货单", "新退货"),
	SALES_RETURN(12, "售后退货", "售后退货"),
	SALES_RETURN_ITEM(1201, "售后退货单明细"),
	REBATE(22, "返点单", "返点"),
	SETTLEMENT(23, "结算单", "结算单"),
	SETTLEMENT_ITEM(2301, "结算单项", "结算单项"),
	OTHER(26, "其他扣款", "其他扣款"),
	DSDJ(27, "代收代缴", "代收代缴"),
	GUARANTEE_MONEY(28, "质保金", "质保金"),
	YFKZYD(31, "预付款转移单", "预付款转移单"),
	PURCHASE_RETURNED_OLD(6, "采购退货单（旧）", "采购退货单(旧)"),
	TC_FEE(44, "TC运费", "TC运费"),
	PURCHASE_LOT(55, "采购批次单", "采购批次单"),
	SERVICE_CAR(60, "服务直通车", "服务直通车"),
	SXSJ(70, "实销实结销售单", "实销实结销售单"),
	SXSJ_THRKD(71, "实销实结退货入库单", "实销实结退货入库单"),
	GYKCGD(80, "公益款采购单", "公益款采购单"),
	TSTMFY(90, "图书贴码费用", "图书贴码费用"),
	ADV_FEE(100, "广告费", "广告费"),
	WQZYXSD(130, "物权转移销售单", "物权转移销售单"),
	WQZYTHD(131, "物权转移退货单", "物权转移退货单"),
	YB_FEE(140, "延保费用", "延保费用"),
	YB_FEE_RETURN(141, "延保费用退货", "延保费用退货"),
	TAX(160, "税差单", "税差单");

	private int code;
	private String desc;
	private String alias;

	private Ticket(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private Ticket(int code, String desc, String alias) {
		this.code = code;
		this.desc = desc;
		this.alias = alias;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String toString() {
		return desc;
	}

	public static Ticket getTicketByCode(int code) {
		for (Ticket ticket : Ticket.values()) {
			if (ticket.getCode() == code) {
				return ticket;
			}
		}
		return null;
	}
}