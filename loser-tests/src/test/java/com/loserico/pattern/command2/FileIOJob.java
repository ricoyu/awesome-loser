package com.loserico.pattern.command2;

public class FileIOJob implements Job {

	private FileIO fileIO;

	public void setFileIO(FileIO fileIO) {
		this.fileIO = fileIO;
	}

	@Override
	public void run() {
		System.out.println("Job ID: " + Thread.currentThread().getId() + " executing fileIO jobs.");
		if (fileIO != null) {
			fileIO.execute();
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}
}