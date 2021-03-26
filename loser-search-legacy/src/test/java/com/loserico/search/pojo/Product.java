package com.loserico.search.pojo;

import com.loserico.searchlegacy.annotation.DocId;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

/**
 * <p>
 * Copyright: (C), 2021-02-09 10:43
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@ToString
public class Product {
	@DocId
	private Long id;
	
	private Long price;
	
	private boolean avaliable;
	
	private LocalDate date;
	
	private String productID;
}
