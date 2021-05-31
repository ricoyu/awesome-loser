package com.loserico;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Locale;

/**
 * <p>
 * Copyright: (C), 2021-05-24 9:13
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class FakerTest {
	
	@Test
	public void test() {
		Locale locale;
		Faker faker = new Faker(Locale.ENGLISH);
		for (int i = 0; i < 100; i++) {
			String name = faker.name().fullName();
			String username = faker.name().username();
			String address = faker.address().streetAddress();
			String position = faker.job().position();
			log.info("name: {}, username: {}, address: {}, position: {}", name, username, address, position);
		}
	}
}
