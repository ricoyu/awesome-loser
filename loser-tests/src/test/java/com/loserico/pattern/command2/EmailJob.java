package com.loserico.pattern.command2;

import static java.util.concurrent.TimeUnit.*;

/**
 * The following are the different command classes that encapsulate the above
 * classes and implement the Job interface.
 * 
 * @author Loser
 * @since Aug 24, 2016
 * @version
 *
 */
public class EmailJob implements Job {

	private Email email;

	public void setEmail(Email email) {
		this.email = email;
	}

	@Override
	public void run() {
		System.out.println("Job ID: " + Thread.currentThread().getId() + " executing email jobs.");
		if (email != null) {
			email.sendEmail();
		}

		try {
			SECONDS.sleep(1);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}

}