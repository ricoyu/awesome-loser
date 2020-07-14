package com.loserico.optional;

import java.util.Optional;

public class LoyaltyCard {
	private String cardNumber;
	private int points;

	public int addPoints(int pointToAdd) {
		System.out.println(pointToAdd);
		setPoints(getPoints() + pointToAdd);
		System.out.println(points);
		return points;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Optional<Gift> getLastGift() {
		// whatever
		return Optional.empty();
	}
}