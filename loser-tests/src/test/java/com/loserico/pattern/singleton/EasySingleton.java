package com.loserico.pattern.singleton;

/**
 * 我们可以通过EasySingleton.INSTANCE来访问实例, 这比调用getInstance()方法简单多了。
 * 创建枚举默认就是线程安全的, 所以不需要担心double checked locking, 而且还能防止反序列化导致重新创建新的对象。
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 * @on
 */
public enum EasySingleton {
	INSTANCE;
}