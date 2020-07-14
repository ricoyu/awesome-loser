package com.loserico.pattern.visitor.example;

/**
 * 违规行车的罚单
 * @author Loser
 * @since Jul 5, 2016
 * @version 
 *
 */
public class FpmTicket extends Ticket {
	private String offenceType;

	@Override
	public void accept(Vistor vistor) {
		vistor.visit(this);
	}

}