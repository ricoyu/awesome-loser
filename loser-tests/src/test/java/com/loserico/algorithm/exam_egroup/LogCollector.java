package com.loserico.algorithm.exam_egroup;

import java.util.Scanner;

/**
 * 日志采集系统 <p/>
 * 题目描述
 * <p/>
 * 日志采集是运维系统的的核心组件。日志是按行生成，每行记做一条，由采集系统分批上报。如果上报太频繁，会对服务端造成压力；如果上报太晚，会降低用户的体验；如果一次上报的条数太多，会导致超时失败。为此，项目组设计了如下的上报策略：
 * <p/>
 * 每成功上报一条日志，奖励1分
 * <p/>
 * 每条日志每延迟上报1秒，扣1分
 * <p/>
 * 积累日志达到100条，必须立即上报
 * <p/>
 * 给出日志序列，根据该规则，计算首次上报能获得的最多积分数。
 * <p/>
 * 输入描述:
 * <br/>
 * 按时序产生的日志条数 T1​,T2​...Tn​, 其中 1≤n≤1000,0≤Ti​≤100
 * <p/>
 * 输出描述:
 * <br/>
 * 首次上报最多能获得的积分数
 * <p/>
 * 示例1
 * <p/>
 * 输入: 1 98 1
 * <br/>
 * 输出: 98
 * <br/>
 * 说明：采集系统第2个时刻上报，可获得最大积分(98+1)-1=98<p/>
 * 示例2
 * <p/>
 * 输入: 50 60 1
 * <p/>
 * 输出: 50
 * <p/>
 * 说明:
 * <br/>
 * 如果第1个时刻上报，获得积分50。<br/>
 * 如果第2个时刻上报，最多上报100条，前50条延迟上报1s，每条扣除1分，共获得积分为 100-50=50。<br/>
 * <p>
 * Copyright: Copyright (c) 2024-09-10 11:24
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class LogCollector {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入日志序列: ");
		String input = scanner.nextLine();
		String[] inputs = input.split(" ");
		int[] logs = new int[inputs.length];
		for (int i = 0; i < inputs.length; i++) {
			logs[i] = Integer.parseInt(inputs[i]);
		}
		System.out.println(maxScoreFirstReport(logs));
	}

	public static int maxScoreFirstReport(int[] logs) {
		int n = logs.length;
		int maxScore = Integer.MIN_VALUE;// 初始化最大积分为最小整数，用于比较更新
		int totalLogs = 0; // 记录累计的日志条数

		// 遍历每个时刻的日志数
		for (int i = 0; i < n; i++) {
			totalLogs += logs[i]; // 累加当前时刻的日志数

			// 判断是否达到100条日志的条件
			if (totalLogs >= 100) {
				// 如果达到100条，计算当前时刻上报的积分
				int score = 100 - i;  // 每条日志每延迟上报1秒扣1分
				maxScore = Math.max(maxScore, score); // 更新最大积分
				break; // 达到100条日志后，按规则立即上报，不再考虑后续时刻
			} else {
				// 如果未达到100条，计算当前时刻上报的积分
				int score = totalLogs - i; // 同样每条日志每延迟1秒扣1分
				maxScore = Math.max(maxScore, score); // 更新最大积分
			}
		}

		return maxScore; // 返回计算出的最大积分
	}
}
