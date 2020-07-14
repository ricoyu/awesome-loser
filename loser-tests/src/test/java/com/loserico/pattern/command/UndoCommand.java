package com.loserico.pattern.command;

/**
* 撤销命令实现类
*/
public class UndoCommand implements Command {
	/**
	* 命令接受者对象
	*/
	public DocumentReceiver documentReceiver;

	/**
	* 构造方法
	* @param documentReceiver
	*/
	public UndoCommand(DocumentReceiver documentReceiver) {
		this.documentReceiver = documentReceiver;
	}

	@Override
	public void execute() {
		this.documentReceiver.undo();
	}
}