package com.loserico.test.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 * Copyright: (C), 2020-8-1 0001 10:58
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoAnnotationTest {
	
	@Mock
	private List<String> mockedList;
	
	@Test
	public void whenNotUseMockAnnotation_thenCorrect() {
		List mockList = Mockito.mock(ArrayList.class);
		
		mockList.add("one");
		//验证add方法被调用了一次
		Mockito.verify(mockList).add("one");
		//调用了也没有效果?
		assertEquals(0, mockList.size());
		
		//意思是如果调用mockList.size()ff,固定返回100这个值
		Mockito.when(mockList.size()).thenReturn(100);
		assertEquals(100, mockList.size());
	}
	
	@Test
	public void whenUseMockAnnotation_thenMockIsInjected() {
		mockedList.add("one");
		Mockito.verify(mockedList).add("one");
		assertEquals(0, mockedList.size());
		
		Mockito.when(mockedList.size()).thenReturn(100);
		assertEquals(100, mockedList.size());
	}
	
	@Test
	public void whenNotUseSpyAnnotation_thenCorrect() {
		List<String> spyList = Mockito.spy(new ArrayList<>());
		spyList.add("one");
		spyList.add("two");
		
		Mockito.verify(spyList).add("one");
		Mockito.verify(spyList).add("two");
		
		assertEquals(2, spyList.size());
		
		Mockito.doReturn(100).when(spyList).size();
		assertEquals(100, spyList.size());
	}
	
	@Spy
	private List<String> spiedList = new ArrayList<>();
	
	@Test
	public void whenUseSpyAnnotation_thenSpyIsInjectedCorrectly() {
		spiedList.add("one");
		spiedList.add("two");
		
		Mockito.verify(spiedList).add("one");
		Mockito.verify(spiedList).add("two");
		
		assertEquals(2, spiedList.size());
		
		Mockito.doReturn(100).when(spiedList).size();
		assertEquals(100, spiedList.size());
	}
	
	@Test
	public void whenNotUseCaptorAnnotation_thenCorrect() {
		List mockList = Mockito.mock(List.class);
		ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
		
		mockList.add("one");
		Mockito.verify(mockList).add(arg.capture());
		
		//验证传的参数值是 "one"
		assertEquals("one", arg.getValue());
	}
	
	@Mock
	private List mockedList2;
	
	@Captor
	private ArgumentCaptor arg;
	
	@Test
	public void whenUseCaptorAnnotation_thenTheSam() {
		mockedList2.add("one");
		Mockito.verify(mockedList2).add(arg.capture());
		assertEquals("one", arg.getValue());
	}
}
