package com.loserico.jvm.reference;

public class User {
	
	private byte[] data = new byte[1024 * 1024];

	private String userId;

	public User(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "userId == " + userId;
	}

	@Override
	protected void finalize() throws Throwable {
		System.out.println("now finalize userId == " + userId);
	}

}
