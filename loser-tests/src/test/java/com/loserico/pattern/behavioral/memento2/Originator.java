package com.loserico.pattern.behavioral.memento2;

/**
 * This is the Originator class whose object state should be saved in a memento.
 * 
 * The class contains two double type’s fields x and y, and also takes a reference
 * of a CareTaker. The CareTaker is used to save and retrieve the memento objects
 * which represent the state of the Originator object.
 * 
 * @author Loser
 * @since Aug 18, 2016
 * @version
 *
 */
public class Originator {

	private double x;
	private double y;
	private String lastUndoSavepoint;
	private CareTaker careTaker;

	public Originator(double x, double y, CareTaker careTaker) {
		this.x = x;
		this.y = y;
		this.careTaker = careTaker;
		createSavepoint("INITIAL");
	}

	public void createSavepoint(String savepointName) {
		careTaker.saveMemento(new Memento(this.x, this.y), savepointName);
		lastUndoSavepoint = savepointName;
	}

	public void undo() {
		setOriginatorState(lastUndoSavepoint);
	}

	public void undo(String savepointName) {
		setOriginatorState(savepointName);
	}

	public void undoAll() {
		setOriginatorState("INITIAL");
		careTaker.clearSavepoints();
	}

	private void setOriginatorState(String savepointName) {
		Memento mem = careTaker.getMemento(savepointName);
		this.x = mem.getX();
		this.y = mem.getY();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "X: " + x + ", Y: " + y;
	}

}
