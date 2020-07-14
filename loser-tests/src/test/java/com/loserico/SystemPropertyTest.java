package com.loserico;

import org.junit.Test;

import java.util.Properties;

public class SystemPropertyTest {
	
	@Test
	public void testProperties() {
		System.out.println(System.getProperty("user.dir"));
		System.out.println(System.getProperty("user.home"));
	}
	
	@Test
	public void testAllSystemProperties() {
		Properties properties = System.getProperties();
		properties.forEach((property, value) -> {
			System.out.println(property +": " + value);
		});
	}
	
	@Test
	public void testSystemProcesors() {
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
}
