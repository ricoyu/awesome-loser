package com.loserico.pattern.command;

public class CommandClient {
	
	public static void main(String args[]) {
		DocumentReceiver documentReceiver = new DocumentReceiver(); //文档实体对象
		AddCommand addCmd = new AddCommand(documentReceiver); //具体命令实体对象
		UndoCommand undoCmd = new UndoCommand(documentReceiver); //具体命令实体对象
		RedoCommand redoCmd = new RedoCommand(documentReceiver); //具体命令实体对象
		Invoker invoker = new Invoker(); //调用者对象
		invoker.setCommand(addCmd);
		invoker.execute();
		invoker.setCommand(addCmd);
		invoker.execute();
		invoker.setCommand(undoCmd);
		invoker.execute();
		invoker.setCommand(redoCmd);
		invoker.execute();
	}
}