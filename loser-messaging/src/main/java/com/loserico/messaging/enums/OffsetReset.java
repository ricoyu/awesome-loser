package com.loserico.messaging.enums;

/**
 * What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server:
 * <ul>
 * <li>earliest</li>       automatically reset the offset to the earliest offset
 * <li>latest</li>         automatically reset the offset to the latest offset
 * <li>none</li>           throw exception to the consumer if no previous offset is found for the consumer's group
 * <li>anything else</li>  throw exception to the consumer.
 * </ul>
 * <p>
 * Copyright: Copyright (c) 2021-04-27 13:47
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public enum OffsetReset {
	
	EARLIEST,
	
	LATEST,
	
	NONE;
	
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
