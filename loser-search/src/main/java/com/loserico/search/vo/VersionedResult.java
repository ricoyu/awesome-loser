package com.loserico.search.vo;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * Copyright: (C), 2023-08-14 15:35
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@Builder
public class VersionedResult<T> {
	
	private String id;
	
	private T doc;
	
	private Long ifSeqNo;
	
	private Long ifPrimaryTerm;
}
