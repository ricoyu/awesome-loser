package com.loserico.pattern.command;

/**
* 恢复命令实现类
*/
public class RedoCommand implements Command {
	/**
	* 命令接受者对象
	*/
	public DocumentReceiver documentReceiver;

	/**
	* 构造方法
	* @param documentReceiver
	*/
	public RedoCommand(DocumentReceiver documentReceiver) {
		this.documentReceiver = documentReceiver;
	}

	@Override
	public void execute() {
		this.documentReceiver.redo();
	}
}