package com.loserico.json;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class BookSearchResultVO {
	
	private Integer id;
	
	private String name;
	
	private Integer language;
	
	private Integer type;
	
	private Integer level;
	
	private String author;
	
	private String imgurl;
	
	private Integer score;
	
	private String publisher;
	
	private LocalDateTime publishdate;
	
	private Integer wordCount;
	
	private BigDecimal praiseRate;
	
	private Integer pageCount;
	
	private BigDecimal price;
	
	private String isbn;
	
	private Integer readerNumber;
	
	private Integer recommendNumber;
	
	private Integer bookSize;
	
	private String description;
	
	private Integer createTchId;
	
	private Integer state;
	
	private Byte isDelete;
	
	private Integer creator;
	
	private LocalDateTime createTime;
	
	private Integer updator;
	
	private LocalDateTime updateTime;
	
	private Map<String, String> highlightMaps;
	
}