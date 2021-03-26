package com.loserico.searchlegacy.enums;

/**
 * <p>
 * Copyright: (C), 2021-02-13 21:15
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public enum SuggestSort {
	
	/** 
	 * Sort should first be based on score, then document frequency and then the term itself. 
	 */
	SCORE,
	
	/** 
	 * Sort should first be based on document frequency, then score and then the term itself. 
	 */
	FREQUENCY;
}
