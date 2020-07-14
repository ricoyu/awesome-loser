package com.loserico.pattern.structural.facade;

/**
 * 话说土豪下班回到家里后首先要做的就是把灯打开，我们假设他一共需要打开三个灯，然后就是打开热水器烧水准备洗澡，在等待的过程还会打开电视机看新闻。
 * <p>
 * 在主函数里就要创建各种对象，并且调用他们的额open方法。我们看到主函数为了实现土豪下班回家这一个功能需要和三个电灯，
 * 一个热水器和一台电视机打交道，非常的复杂，所以这时候我们就应该使用门面模式。
 * <p>
 * Copyright: (C), 2020/2/12 10:04
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class Client {
	
	public static void main(String[] args) {
		Facade facade = new Facade();
		/**
		 * 一步操作就可以完成所有的准备工作
		 */
		facade.open();
	}
}
