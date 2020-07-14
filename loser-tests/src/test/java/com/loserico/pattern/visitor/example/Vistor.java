package com.loserico.pattern.visitor.example;

public abstract class Vistor {
	public abstract void visit(FpmTicket ticket);

	public abstract void visit(FppTicket ticket);
}