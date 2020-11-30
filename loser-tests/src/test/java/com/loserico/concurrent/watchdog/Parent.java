package com.loserico.concurrent.watchdog;

public class Parent {
	
	private Child child;
	
	public Parent() {
		child = new Child(this);
		child.start();
	}
	
	public void report(int count) { //Starts a new watchdog timer
		Watchdog restartTimer = new Watchdog(this, count);
		restartTimer.start();
	}
	
	public void restartChild(int currentCount) {
		if (currentCount == child.getCount()) { //Check if the count has not changed
			//If it hasn't
			child.kill();
			child.start();
		}
		
	}
	
	public static void main(String[] args) {
		//Start up the parent function, it spawns the child
		new Parent();
	}
	
}
