package com.loserico.optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;

public class OptionalTest {

	/**
	 * It returns the value if is present, or the other specified otherwise.
	 */
	@Test
	public void orElseWhenNamePresentThenName() {
		Optional<String> petName = Optional.of("Bobby");
		assertEquals("Bobby", petName.orElse(""));
	}

	@Test
	public void orElseWhenNameNotPresentThenName() {
		Optional<String> petName = Optional.empty();
		assertEquals("Bobby", petName.orElse("Bobby"));
	}

	@Test
	public void isPresentGetWhenNamePresentThenName() {
		Optional<String> petNameOptional = Optional.of("Bobby");
		String petName = "";
		if (petNameOptional.isPresent()) {
			petName = petNameOptional.get();
		}
		assertEquals("Bobby", petName);
	}

	@Test
	public void isPresentGetWhenNameNotPresentThenEmptyString() {
		Optional<String> petNameOptional = Optional.empty();
		String petName = "";
		if (petNameOptional.isPresent()) {
			petName = petNameOptional.get();
		}
		assertEquals("", petName);
	}

	/**
	 * It returns the value if is present, or the other specified otherwise.
	 */
	@Test
	public void elseOrThrowWhenNamePresentThenName() {
		Optional<String> petName = Optional.of("Bobby");
		assertEquals("Bobby", petName.orElse(""));
	}

	/**
	 * It returns the value if is present, or throws the specified exception
	 * otherwise.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void elseOrThrowWhenNameNotPresentThenIllegalArgEx() {
		Optional<String> petName = Optional.empty();
		petName.orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * filter() is useful to specify other conditions on our object. It returns an
	 * Optional containing the value if is not empty and satisfy the specified
	 * predicate, an empty Optional otherwise.
	 * 
	 * In this example we want that the name not only is different from null but
	 * also that is not empty or made of only empty spaces.
	 */
	@Test
	public void filterWhenNameNotEmptyThenName() {
		Optional<String> petNameOpt = Optional.of("Bobby");
		String petName = petNameOpt.filter(name -> !name.trim().isEmpty())
				.orElseThrow(IllegalArgumentException::new);
		assertEquals("Bobby", petName);
	}

	/**
	 * tests for the null and the empty name
	 */
	@Test(expected = IllegalArgumentException.class)
	public void filterWhenNameNotPresentThenIllegalArgEx() {
		Optional<String> petNameOpt = Optional.empty();
		petNameOpt.filter(name -> !name.trim().isEmpty())
				.orElseThrow(IllegalArgumentException::new);
	}

	@Test(expected = IllegalArgumentException.class)
	public void filterWhenNameEmptyThenIllegalArgEx() {
		Optional<String> petNameOpt = Optional.of(" ");
		petNameOpt.filter(name -> !name.trim().isEmpty())
				.orElseThrow(IllegalArgumentException::new);
	}

	/*
	 * IfPresent, that it’s different from isPresent, accept a function, a Consumer,
	 * and executes it only if the value is present. We want to add 3 points to the
	 * loyalty card if loyalty card is actually present.
	 */
	@Test
	public void ifPresentWhenCardPresentThenPointsAdded() {
		LoyaltyCard mockedCard = mock(LoyaltyCard.class, "mockName");
		Optional<LoyaltyCard> loyaltyCard = Optional.of(mockedCard);
		loyaltyCard.ifPresent(c -> c.addPoints(3));
		// Verify addPoints method has been called 1 time and with input=3
		verify(mockedCard, times(1)).addPoints(3);
	}

	/*
	 * map() it’s a method that we use to transform an input in a different output.
	 * In this case nothing changes except that the map operation will be executed
	 * only if the value is actually present, otherwise it returns an empty
	 * Optional.
	 * 
	 * In this example we want to retrieve the number of points of our loyalty card
	 * if we have it, otherwise number of point will return 0.
	 */
	@Test
	public void mapWhenCardPresentThenNumber() {
		LoyaltyCard mockedCard = mock(LoyaltyCard.class);
		when(mockedCard.getPoints()).thenReturn(3);
		Optional<LoyaltyCard> card = Optional.of(mockedCard);
		int point = card.map(LoyaltyCard::getPoints).orElse(0);
		assertEquals(3, point);
	}

	@Test
	public void mapWhenCardNotPresentThenZero() {
		Optional<LoyaltyCard> card = Optional.empty();
		int point = card.map(LoyaltyCard::getPoints).orElse(0);
		assertEquals(0, point);
	}

	/*
	 * flatMap() it’s really similar to map() but when output is already an Optional
	 * it doesn’t wrap it with another Optional. So instead of having
	 * Optional<Optional<T>> if will just return Optional<T>.
	 */
	@Test
	public void flatMapWhenCardAndLastGiftPresentThenName() {
		/*
		 * We can now create a mocked Gift with name “Biography of Guybrush
		 * Threepwood”, put it into an Optional and make getLastGift return it.
		 */
		Gift mockedGift = mock(Gift.class);
		when(mockedGift.getName()).thenReturn("Biography of Guybrush Threepwood");
		LoyaltyCard mockedCard = mock(LoyaltyCard.class);
		when(mockedCard.getLastGift()).thenReturn(Optional.of(mockedGift));

		Optional<LoyaltyCard> card = Optional.of(mockedCard);
		String giftName = card.flatMap(LoyaltyCard::getLastGift)
				.map(Gift::getName)
				.orElse("");
		assertEquals("Biography of Guybrush Threepwood", giftName);
	}

	@Test
	public void testOptional() {
		Optional<String> fullName = Optional.ofNullable(null);
		System.out.println("Full Name is set? " + fullName.isPresent());
		System.out.println("Full Name: " + fullName.orElseGet(() -> "[none]"));
		System.out.println(fullName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));
	}

	@Test
	public void testOptional2() {
		Optional<String> firstName = Optional.of("Tom");
		System.out.println("First Name is set? " + firstName.isPresent());
		System.out.println("First Name: " + firstName.orElseGet(() -> "[none]"));
		System.out.println(firstName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));
		System.out.println();
	}
}
