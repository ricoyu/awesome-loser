package com.loserico.jackson.customdate;

import com.loserico.jackson.customserializer.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * Copyright: (C), 2020-09-17 10:48
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
public class Request {
	
	private Car car;
	
	private Date datePurchased;
}
