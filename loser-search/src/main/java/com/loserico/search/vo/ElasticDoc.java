package com.loserico.search.vo;

import lombok.Data;

/**
 * 承载了丰富内容的Elasticsearch文档对象
 * <p>
 * Copyright: Copyright (c) 2024-08-31 21:28
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class ElasticDoc {

	private String docId;

	private Long version;

	private Long seqNo;

	private Long primaryTerm;

	private String source;
}
