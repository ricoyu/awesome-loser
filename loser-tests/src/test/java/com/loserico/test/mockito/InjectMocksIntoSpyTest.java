package com.loserico.test.mockito;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2020-8-1 0001 11:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class InjectMocksIntoSpyTest {
	
	@Mock
	private Map<String, String> wordMap;
	
	private MyDictionary spyDic = null;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		spyDic = Mockito.spy(new MyDictionary(wordMap));
	}
	
	@Test
	public void whenUseInjectMocksAnnotation_thenCorrect() {
		Mockito.when(wordMap.get("aWord")).thenReturn("aMeaning");
		Assert.assertEquals("aMeaning", spyDic.getMeaning("aWord"));
	}
	
	public class MyDictionary {
		Map<String, String> wordMap;
		
		public MyDictionary() {
			wordMap = new HashMap<>();
		}
		
		public MyDictionary(Map<String, String> wordMap) {
			this.wordMap = wordMap;
		}
		
		public void add(final String word, final String meaning) {
			wordMap.put(word, meaning);
		}
		
		public String getMeaning(final String word) {
			return wordMap.get(word);
		}
	}
}
