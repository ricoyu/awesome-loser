package com.loserico.jackson.customserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Copyright: (C), 2020-09-17 10:20
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
	
	private String color;
	
	private String type;
}
