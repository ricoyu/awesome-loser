package com.loserico.jackson.core;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonModulesStudyTest {

	@Test
	public void testAllRegistedModules() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
	}
}
