package com.loserico.concurrent.watchdog;

class Watchdog implements Runnable { //A timer that will run after five seconds
	private Thread t;
	private Parent parent;
	private int initialCount;
	
	public Watchdog(Parent parent, int count) { //make a  timer with a count, and access to the parent
		initialCount = count;
		this.parent = parent;
	}
	
	public void run() { //Timers logic
		try {
			Thread.sleep(5000); // If you want to change the time requirement, modify it here
			parent.restartChild(initialCount);
			
		} catch (InterruptedException e) {
			System.out.println("Error in watchdog thread");
		}
		
	}
	
	public void start() // start the timer
	{
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
}
