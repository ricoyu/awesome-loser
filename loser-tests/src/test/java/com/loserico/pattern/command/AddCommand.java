package com.loserico.pattern.command;

/**
* 执行命令实现类
*/
public class AddCommand implements Command {

	public DocumentReceiver doucmentReceiver;

	public AddCommand(DocumentReceiver document) {
		this.doucmentReceiver = document;
	}

	@Override
	public void execute() {
		this.doucmentReceiver.add();
	}
}