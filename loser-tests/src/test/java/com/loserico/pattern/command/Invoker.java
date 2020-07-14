package com.loserico.pattern.command;

/**
* 提供给客户端的命令调用方法
* @author feng
*
*/
public class Invoker {
	/**
	* 命令对象
	*/
	public Command command;

	/**
	* 命令设置方法
	* @param command
	*/
	public void setCommand(Command command) {
		this.command = command;
	}

	/**
	* 命令执行方法
	*/
	public void execute() {
		this.command.execute();
	}
}