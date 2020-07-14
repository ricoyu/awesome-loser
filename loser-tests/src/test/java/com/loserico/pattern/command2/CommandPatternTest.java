package com.loserico.pattern.command2;

/**
 * In the above class, we created a thread pool with 10 threads. Then, we set
 * different command objects with different jobs and add these jobs to the queue
 * using the addJob method of the ThreadPool class. As soon as the job is inserted
 * into the queue, a thread executes the job and removes it from the queue.
 * 
 * We have set different type of jobs, but by using the Command Design Pattern, we
 * decouple the job from the invoker thread. The thread will execute any kind of
 * object that implements the Job interface. The different command objects
 * encapsulate the different object and executed the requested operations on these
 * objects.
 * 
 * The output shows the different threads executing the different job. By watching
 * the job id in the output, you can clearly see that a single thread is executing
 * more than one job. This is because after executing a job the thread sends back to
 * the pool.
 * 
 * The advantage of the Command Design Pattern is that you can add more different
 * kind of jobs without changing the existing classes. The leads to more
 * flexibility, and maintainability and also reduce the chances of bugs in the code.
 * 
 * @author Loser
 * @since Aug 24, 2016
 * @version
 *
 */
public class CommandPatternTest {

	public static void main(String[] args) {
		init();
	}

	private static void init() {
		ThreadPool pool = new ThreadPool(10);

		Email email = null;
		EmailJob emailJob = new EmailJob();

		Sms sms = null;
		SmsJob smsJob = new SmsJob();

		FileIO fileIO = null;
		FileIOJob fileIOJob = new FileIOJob();

		Logging logging = null;
		LoggingJob logJob = new LoggingJob();

		for (int i = 0; i < 5; i++) {
			email = new Email();
			emailJob.setEmail(email);

			sms = new Sms();
			smsJob.setSms(sms);

			fileIO = new FileIO();
			fileIOJob.setFileIO(fileIO);

			logging = new Logging();
			logJob.setLogging(logging);

			pool.addJob(emailJob);
			pool.addJob(smsJob);
			pool.addJob(fileIOJob);
			pool.addJob(logJob);
		}
		pool.shutdownPool();
	}

}