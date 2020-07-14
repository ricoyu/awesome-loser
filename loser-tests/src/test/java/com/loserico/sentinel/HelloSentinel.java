package com.loserico.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright: (C), 2020/3/19 17:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class HelloSentinel {
	
	public static void main(String[] args) {
		initFlowRule();
		while (true) {
			Entry entry = null;
			try {
				entry = SphU.entry("Hello World");
				/*您的业务逻辑 - 开始*/
				System.out.println("hello world");
				/*您的业务逻辑 - 结束*/
			} catch (BlockException e) {
				/*流控逻辑处理 - 开始*/
				System.out.println("block!");
				/*流控逻辑处理 - 结束*/
			} finally {
				if (entry != null) {
					entry.exit();
				}
			}
		}
	}
	
	private static void initFlowRule() {
		List<FlowRule> rules = new ArrayList<>();
		//创建流控规则对象
		FlowRule rule = new FlowRule();
		//设置受保护的资源
		rule.setResource("Hwllo World");
		//设置流控规则 QPS
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		//设置受保护的资源的阈值 set limit QPS to 20
		rule.setCount(20);
		rules.add(rule);
		//加载配置好的规则
		FlowRuleManager.loadRules(rules);
	}
}
