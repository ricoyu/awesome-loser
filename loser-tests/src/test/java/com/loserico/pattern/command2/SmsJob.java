package com.loserico.pattern.command2;

/**
 * The above classes hold a reference to their respective classes that will be used
 * to get the job done. The classes override the run method and do the work
 * requested. For example, the SmsJob class is used to send sms, its run method
 * calls the sendSms method of the Sms object in order to get the job done.
 * 
 * @author Loser
 * @since Aug 24, 2016
 * @version
 *
 */
public class SmsJob implements Job {

	private Sms sms;

	public void setSms(Sms sms) {
		this.sms = sms;
	}

	@Override
	public void run() {
		System.out.println("Job ID: " + Thread.currentThread().getId() + " executing sms jobs.");
		if (sms != null) {
			sms.sendSms();
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}

}