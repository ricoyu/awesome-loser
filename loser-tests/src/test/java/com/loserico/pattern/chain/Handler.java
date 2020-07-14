package com.loserico.pattern.chain;

public abstract class Handler {
	
	private Handler successor;

	public void setSuccessor(Handler successor) {
		this.successor = successor;
	}

	public abstract void HandleRequest(int request);
}