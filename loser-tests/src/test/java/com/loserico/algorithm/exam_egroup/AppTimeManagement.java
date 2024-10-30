package com.loserico.algorithm.exam_egroup;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 题目描述
 * <p/>
 * 智能手机方便了我们生活的同时，也侵占了我们不少的时间。
 * <p/>
 * “手机App防沉迷系统”能够让我们每天合理的规划手机App使用时间，在正确的时间做正确的事。它的大概原理是这样的:
 * <p/>
 * 1、在一天24小时内，可注册每个App的允许使用时段;
 * <p/>
 * 2、一个时段只能使用一个App，举例说明:不能同时在09:00-10:00注册App2和App3;
 * <p/>
 * 3、App有优先级，数值越高，优先级越高。注册使用时段时，如果高优先级的App时间和低优先级的时段有冲突，则系统会自动注销低优先级的时段;如果App的优先级相同，则后添加的 App不能注册。
 * <p/>
 * 举例1:
 * <br/>
 * (1)注册App3前:
 * <p/>
 * (2)App3注册时段和App2有冲突:
 * <p/>
 * (3)App3优先级高，系统接受App3的注册，自动注销App2的注册:
 * <p/>
 * 举例2:
 * <p/>
 * (1)注册App4:
 * <p/>
 * (2)App4和App2及App3都有冲突，优先级比App2高，但比 App3低，这种场景下App4注册不上，最终的注册效果如下:
 * <p/>
 * 4、一个App可以在一天内注册多个时段。
 * <p/>
 * 请编程实现，根据输入数据注册App，并根据输入的时间点，返回该时间点可用的App名称，如果该时间点没有注册任何App，请返回字符串"NA"。
 * <p/>
 * 输入描述
 * <p/>
 * 输入分3部分:
 * <p/>
 * 第一行表示注册的App数N(N≤100);
 * <p/>
 * 第二部分包括N行，每行表示一条App注册数据;
 * <p/>
 * 最后一行输入一个时间点，程序即返回该时间点的可用App。
 *
 * <pre> {@code
 * 2
 *
 * App1 1 09:00 10:00
 *
 * App2 2 11:00 11:30
 *
 * 09:30
 * }</pre>
 * <p>
 * 数据说明如下:
 * <p/>
 * 1、N行注册数据以空格分隔，四项数据依次表示:App名称、优先级、起始时间、结束时间
 * <p/>
 * 2、优先级1-5，数字值越大，优先级越高
 * <p/>
 * 3、时间格式HH:MM，小时和分钟都是两位，不足两位前面补0
 * <p/>
 * 4、起始时间需小于结束时间，否则注册不上
 * <p/>
 * 5、注册信息中的时间段包含起始时间点，不包含结束时间点
 * <p/>
 * 输出描述
 * <p/>
 * 输出一个字符串，表示App名称，或NA表示空闲时间。
 * <p/>
 * 补充说明
 * <p/>
 * 1、用例保证时间都介于00:00-24:00之间;
 * <p/>
 * 2、用例保证数据格式都是正确的，不用考虑数据输入行数不够、注册信息不完整、字符串非法、优先级超限、时间格式不正确的问题。
 * <p/>
 * 示例1
 * <p/>
 * 输入
 * <p/>
 * <pre> {@code
 * 1
 * App1 1 09:00 10:00
 * 09:30
 * }</pre>
 * <p>
 * 输出: App1
 * <p/>
 * 说明 <br/>
 * App1注册在9点到10点间，9点半可用的应用名是App1
 * <p/>
 * <p>
 * 示例2
 * <p/>
 * 输入
 *
 * <pre> {@code
 * 2
 * App1 1 09:00 10:00
 * App2 2 09:10 09:30
 * 09:20
 * }</pre>
 * <p/>
 * 输出: App2
 * <p/>
 * 说明 <br/>
 * App1和App2的时段有冲突，App2的优先级比App1高，注册App2后，系统将App1的注册信息自动注销后，09:20时刻可用应用名是 App2.
 * <p/>
 * <p>
 * 示例3
 * <p/>
 * 输入
 *
 * <pre> {@code
 * 2
 * App1 1 09:00 10:0
 * App2 2 09:10 09:30
 * 09:50
 * }</pre>
 * 输出: NA
 * <p/>
 * 说明: App1被注销后，09:50时刻没有应用注册，因此输出NA。
 * <p>
 * Copyright: Copyright (c) 2024-09-12 12:11
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class AppTimeManagement {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入注册的App数: ");
		// 读取App数量
		int n = scanner.nextInt();
		scanner.nextLine(); //清理scanner缓存
		List<App> apps = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			System.out.print("请输入第" + (i + 1) + "条App注册数据: ");
			String input = scanner.nextLine().trim();
			String[] registration = input.split(" ");
			App newApp = new App(registration[0], Integer.parseInt(registration[1]), registration[2], registration[3]);
			registerApp(apps, newApp);
		}

		System.out.print("请输入一个时间点: ");
		String queryTime = scanner.nextLine().trim();
		int queryMonutes = convertToMinutes(queryTime);
		System.out.println(findAppAtTime(apps, queryMonutes));
		scanner.close();
	}

	/**
	 * 注册App，处理时间冲突
	 *
	 * @param apps
	 * @param newApp
	 */
	public static void registerApp(List<App> apps, App newApp) {
		List<App> toRemove = new ArrayList<>();
		boolean shouldAdd = true;
		for (App app : apps) {
			if (app.isConflict(newApp)) {
				if (app.getPriority() < newApp.getPriority()) {
					toRemove.add(app);
				} else if (app.getPriority() > newApp.getPriority()) {
					shouldAdd=false;
					break; // 不注册新App
				}
				// 相同优先级的情况下，不做任何操作，因为后添加的App不能注册
			}
		}

		apps.removeAll(toRemove);
		if (shouldAdd) {
			apps.add(newApp);
		}
	}

	/**
	 * 找到特定时间的可用App
	 *
	 * @param apps
	 * @param time
	 * @return String
	 */
	public static String findAppAtTime(List<App> apps, int time) {
		for (App app : apps) {
			if (app.getStartTime() <= time && app.getEndTime() > time) {
				return app.getName();
			}
		}
		return "NA";
	}

	// 将时间HH:MM格式转换为一天中的分钟数
	public static int convertToMinutes(String time) {
		String[] splits = time.split(":");
		return Integer.parseInt(splits[0]) * 60 + Integer.parseInt(splits[1]);
	}
}


@Data
class App {
	private String name;
	private int priority;
	private int startTime; // 以分钟计的起始时间
	private int endTime; // 以分钟计的结束时间

	/**
	 * @param name     名字
	 * @param priority 优先级
	 * @param start    开始时间
	 * @param end      结束时间
	 */
	public App(String name, int priority, String start, String end) {
		this.name = name;
		this.priority = priority;
		this.startTime = convertToMinutes(start);
		this.endTime = convertToMinutes(end);
	}


	// 检查时间是否冲突
	public boolean isConflict(App other) {
		return !(this.startTime >= other.endTime || this.endTime <= other.startTime);
	}


	// 将时间HH:MM格式转换为一天中的分钟数
	private int convertToMinutes(String time) {
		String[] splits = time.split(":");
		return Integer.parseInt(splits[0]) * 60 + Integer.parseInt(splits[1]);
	}
}
