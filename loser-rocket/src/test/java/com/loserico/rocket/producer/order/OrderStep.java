package com.loserico.rocket.producer.order;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2023-01-21 9:33
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@ToString
public class OrderStep {
	
	private Long orderId;
	
	private String desc;
	
	/**
	 * 1039 : 创建 付款 推送 完成
	 * 1065 : 创建 付款 
	 * 7235 : 创建 付款 完成
	 * @return
	 */
	public static List<OrderStep> buildOrders() {
		List<OrderStep> orders = new ArrayList<>();
		OrderStep orderStep = new OrderStep();
		orderStep.setOrderId(1039L);
		orderStep.setDesc("创建");
		orders.add(orderStep);
		
		orderStep = new OrderStep();
		orderStep.setOrderId(1065L);
		orderStep.setDesc("创建");
		orders.add(orderStep);
		
		orderStep = new OrderStep();
		orderStep.setOrderId(1039L);
		orderStep.setDesc("付款");
		orders.add(orderStep);
		
		orderStep = new OrderStep();
		orderStep.setOrderId(7235L);
		orderStep.setDesc("创建");
		orders.add(orderStep);
		
		orderStep = new OrderStep();
		orderStep.setOrderId(1065L);
		orderStep.setDesc("付款");
		orders.add(orderStep);
		
		orderStep = new OrderStep();
		orderStep.setOrderId(7325L);
		orderStep.setDesc("付款");
		orders.add(orderStep);
		
		orderStep = new OrderStep();
		orderStep.setOrderId(1039L);
		orderStep.setDesc("推送");
		orders.add(orderStep);
		
		orderStep = new OrderStep();
		orderStep.setOrderId(7235L);
		orderStep.setDesc("完成");
		orders.add(orderStep);
		
		orderStep = new OrderStep();
		orderStep.setOrderId(1039L);
		orderStep.setDesc("完成");
		orders.add(orderStep);
		
		return orders;
	}
}
