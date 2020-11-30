package com.loserico.concurrent.watchdog;

public class Child implements Runnable {
	
	private Thread t;
	public int counter = 0;
	private boolean running;
	
	private Parent parent; // Record the parent function
	
	public Child(Parent parent) {
		this.parent = parent;
	}
	
	private void initializeAll() {
		counter = 0;
		running = true;
	}
	
	public int getCount() {
		return counter;
	}
	
	@Override
	public void run() {
		while ((counter <= 100) && (running)) {
			//The main logic for child
			counter += 1;
			System.out.println(counter);
			parent.report(counter); // Report a new count every two seconds
			
			try {
				Thread.sleep(2000); // Wait two seconds
			} catch (InterruptedException e) {
				System.out.println("Thread Failed");
			}
		}
		
	}
	
	public void start() { //Start the thread
		
		initializeAll();
		t = new Thread(this);
		t.start();
		
	}
	
	public void kill() { //Kill the thread
		running = false;
	}
	
	
}
