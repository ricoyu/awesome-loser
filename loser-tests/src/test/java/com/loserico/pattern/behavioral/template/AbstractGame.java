package com.loserico.pattern.behavioral.template;

/**
 * <p>
 * Copyright: (C), 2020/2/3 10:57
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public abstract class AbstractGame {
	
	/*
	 * 模板方法
	 */
	public final void play() {
		initialize();
		startPlay();
		endPlay();
	}
	
	/*
	 * 初始化游戏
	 */
	abstract void initialize();
	
	/*
	 * 开始游戏
	 */
	abstract void startPlay();
	
	/*
	 * 结束游戏
	 */
	abstract void endPlay();
}
