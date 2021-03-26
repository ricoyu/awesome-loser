package com.loserico.search.pojo;

import com.loserico.searchlegacy.annotation.DocId;
import lombok.Data;

/**
 * <p>
 * Copyright: (C), 2021-02-08 12:58
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class Movie {
	
	@DocId
	private Long id;
	
	private Integer year;
	
	private String title;
	
	private String[] genre;
}
