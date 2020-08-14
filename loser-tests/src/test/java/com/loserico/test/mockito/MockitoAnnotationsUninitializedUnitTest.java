package com.loserico.test.mockito;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

/**
 * <p>
 * Copyright: (C), 2020-8-1 0001 11:55
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class MockitoAnnotationsUninitializedUnitTest {
	
	@Mock
	private List<String> mockedList;
	
	@Test(expected = NullPointerException.class)
	public void whenMockitoAnnotationsUninitialized_thenNPEThrown() {
		Mockito.when(mockedList.size()).thenReturn(1);
	}
}
