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
public class VersionedDoc<T> {
	
	private String id;
	
	private T source;
	
	private Long ifSeqNo;
	
	private Long ifPrimaryTerm;

	private Long version;
}
