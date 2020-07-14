package com.loserico.workbook.pojo;

import com.loserico.workbook.annotation.Col;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FaPiao {
	@Col(name = "发票号")
	private String ticketNo;
	
	@Col(name = "发票代码")
	private String ticketCode;
	
	@Col(name = "录入日期")
	private LocalDateTime inboundTime;
	
	@Col(name = "开票日期")
	private LocalDateTime ticketTime;
	
	@Col(name = "价税合计金额")
	private BigDecimal amount;
	
	@Col(name = "折扣金额")
	private BigDecimal discount;
	
	@Col(name = "税率")
	private Double taxRate;
	
	@Col(name = "税额")
	private BigDecimal tax;
	
	@Col(name = "发票类型")
	private String ticketType;
	
	@Col(name = "核销状态")
	private String status;
	
	@Col(name = "核销日期")
	private LocalDateTime settledDate;
}
