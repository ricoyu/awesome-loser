package com.loserico.test.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 * Copyright: (C), 2020-8-1 0001 11:43
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class InjectMocksTest {
	
	@Mock
	private Map<String, String> wordMap;
	
	//将上面Mock的wordMap注入到dic里面
	@InjectMocks
	private MyDictionary dic = new MyDictionary();
	
	@Test
	public void whenUseInjectMocksAnnotation_thenCorrect() {
		Mockito.when(wordMap.get("aWord")).thenReturn("aMeaning");
		
		assertEquals("aMeaning", dic.getMeaning("aWord"));
	}
	
	public class MyDictionary {
		Map<String, String> wordMap;
		
		public MyDictionary() {
			wordMap = new HashMap<>();
		}
		
		public void add(final String word, final String meaning) {
			wordMap.put(word, meaning);
		}
		
		public String getMeaning(final String word) {
			return wordMap.get(word);
		}
	}
}
